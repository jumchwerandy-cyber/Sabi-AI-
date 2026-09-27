package com.example.data.repository

import com.example.data.local.ConversationEntity
import com.example.data.local.DocumentEntity
import com.example.data.local.FeedbackEntity
import com.example.data.local.MessageEntity
import com.example.data.local.SabiDao
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.ConversationMode
import com.example.data.model.FeedbackCategory
import com.example.data.model.SabiChatRequest
import com.example.data.model.SabiChatResponse
import com.example.data.model.SabiLanguage
import com.example.data.remote.SabiAiBackendService
import com.example.data.remote.SabiAiBackendServiceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class SabiRepository(
    private val sabiDao: SabiDao,
    private val aiService: SabiAiBackendService = SabiAiBackendServiceImpl(),
    val preferencesDataStore: com.example.data.local.SabiPreferencesDataStore? = null,
    val firestoreService: com.example.data.remote.SabiFirestoreService = com.example.data.remote.SabiFirestoreService()
) {

    val conversations: Flow<List<ConversationEntity>> = sabiDao.getAllConversations()
    val allFeedback: Flow<List<FeedbackEntity>> = sabiDao.getAllFeedback()
    val userPreferences: Flow<UserPreferencesEntity?> = sabiDao.getUserPreferences()
    val documents: Flow<List<DocumentEntity>> = sabiDao.getAllDocuments()

    val dataStoreLanguage: Flow<String>? = preferencesDataStore?.selectedLanguage
    val dataStoreMode: Flow<String>? = preferencesDataStore?.selectedMode
    val dataStoreActiveModel: Flow<String>? = preferencesDataStore?.activeModelId

    suspend fun saveCachedPreferences(lang: String, mode: String, model: String) {
        preferencesDataStore?.saveSelectedLanguage(lang)
        preferencesDataStore?.saveSelectedMode(mode)
        preferencesDataStore?.saveActiveModel(model)
    }

    suspend fun saveLastActiveConversation(convId: String) {
        preferencesDataStore?.saveLastConversationId(convId)
    }

    fun getMessagesForConversation(convId: String): Flow<List<MessageEntity>> {
        return sabiDao.getMessagesForConversation(convId)
    }

    suspend fun createNewConversation(
        title: String = "New Conversation",
        language: SabiLanguage = SabiLanguage.ENGLISH,
        mode: ConversationMode = ConversationMode.CASUAL
    ): String {
        val id = UUID.randomUUID().toString()
        val conv = ConversationEntity(
            id = id,
            title = title,
            language = language.id,
            mode = mode.id
        )
        sabiDao.insertConversation(conv)
        firestoreService.syncConversation(conv)
        return id
    }

    val totalConversationCount: Flow<Int> = sabiDao.getConversationCount()
    val totalMessageCount: Flow<Int> = sabiDao.getTotalMessageCount()

    fun searchConversations(query: String): Flow<List<ConversationEntity>> {
        return if (query.isBlank()) {
            sabiDao.getAllConversations()
        } else {
            sabiDao.searchConversations(query.trim())
        }
    }

    fun searchMessages(query: String): Flow<List<MessageEntity>> {
        return sabiDao.searchMessages(query.trim())
    }

    suspend fun setConversationPinned(id: String, isPinned: Boolean) {
        sabiDao.setConversationPinned(id, isPinned)
    }

    suspend fun deleteConversation(id: String) {
        sabiDao.deleteConversationById(id)
    }

    suspend fun deleteAllConversations() {
        sabiDao.deleteAllConversations()
    }

    suspend fun updateConversationTitle(id: String, newTitle: String) {
        val conv = sabiDao.getConversationById(id)
        if (conv != null) {
            sabiDao.updateConversation(conv.copy(title = newTitle, updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun sendMessage(
        conversationId: String,
        userPrompt: String,
        language: SabiLanguage,
        mode: ConversationMode,
        activeModelId: String = "sabi_v1"
    ): SabiChatResponse {
        val userMsgId = UUID.randomUUID().toString()
        val userMessage = MessageEntity(
            id = userMsgId,
            conversationId = conversationId,
            role = "user",
            content = userPrompt,
            timestamp = System.currentTimeMillis(),
            language = language.id,
            mode = mode.id
        )
        sabiDao.insertMessage(userMessage)
        firestoreService.syncMessage(userMessage)

        // Retrieve existing history
        val history = sabiDao.getMessageListForConversation(conversationId)

        // Retrieve knowledge base documents via intelligent RAG scoring
        val allDocs = sabiDao.getAllDocuments().firstOrNull() ?: emptyList()
        val queryTokens = userPrompt.lowercase()
            .split(Regex("[^a-zA-Z0-9]+"))
            .filter { it.length >= 3 }

        val relevantDocs = if (queryTokens.isEmpty()) {
            emptyList()
        } else {
            allDocs.map { doc ->
                var score = 0
                val titleLower = doc.title.lowercase()
                val kwLower = doc.keywords.lowercase()
                val contentLower = doc.content.lowercase()

                for (token in queryTokens) {
                    if (titleLower.contains(token)) score += 5
                    if (kwLower.contains(token)) score += 4
                    if (contentLower.contains(token)) score += 1
                }
                Pair(doc, score)
            }
                .filter { it.second > 0 }
                .sortedByDescending { it.second }
                .map { it.first }
                .take(3)
        }

        // Call AI backend
        val request = SabiChatRequest(
            message = userPrompt,
            language = language.id,
            mode = mode.id,
            conversationId = conversationId
        )

        val aiResponse = aiService.sendChat(
            request = request,
            conversationHistory = history,
            relevantDocuments = relevantDocs,
            activeModelId = activeModelId
        )

        // Save AI Response
        val aiMsgId = UUID.randomUUID().toString()
        val aiMessage = MessageEntity(
            id = aiMsgId,
            conversationId = conversationId,
            role = "assistant",
            content = aiResponse.response,
            timestamp = System.currentTimeMillis(),
            language = aiResponse.language,
            mode = mode.id
        )
        sabiDao.insertMessage(aiMessage)
        firestoreService.syncMessage(aiMessage)

        // Update conversation's last message and timestamp
        val conv = sabiDao.getConversationById(conversationId)
        if (conv != null) {
            val updatedTitle = if (conv.title == "New Conversation" && userPrompt.isNotBlank()) {
                if (userPrompt.length > 32) userPrompt.take(32) + "..." else userPrompt
            } else {
                conv.title
            }
            val updatedConv = conv.copy(
                title = updatedTitle,
                lastMessage = aiResponse.response.take(64),
                updatedAt = System.currentTimeMillis(),
                language = language.id,
                mode = mode.id
            )
            sabiDao.updateConversation(updatedConv)
            firestoreService.syncConversation(updatedConv)
        }

        return aiResponse
    }

    suspend fun submitFeedback(
        messageId: String,
        conversationId: String,
        isHelpful: Boolean,
        category: FeedbackCategory?,
        comment: String,
        userQuery: String,
        aiResponse: String,
        language: String,
        mode: String
    ) {
        val state = if (isHelpful) 1 else -1
        sabiDao.updateMessageFeedback(
            messageId = messageId,
            state = state,
            category = category?.label,
            comment = comment
        )

        val feedbackRecord = FeedbackEntity(
            messageId = messageId,
            conversationId = conversationId,
            userQuery = userQuery,
            aiResponse = aiResponse,
            category = category?.label ?: if (isHelpful) "Helpful" else "Unhelpful",
            comment = comment,
            language = language,
            mode = mode,
            timestamp = System.currentTimeMillis(),
            reviewed = false
        )
        sabiDao.insertFeedback(feedbackRecord)
        firestoreService.syncFeedback(feedbackRecord)
    }

    suspend fun markFeedbackReviewed(id: String) {
        sabiDao.markFeedbackReviewed(id)
    }

    suspend fun saveUserPreferences(preferences: UserPreferencesEntity) {
        sabiDao.saveUserPreferences(preferences)
        firestoreService.syncUserPreferences(preferences)
    }

    suspend fun getFirestoreAggregatedMetrics(localConvCount: Int = 0, localMsgCount: Int = 0): com.example.data.remote.FirestoreAggregatedMetrics {
        return firestoreService.fetchAggregatedMetrics(localConvCount, localMsgCount)
    }

    fun searchKnowledge(query: String): Flow<List<DocumentEntity>> {
        return if (query.isBlank()) {
            sabiDao.getAllDocuments()
        } else {
            sabiDao.searchDocuments(query)
        }
    }
}
