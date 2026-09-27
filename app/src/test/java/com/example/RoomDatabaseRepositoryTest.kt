package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.*
import com.example.data.model.ConversationMode
import com.example.data.model.FeedbackCategory
import com.example.data.model.SabiLanguage
import com.example.data.repository.SabiRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RoomDatabaseRepositoryTest {

    private lateinit var database: SabiDatabase
    private lateinit var dao: SabiDao
    private lateinit var repository: SabiRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SabiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.sabiDao()
        repository = SabiRepository(sabiDao = dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testCreateAndRetrieveConversation() = runTest {
        val convId = repository.createNewConversation(
            initialTitle = "Naija Tech Discussion",
            language = "pcm",
            mode = "casual"
        )
        assertNotNull(convId)

        val conversations = repository.conversations.first()
        assertEquals(1, conversations.size)
        val created = conversations.first()
        assertEquals("Naija Tech Discussion", created.title)
        assertEquals("pcm", created.language)
        assertEquals("casual", created.mode)
        assertFalse(created.isPinned)
    }

    @Test
    fun testUpdateConversationTitleAndPin() = runTest {
        val convId = repository.createNewConversation(
            initialTitle = "Initial Title",
            language = "pcm",
            mode = "business"
        )

        repository.updateConversationTitle(convId, "Updated Business Plan")
        repository.setConversationPinned(convId, true)

        val conversations = repository.conversations.first()
        val conv = conversations.find { it.id == convId }
        assertNotNull(conv)
        assertEquals("Updated Business Plan", conv?.title)
        assertTrue(conv?.isPinned == true)
    }

    @Test
    fun testMessageInsertionAndOrder() = runTest {
        val convId = repository.createNewConversation("Chat with SABI")

        val msg1 = MessageEntity(
            id = "msg_001",
            conversationId = convId,
            role = "user",
            content = "How body dey?",
            timestamp = 1000L,
            language = "pcm"
        )
        val msg2 = MessageEntity(
            id = "msg_002",
            conversationId = convId,
            role = "assistant",
            content = "Body dey kampe! Wetin dey happen?",
            timestamp = 2000L,
            language = "pcm"
        )

        dao.insertMessage(msg1)
        dao.insertMessage(msg2)

        val messages = repository.getMessagesForConversation(convId).first()
        assertEquals(2, messages.size)
        assertEquals("msg_001", messages[0].id)
        assertEquals("msg_002", messages[1].id)
        assertEquals("user", messages[0].role)
        assertEquals("assistant", messages[1].role)
    }

    @Test
    fun testMessageFeedbackSubmission() = runTest {
        val convId = repository.createNewConversation("Feedback Test Session")
        val msg = MessageEntity(
            id = "msg_feedback_test",
            conversationId = convId,
            role = "assistant",
            content = "Lagos traffic na normal thing.",
            timestamp = 1500L,
            language = "pcm"
        )
        dao.insertMessage(msg)

        repository.submitMessageFeedback(
            messageId = msg.id,
            conversationId = convId,
            isHelpful = true,
            category = FeedbackCategory.HELPFUL,
            comment = "Very authentic pidgin tone",
            userQuery = "Tell me about Lagos",
            aiResponse = msg.content,
            language = "pcm",
            mode = "casual"
        )

        val allFeedback = repository.allFeedback.first()
        assertEquals(1, allFeedback.size)
        val fb = allFeedback.first()
        assertEquals(msg.id, fb.messageId)
        assertEquals("Helpful", fb.category)
        assertEquals("Very authentic pidgin tone", fb.comment)
        assertFalse(fb.reviewed)

        repository.markFeedbackReviewed(fb.id)
        val updatedFb = repository.allFeedback.first().first()
        assertTrue(updatedFb.reviewed)
    }

    @Test
    fun testSearchConversationsOffline() = runTest {
        repository.createNewConversation(initialTitle = "Cooking Jollof Rice")
        repository.createNewConversation(initialTitle = "Flutterwave API Docs")
        repository.createNewConversation(initialTitle = "Afrobeats Playlist 2026")

        val searchResult1 = repository.searchConversations("Jollof").first()
        assertEquals(1, searchResult1.size)
        assertEquals("Cooking Jollof Rice", searchResult1.first().title)

        val searchResult2 = repository.searchConversations("API").first()
        assertEquals(1, searchResult2.size)
        assertEquals("Flutterwave API Docs", searchResult2.first().title)

        val searchAll = repository.searchConversations("").first()
        assertEquals(3, searchAll.size)
    }

    @Test
    fun testDeleteConversation() = runTest {
        val convId1 = repository.createNewConversation("Session to keep")
        val convId2 = repository.createNewConversation("Session to delete")

        assertEquals(2, repository.conversations.first().size)

        repository.deleteConversation(convId2)
        val remaining = repository.conversations.first()
        assertEquals(1, remaining.size)
        assertEquals(convId1, remaining.first().id)
    }

    @Test
    fun testUserPreferencesPersistence() = runTest {
        val prefs = UserPreferencesEntity(
            id = "user_default",
            displayName = "Chidi Okafor",
            email = "chidi@example.com",
            defaultLanguage = "pcm",
            defaultMode = "business",
            isGoogleLinked = true
        )

        repository.saveUserPreferences(prefs)
        val loaded = repository.userPreferences.first()
        assertNotNull(loaded)
        assertEquals("Chidi Okafor", loaded?.displayName)
        assertEquals("chidi@example.com", loaded?.email)
        assertEquals("business", loaded?.defaultMode)
        assertTrue(loaded?.isGoogleLinked == true)
    }

    @Test
    fun testKnowledgeDocumentSearch() = runTest {
        val doc1 = DocumentEntity(
            id = "doc_jollof",
            title = "Authentic Nigerian Jollof Rice Recipe",
            content = "Key ingredients include long-grain parboiled rice, tomato paste, scotch bonnet peppers, and firewood aroma.",
            category = "culinary",
            language = "en",
            source = "Sabi Culinary Archives"
        )
        val doc2 = DocumentEntity(
            id = "doc_tech",
            title = "Nigeria FinTech & Payment Gateways",
            content = "Overview of Paystack, Flutterwave, Moniepoint, and Interswitch payment rails.",
            category = "fintech",
            language = "en",
            source = "Sabi FinTech Guide"
        )
        dao.insertDocument(doc1)
        dao.insertDocument(doc2)

        val searchResult = repository.searchKnowledge("Jollof").first()
        assertEquals(1, searchResult.size)
        assertEquals("doc_jollof", searchResult.first().id)

        val allDocs = repository.searchKnowledge("").first()
        assertEquals(2, allDocs.size)
    }
}
