package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ConversationEntity
import com.example.data.local.DocumentEntity
import com.example.data.local.FeedbackEntity
import com.example.data.local.MessageEntity
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.ConversationMode
import com.example.data.model.FeedbackCategory
import com.example.data.model.SabiLanguage
import com.example.data.repository.SabiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SabiScreen {
    LANDING,
    CHAT,
    ASSISTANTS,
    CONTEXT_ENGINE,
    ADMIN,
    PROFILE
}

class SabiViewModel(
    private val repository: SabiRepository
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(SabiScreen.LANDING)
    val currentScreen: StateFlow<SabiScreen> = _currentScreen.asStateFlow()

    val conversations: StateFlow<List<ConversationEntity>> = repository.conversations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFeedback: StateFlow<List<FeedbackEntity>> = repository.allFeedback
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userPreferences: StateFlow<UserPreferencesEntity?> = repository.userPreferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _activeConversationId = MutableStateFlow<String?>("welcome_conv_01")
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val activeMessages: StateFlow<List<MessageEntity>> = _activeConversationId
        .flatMapLatest { convId ->
            if (convId != null) repository.getMessagesForConversation(convId)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedLanguage = MutableStateFlow(SabiLanguage.PIDGIN)
    val selectedLanguage: StateFlow<SabiLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedMode = MutableStateFlow(ConversationMode.CASUAL)
    val selectedMode: StateFlow<ConversationMode> = _selectedMode.asStateFlow()

    private val _activeModelId = MutableStateFlow("sabi_v1")
    val activeModelId: StateFlow<String> = _activeModelId.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Dialog & Sheet States
    private val _showFeedbackDialog = MutableStateFlow(false)
    val showFeedbackDialog: StateFlow<Boolean> = _showFeedbackDialog.asStateFlow()

    private val _feedbackTargetMessage = MutableStateFlow<MessageEntity?>(null)
    val feedbackTargetMessage: StateFlow<MessageEntity?> = _feedbackTargetMessage.asStateFlow()

    private val _showVoiceDialog = MutableStateFlow(false)
    val showVoiceDialog: StateFlow<Boolean> = _showVoiceDialog.asStateFlow()

    private val _showAttachmentDialog = MutableStateFlow(false)
    val showAttachmentDialog: StateFlow<Boolean> = _showAttachmentDialog.asStateFlow()

    private val _showLanguageSheet = MutableStateFlow(false)
    val showLanguageSheet: StateFlow<Boolean> = _showLanguageSheet.asStateFlow()

    private val _showModeSheet = MutableStateFlow(false)
    val showModeSheet: StateFlow<Boolean> = _showModeSheet.asStateFlow()

    // Knowledge Base State
    private val _knowledgeQuery = MutableStateFlow("")
    val knowledgeQuery: StateFlow<String> = _knowledgeQuery.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val knowledgeDocuments: StateFlow<List<DocumentEntity>> = _knowledgeQuery
        .flatMapLatest { query -> repository.searchKnowledge(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Assistant / Rewriter State
    private val _assistantInputText = MutableStateFlow("")
    val assistantInputText: StateFlow<String> = _assistantInputText.asStateFlow()

    private val _assistantResultText = MutableStateFlow("")
    val assistantResultText: StateFlow<String> = _assistantResultText.asStateFlow()

    private val _assistantToolType = MutableStateFlow("nigerianize") // nigerianize, professional, simplify, translate
    val assistantToolType: StateFlow<String> = _assistantToolType.asStateFlow()

    private val _isAssistantProcessing = MutableStateFlow(false)
    val isAssistantProcessing: StateFlow<Boolean> = _isAssistantProcessing.asStateFlow()

    fun navigateTo(screen: SabiScreen) {
        _currentScreen.value = screen
    }

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setLanguage(language: SabiLanguage) {
        _selectedLanguage.value = language
        _showLanguageSheet.value = false
    }

    fun setMode(mode: ConversationMode) {
        _selectedMode.value = mode
        _showModeSheet.value = false
    }

    fun setModel(modelId: String) {
        _activeModelId.value = modelId
    }

    fun openLanguageSheet(show: Boolean) {
        _showLanguageSheet.value = show
    }

    fun openModeSheet(show: Boolean) {
        _showModeSheet.value = show
    }

    fun openVoiceDialog(show: Boolean) {
        _showVoiceDialog.value = show
    }

    fun openAttachmentDialog(show: Boolean) {
        _showAttachmentDialog.value = show
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun selectConversation(id: String) {
        _activeConversationId.value = id
        _currentScreen.value = SabiScreen.CHAT
    }

    fun startNewChat(initialPrompt: String? = null) {
        viewModelScope.launch {
            val title = if (!initialPrompt.isNullOrBlank()) {
                if (initialPrompt.length > 25) initialPrompt.take(25) + "..." else initialPrompt
            } else {
                "New Conversation"
            }
            val newId = repository.createNewConversation(
                title = title,
                language = _selectedLanguage.value,
                mode = _selectedMode.value
            )
            _activeConversationId.value = newId
            _currentScreen.value = SabiScreen.CHAT

            if (!initialPrompt.isNullOrBlank()) {
                sendUserMessage(initialPrompt)
            }
        }
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            repository.deleteConversation(id)
            if (_activeConversationId.value == id) {
                val remaining = conversations.value.filter { it.id != id }
                _activeConversationId.value = remaining.firstOrNull()?.id
            }
        }
    }

    fun sendUserMessage(overridePrompt: String? = null) {
        val prompt = overridePrompt ?: _inputText.value.trim()
        if (prompt.isBlank()) return

        val convId = _activeConversationId.value
        if (convId == null) {
            startNewChat(prompt)
            return
        }

        _inputText.value = ""
        _isLoading.value = true

        viewModelScope.launch {
            try {
                repository.sendMessage(
                    conversationId = convId,
                    userPrompt = prompt,
                    language = _selectedLanguage.value,
                    mode = _selectedMode.value,
                    activeModelId = _activeModelId.value
                )
            } catch (e: Exception) {
                _snackbarMessage.value = "Error sending message: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitFeedbackPositive(message: MessageEntity) {
        viewModelScope.launch {
            repository.submitFeedback(
                messageId = message.id,
                conversationId = message.conversationId,
                isHelpful = true,
                category = null,
                comment = "Helpful response",
                userQuery = "",
                aiResponse = message.content,
                language = message.language,
                mode = message.mode
            )
            _snackbarMessage.value = "Daalu! Feedback recorded. 👍"
        }
    }

    fun openNegativeFeedbackDialog(message: MessageEntity) {
        _feedbackTargetMessage.value = message
        _showFeedbackDialog.value = true
    }

    fun closeFeedbackDialog() {
        _showFeedbackDialog.value = false
        _feedbackTargetMessage.value = null
    }

    fun submitNegativeFeedback(category: FeedbackCategory, explanation: String) {
        val target = _feedbackTargetMessage.value ?: return
        viewModelScope.launch {
            repository.submitFeedback(
                messageId = target.id,
                conversationId = target.conversationId,
                isHelpful = false,
                category = category,
                comment = explanation,
                userQuery = "",
                aiResponse = target.content,
                language = target.language,
                mode = target.mode
            )
            closeFeedbackDialog()
            _snackbarMessage.value = "Nagode! Feedback recorded for model fine-tuning. 🇳🇬"
        }
    }

    fun markAdminFeedbackReviewed(feedbackId: String) {
        viewModelScope.launch {
            repository.markFeedbackReviewed(feedbackId)
            _snackbarMessage.value = "Feedback marked as reviewed for dataset."
        }
    }

    fun setKnowledgeSearchQuery(query: String) {
        _knowledgeQuery.value = query
    }

    // Assistant / Rewriter functions
    fun setAssistantInput(text: String) {
        _assistantInputText.value = text
    }

    fun setAssistantTool(tool: String) {
        _assistantToolType.value = tool
    }

    fun runAssistantRewrite() {
        val input = _assistantInputText.value.trim()
        if (input.isBlank()) return

        _isAssistantProcessing.value = true
        viewModelScope.launch {
            val convId = _activeConversationId.value ?: "temp_rewrite"
            val prompt = when (_assistantToolType.value) {
                "nigerianize" -> "Rewrite this message to sound authentically Nigerian and natural: \"$input\""
                "professional" -> "Rewrite this message/Pidgin into formal, polished corporate English: \"$input\""
                "simplify" -> "Explain this in very simple language with Nigerian everyday examples: \"$input\""
                "translate" -> "Translate this between English, Nigerian Pidgin, and explain the nuance: \"$input\""
                else -> "Improve this text: \"$input\""
            }

            try {
                val mode = when (_assistantToolType.value) {
                    "professional" -> ConversationMode.PROFESSIONAL
                    "simplify" -> ConversationMode.SIMPLE
                    "nigerianize" -> ConversationMode.PIDGIN
                    else -> ConversationMode.CASUAL
                }

                val response = repository.sendMessage(
                    conversationId = convId,
                    userPrompt = prompt,
                    language = _selectedLanguage.value,
                    mode = mode,
                    activeModelId = _activeModelId.value
                )
                _assistantResultText.value = response.response
            } catch (e: Exception) {
                _assistantResultText.value = "Could not process rewrite: ${e.message}"
            } finally {
                _isAssistantProcessing.value = false
            }
        }
    }

    fun saveProfilePreferences(displayName: String, email: String, defaultLang: String, defaultMode: String) {
        viewModelScope.launch {
            repository.saveUserPreferences(
                UserPreferencesEntity(
                    id = "default_user",
                    displayName = displayName,
                    email = email,
                    defaultLanguage = defaultLang,
                    defaultMode = defaultMode,
                    isGoogleLinked = true
                )
            )
            _selectedLanguage.value = SabiLanguage.fromId(defaultLang)
            _selectedMode.value = ConversationMode.fromId(defaultMode)
            _snackbarMessage.value = "Preferences saved successfully!"
        }
    }
}

class SabiViewModelFactory(private val repository: SabiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SabiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SabiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
