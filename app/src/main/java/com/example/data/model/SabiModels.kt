package com.example.data.model

enum class SabiLanguage(
    val id: String,
    val displayName: String,
    val nativeName: String,
    val isSupported: Boolean,
    val statusTag: String
) {
    ENGLISH("english", "English", "English", true, "Active"),
    PIDGIN("pidgin", "Nigerian Pidgin", "Naija Pidgin", true, "Active"),
    IGBO("igbo", "Igbo", "Asụsụ Igbo", false, "Coming Soon"),
    YORUBA("yoruba", "Yoruba", "Èdè Yorùbá", false, "Coming Soon"),
    HAUSA("hausa", "Hausa", "Harshen Hausa", false, "Coming Soon");

    companion object {
        fun fromId(id: String): SabiLanguage =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: ENGLISH
    }
}

enum class ConversationMode(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val systemPromptDirective: String
) {
    CASUAL(
        "casual",
        "Casual",
        "Natural everyday Nigerian conversation.",
        "☕",
        "Respond in a natural, everyday Nigerian conversational tone. Be warm, smart, and relatable without overusing slang."
    ),
    PROFESSIONAL(
        "professional",
        "Professional",
        "Formal and polished communication.",
        "💼",
        "Respond in a formal, polished, professional tone suitable for corporate, executive, or institutional communications in Nigeria and globally."
    ),
    ACADEMIC(
        "academic",
        "Academic",
        "Clear educational explanations.",
        "🎓",
        "Provide thorough, structured educational explanations with clarity, examples, and step-by-step breakdowns suitable for secondary and university students."
    ),
    BUSINESS(
        "business",
        "Business",
        "Professional business communication.",
        "📈",
        "Respond with sharp, practical business insight tailored to Nigerian and African markets, commerce, proposals, and customer relations."
    ),
    PIDGIN(
        "pidgin",
        "Pidgin",
        "Natural Nigerian Pidgin style.",
        "🗣",
        "Respond in authentic, natural Nigerian Pidgin. Keep it fluent, respectful, and clear, avoiding forced or exaggerated caricatures."
    ),
    SIMPLE(
        "simple",
        "Simple",
        "Explain complicated subjects in simple language.",
        "💡",
        "Explain complex subjects using clear, everyday language and relatable everyday Nigerian scenarios without overwhelming technical jargon."
    );

    companion object {
        fun fromId(id: String): ConversationMode =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: CASUAL
    }
}

enum class FeedbackCategory(val id: String, val label: String) {
    WRONG_EXPRESSION("wrong_expression", "Wrong Nigerian expression"),
    WRONG_LANGUAGE("wrong_language", "Wrong language"),
    WRONG_CONTEXT("wrong_context", "Wrong cultural context"),
    INCORRECT_INFO("incorrect_info", "Incorrect information"),
    POOR_RESPONSE("poor_response", "Poor response"),
    OTHER("other", "Other")
}

data class SabiChatRequest(
    val message: String,
    val language: String,
    val mode: String,
    val conversationId: String
)

data class SabiChatResponse(
    val response: String,
    val language: String,
    val conversationId: String,
    val modelUsed: String = "SABI Engine v1",
    val culturalNote: String? = null
)
