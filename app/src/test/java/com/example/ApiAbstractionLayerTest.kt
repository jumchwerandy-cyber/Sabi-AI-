package com.example

import com.example.data.local.DocumentEntity
import com.example.data.local.MessageEntity
import com.example.data.model.SabiChatRequest
import com.example.data.model.SabiLanguage
import com.example.data.remote.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class FakeGeminiApiService(
    var shouldThrow: Boolean = false,
    var mockResponseText: String = "SABI AI Gemini Response: Body dey kampe!"
) : GeminiApiService {

    var lastApiKeyReceived: String? = null
    var lastRequestReceived: GeminiRequest? = null

    override suspend fun generateContent(apiKey: String, request: GeminiRequest): GeminiResponse {
        lastApiKeyReceived = apiKey
        lastRequestReceived = request

        if (shouldThrow) {
            throw IOException("Simulated network error: HTTP 503 Service Unavailable")
        }

        return GeminiResponse(
            candidates = listOf(
                GeminiCandidate(
                    content = GeminiContent(
                        role = "model",
                        parts = listOf(GeminiPart(text = mockResponseText))
                    )
                )
            )
        )
    }
}

class ApiAbstractionLayerTest {

    @Test
    fun testUnsupportedLanguageReturnsGracefulPreviewNotice() = runTest {
        val fakeApi = FakeGeminiApiService()
        val service = SabiAiBackendServiceImpl(geminiService = fakeApi)

        val requestIgbo = SabiChatRequest(
            message = "Kedụ ka ị mere?",
            language = "ig",
            mode = "casual",
            conversationId = "conv_igbo_01"
        )

        val responseIgbo = service.sendChat(
            request = requestIgbo,
            conversationHistory = emptyList(),
            relevantDocuments = emptyList()
        )

        assertNotNull(responseIgbo.response)
        assertTrue(responseIgbo.response.contains("Asụsụ Igbo support is currently in fine-tuning"))
        assertEquals("ig", responseIgbo.language)
        assertEquals("conv_igbo_01", responseIgbo.conversationId)
        assertTrue(responseIgbo.modelUsed.contains("Preview"))

        // Verify that Gemini network call was NOT made for unsupported language
        assertNull(fakeApi.lastRequestReceived)
    }

    @Test
    fun testQwenSandboxModelSelection() = runTest {
        val fakeApi = FakeGeminiApiService()
        val service = SabiAiBackendServiceImpl(geminiService = fakeApi)

        val request = SabiChatRequest(
            message = "Explain what POS agent means in Nigeria",
            language = "pcm",
            mode = "casual",
            conversationId = "conv_qwen_01"
        )

        val response = service.sendChat(
            request = request,
            conversationHistory = emptyList(),
            relevantDocuments = emptyList(),
            activeModelId = "sabi_qwen"
        )

        assertNotNull(response.response)
        assertTrue(response.response.contains("[SABI Fine-Tuned Qwen-7B Nigerian Layer - Sandbox Mode]"))
        assertEquals("SABI-Qwen-7B-Nigerian-v0.4", response.modelUsed)
        assertTrue(response.culturalNote.contains("Sandbox"))
    }

    @Test
    fun testOfflineFallbackContextEngineWhenNoApiKey() = runTest {
        val fakeApi = FakeGeminiApiService()
        val service = SabiAiBackendServiceImpl(geminiService = fakeApi)

        val request = SabiChatRequest(
            message = "How to make party jollof rice?",
            language = "pcm",
            mode = "casual",
            conversationId = "conv_fallback_01"
        )

        val history = listOf(
            MessageEntity(
                id = "m1",
                conversationId = "conv_fallback_01",
                role = "user",
                content = "Hello SABI",
                timestamp = 1000L,
                language = "pcm"
            )
        )

        val docs = listOf(
            DocumentEntity(
                id = "doc1",
                title = "Jollof Recipe",
                content = "Smoke flavor from firewood is essential.",
                category = "culinary",
                language = "en",
                source = "Sabi Knowledge"
            )
        )

        val response = service.sendChat(
            request = request,
            conversationHistory = history,
            relevantDocuments = docs
        )

        assertNotNull(response.response)
        assertTrue(response.response.isNotBlank())
        assertEquals("conv_fallback_01", response.conversationId)
        assertEquals("pcm", response.language)
    }

    @Test
    fun testNetworkFailureTriggersResilientFallback() = runTest {
        val failingApi = FakeGeminiApiService(shouldThrow = true)
        val service = SabiAiBackendServiceImpl(geminiService = failingApi)

        val request = SabiChatRequest(
            message = "Wetin be inflation rate in Nigeria?",
            language = "pcm",
            mode = "business",
            conversationId = "conv_error_resilience"
        )

        // Must not throw an unhandled exception, but return resilient fallback
        val response = service.sendChat(
            request = request,
            conversationHistory = emptyList(),
            relevantDocuments = emptyList()
        )

        assertNotNull(response)
        assertTrue(response.response.isNotBlank())
        assertEquals("conv_error_resilience", response.conversationId)
    }

    @Test
    fun testFirestoreAggregatedMetricsModelIntegrity() = runTest {
        val metrics = FirestoreAggregatedMetrics(
            activeUsersDaily = 920,
            activeUsersMonthly = 3680,
            totalRegisteredUsers = 1500,
            totalConversations = 1350,
            totalMessageVolume = 20500,
            messagesLast24h = 1600,
            voiceTranscriptionQueries = 4200,
            textQueries = 16300,
            satisfactionScore = 96,
            isLiveFirestoreConnected = true,
            source = "Google Cloud Firestore"
        )

        assertEquals(920, metrics.activeUsersDaily)
        assertEquals(3680, metrics.activeUsersMonthly)
        assertEquals(20500, metrics.totalMessageVolume)
        assertEquals(1600, metrics.messagesLast24h)
        assertEquals(96, metrics.satisfactionScore)
        assertTrue(metrics.isLiveFirestoreConnected)
        assertEquals("Google Cloud Firestore", metrics.source)
    }

    @Test
    fun testFirestoreServiceOfflineFallbackMetrics() = runTest {
        val service = SabiFirestoreService()
        val metrics = service.fetchAggregatedMetrics(localConvCount = 15, localMsgCount = 60)

        assertNotNull(metrics)
        assertTrue(metrics.totalConversations >= 15)
        assertTrue(metrics.totalMessageVolume >= 60)
        assertTrue(metrics.activeUsersDaily > 0)
        assertTrue(metrics.satisfactionScore in 80..100)
    }
}
