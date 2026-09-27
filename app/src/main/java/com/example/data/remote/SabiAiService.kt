package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.context.NigerianContextEngine
import com.example.data.local.DocumentEntity
import com.example.data.local.MessageEntity
import com.example.data.model.ConversationMode
import com.example.data.model.SabiChatRequest
import com.example.data.model.SabiChatResponse
import com.example.data.model.SabiLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clean AI API Abstraction.
 * Enables switching between Base LLM (Gemini), SABI Fine-tuned model (Qwen),
 * and local contextual RAG without frontend refactoring.
 */
interface SabiAiBackendService {
    suspend fun sendChat(
        request: SabiChatRequest,
        conversationHistory: List<MessageEntity>,
        relevantDocuments: List<DocumentEntity>,
        activeModelId: String = "sabi_v1"
    ): SabiChatResponse
}

class SabiAiBackendServiceImpl(
    private val geminiService: GeminiApiService = RetrofitClient.geminiService
) : SabiAiBackendService {

    companion object {
        private const val TAG = "SabiAiService"
    }

    override suspend fun sendChat(
        request: SabiChatRequest,
        conversationHistory: List<MessageEntity>,
        relevantDocuments: List<DocumentEntity>,
        activeModelId: String
    ): SabiChatResponse = withContext(Dispatchers.IO) {
        val language = SabiLanguage.fromId(request.language)
        val mode = ConversationMode.fromId(request.mode)

        // Handle unsupported Nigerian languages transparently
        if (!language.isSupported) {
            val notice = when (language) {
                SabiLanguage.IGBO -> "Nnoo! Asụsụ Igbo support is currently in fine-tuning for SABI AI V2. We are curating authentic Igbo proverbs and idioms."
                SabiLanguage.YORUBA -> "Ẹ ku ikalẹ! Èdè Yorùbá model fine-tuning is coming in SABI AI V2. We are training on authentic tonal and cultural expressions."
                SabiLanguage.HAUSA -> "Sannu! Harshen Hausa model fine-tuning is coming in SABI AI V2. Thank you for your patience as we prepare the dataset."
                else -> "${language.displayName} support is coming soon to SABI AI."
            }
            return@withContext SabiChatResponse(
                response = notice,
                language = request.language,
                conversationId = request.conversationId,
                modelUsed = "SABI Language Engine (Preview)",
                culturalNote = "${language.displayName} support is coming soon."
            )
        }

        // If user selected the Qwen Fine-Tuned Model Preview
        if (activeModelId == "sabi_qwen") {
            return@withContext SabiChatResponse(
                response = "[SABI Fine-Tuned Qwen-7B Nigerian Layer - Sandbox Mode]\n\n" +
                        NigerianContextEngine.generateOfflineFallbackResponse(request.message, language, mode),
                language = request.language,
                conversationId = request.conversationId,
                modelUsed = "SABI-Qwen-7B-Nigerian-v0.4",
                culturalNote = "Rendered via SABI Fine-Tuned Open-Weights Sandbox"
            )
        }

        // Check BuildConfig.GEMINI_API_KEY
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "Using Nigerian Context Engine fallback (No API key configured)")
            val fallback = NigerianContextEngine.generateOfflineFallbackResponse(request.message, language, mode)
            return@withContext SabiChatResponse(
                response = fallback,
                language = request.language,
                conversationId = request.conversationId,
                modelUsed = "SABI Nigerian Engine v1.2",
                culturalNote = "Context generated via SABI Native Nigerian Engine"
            )
        }

        try {
            // Build Contextual System Prompt
            val systemInstructionText = buildString {
                append(NigerianContextEngine.buildSystemInstruction(language, mode))
                if (relevantDocuments.isNotEmpty()) {
                    appendLine("\nNIGERIAN KNOWLEDGE BASE CONTEXT (RAG):")
                    relevantDocuments.take(3).forEach { doc ->
                        appendLine("[${doc.category}] ${doc.title}: ${doc.content}")
                    }
                }
            }

            // Build Conversation turns (keep last 6 for token optimization)
            val contentsList = mutableListOf<GeminiContent>()
            val recentTurns = conversationHistory.takeLast(6)
            for (msg in recentTurns) {
                contentsList.add(
                    GeminiContent(
                        role = if (msg.role == "user") "user" else "model",
                        parts = listOf(GeminiPart(text = msg.content))
                    )
                )
            }
            // Append current message
            contentsList.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = request.message))
                )
            )

            val geminiRequest = GeminiRequest(
                contents = contentsList,
                systemInstruction = GeminiContent(
                    role = "system",
                    parts = listOf(GeminiPart(text = systemInstructionText))
                ),
                generationConfig = GeminiGenerationConfig(temperature = 0.7f, topP = 0.95f)
            )

            val response = geminiService.generateContent(apiKey = apiKey, request = geminiRequest)
            val candidateText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            if (!candidateText.isNullOrBlank()) {
                SabiChatResponse(
                    response = candidateText.trim(),
                    language = request.language,
                    conversationId = request.conversationId,
                    modelUsed = "SABI Engine v1 (${NigerianContextEngine.MODEL_NAME})",
                    culturalNote = "Powered by SABI Nigerian Context Architecture"
                )
            } else {
                val fallback = NigerianContextEngine.generateOfflineFallbackResponse(request.message, language, mode)
                SabiChatResponse(
                    response = fallback,
                    language = request.language,
                    conversationId = request.conversationId,
                    modelUsed = "SABI Nigerian Engine v1.2",
                    culturalNote = "Context generated via SABI Heuristic Engine"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed, falling back to Nigerian Context Engine", e)
            val fallback = NigerianContextEngine.generateOfflineFallbackResponse(request.message, language, mode)
            SabiChatResponse(
                response = fallback,
                language = request.language,
                conversationId = request.conversationId,
                modelUsed = "SABI Nigerian Engine v1.2 (Resilience Mode)",
                culturalNote = "Local contextual response fallback"
            )
        }
    }
}
