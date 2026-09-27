package com.example.data.context

import com.example.data.model.ConversationMode
import com.example.data.model.SabiLanguage

object NigerianContextEngine {

    const val MODEL_NAME = "gemini-3.5-flash"
    const val SABI_VERSION = "SABI AI Engine v1.2 (Nigerian Context Enabled)"

    /**
     * Builds an authentic system instruction tailored for Nigerian context,
     * the requested response mode, and language.
     */
    fun buildSystemInstruction(language: SabiLanguage, mode: ConversationMode): String {
        return buildString {
            appendLine("You are SABI AI 🇳🇬, 'The AI That Understands You'.")
            appendLine("You are an intelligent, modern, African/Nigerian-focused AI assistant built for Nigerian English, Nigerian Pidgin, Nigerian culture, and everyday conversations.")
            appendLine()
            appendLine("CRITICAL PRODUCT PRINCIPLES:")
            appendLine("1. Natural adaptation: Do NOT force Nigerian slang or Pidgin into every response. If a user asks formally or in standard English, respond in clear, polished English.")
            appendLine("2. If the user speaks in Nigerian Pidgin or requests Pidgin mode, respond in authentic, fluent, natural Nigerian Pidgin without sounding caricatured or exaggerated.")
            appendLine("3. If the user mixes English and Pidgin (code-switching), understand it naturally and respond according to the selected mode.")
            appendLine("4. Nigerian Context Awareness: Understand local situations, such as traffic in major cities (Lagos, Port Harcourt, Abuja), NEPA/DisCos, NYSC, JAMB/WAEC, market trade bargaining, POS agency business, Nigerian civil service, landlords, banking apps, and everyday realities.")
            appendLine("5. For unsupported Nigerian languages (such as Igbo, Yoruba, Hausa): Kindly acknowledge that full model generation for these languages is coming soon in SABI V2/V3, but provide helpful translations or cultural phrases if asked.")
            appendLine()
            appendLine("CURRENT USER SETTINGS:")
            appendLine("- Selected Language: ${language.displayName} (${language.nativeName})")
            appendLine("- Selected Mode: ${mode.title} - ${mode.description}")
            appendLine("- Mode Instruction: ${mode.systemPromptDirective}")
        }
    }

    /**
     * Offline or fallback contextual engine to provide realistic, intelligent Nigerian responses
     * when the device is offline or API credentials are not yet entered.
     */
    fun generateOfflineFallbackResponse(
        prompt: String,
        language: SabiLanguage,
        mode: ConversationMode
    ): String {
        val lower = prompt.lowercase().trim()

        if (language == SabiLanguage.IGBO || lower.contains("igbo")) {
            return "Nnoo! (Welcome). SABI AI's full Igbo model fine-tuning is currently in active development for our V2 roadmap. However, in Igbo context: 'Daalu' means Thank you, 'Kedu ka ime?' means How are you, and 'Nnoo' means Welcome. Stay tuned as we roll out full Igbo conversational support!"
        }
        if (language == SabiLanguage.YORUBA || lower.contains("yoruba")) {
            return "Ẹ ku ikalẹ! Full conversational Yoruba support is coming in SABI AI V2. In the meantime: 'Ẹ n lẹ' means Hello/Welcome, 'Bawo ni?' means How is it going?, and 'E se' means Thank you. We are expanding our Nigerian language models step by step!"
        }
        if (language == SabiLanguage.HAUSA || lower.contains("hausa")) {
            return "Sannu! SABI AI's Hausa language model is currently being fine-tuned for our upcoming release. In Hausa: 'Sannu da zuwa' means Welcome, 'Yaya kake?' means How are you?, and 'Nagode' means Thank you. Full Hausa conversations are coming soon!"
        }

        // Check for specific Knowledge Base topic matches
        when {
            lower.contains("amalgamation") || lower.contains("1914") || lower.contains("independence") || lower.contains("lugard") -> {
                return "In Nigerian history, 1914 marks the amalgamation of the Northern and Southern British Protectorates into the Colony and Protectorate of Nigeria by Lord Frederick Lugard. This laid the foundation for modern Nigeria, culminating in full sovereign independence on October 1, 1960, championed by pioneers like Dr. Nnamdi Azikiwe, Chief Obafemi Awolowo, and Sir Ahmadu Bello."
            }
            lower.contains("lokoja") || lower.contains("confluence") || lower.contains("river niger") -> {
                return "The famous Niger-Benue Confluence is located in Lokoja, Kogi State! This is where the River Niger (flowing from Guinea) and the River Benue meet in a majestic Y-shape before flowing southwards into the Atlantic Ocean through the Niger Delta."
            }
            lower.contains("nysc") || lower.contains("camp") || lower.contains("allawee") || lower.contains("corps") -> {
                return "For the National Youth Service Corps (NYSC):\n1. **Orientation Camp**: 21 days of regimented orientation. Compulsory gear includes plain white t-shirts, white shorts, white canvas shoes, call-up letter, green card, and medical certificate.\n2. **Primary Place of Assignment (PPA)**: Core posting for service (schools, public ministries, or private firms).\n3. **Community Development Service (CDS)**: Weekly civic community engagement projects.\n4. **Monthly Allawee**: Paid directly by the federal government."
            }
            lower.contains("landlord") || lower.contains("rent") || lower.contains("notice to quit") || lower.contains("eviction") -> {
                return "Under Nigerian Tenancy Law (such as the Lagos State Tenancy Law):\n• A yearly tenant is legally entitled to a 6-month written Notice to Quit.\n• A half-yearly tenant gets 3 months, and a monthly tenant gets 1 month.\n• Self-help eviction (removing roofs, locking gates, cutting off electricity) is strictly illegal—a landlord must obtain a court warrant of possession.\n• Always keep written records of rent receipts and formal communications."
            }
            lower.contains("jollof") || lower.contains("rice") || lower.contains("party rice") -> {
                return "The secret to authentic Nigerian Jollof rice lies in the base and the smoke! It is cooked with long-grain parboiled rice in a rich stew of blended plum tomatoes, tatashe (red bell pepper), ata rodo (scotch bonnet), and onions. The signature smoky 'party flavor' comes from letting the bottom layer caramelize gently under tight foil during steaming."
            }
            lower.contains("pos") || lower.contains("agency banking") -> {
                return "To run a profitable POS agency business in Nigeria:\n1. Partner with top aggregators (Moniepoint, OPay, Palmpay).\n2. Location is critical: High-density spots near campuses, busy bus stops, or underserved bank corridors.\n3. Maintain a daily cash float of ₦150k - ₦300k.\n4. Guard your terminal PINs, beware of fake SMS transfer alerts, and register your business name with the Corporate Affairs Commission (CAC)."
            }
            lower.contains("slang") || lower.contains("sapa") || lower.contains("japa") || lower.contains("dey play") || lower.contains("wahala") -> {
                return "Here are key Nigerian slang decoders:\n• **Japa**: Emigrating or relocating for greener pastures.\n• **Sapa**: Extreme financial broke-ness.\n• **Wahala**: Trouble, complication, or stress.\n• **Dey play**: Don't joke with serious matters.\n• **E choke**: Overwhelming excitement or astonishment.\n• **No shaking**: Everything is under control."
            }
        }

        // Mode specific replies
        return when (mode) {
            ConversationMode.PIDGIN -> {
                when {
                    lower.contains("explain") || lower.contains("abeg") ->
                        "I dey with you 100%! Wetin you dey ask about: \"$prompt\", na something wey dey very straightforward once we break am down step by step. Make you relax, SABI dey here to help you get am sharp without any stress. Wetin be the next question wey dey your mind?"
                    lower.contains("write") || lower.contains("message") || lower.contains("letter") ->
                        "No wahala at all! Here be sharp way you fit put am:\n\n\"Good day sir/ma, I hope this message finds you well. I am writing to respectfully bring this matter to your attention...\"\n\nIf you want make I adjust am make e sound more formal or more casual, just tell me!"
                    lower.contains("pos") || lower.contains("business") ->
                        "To run sharp business for Nigeria today, location and steady float na number one. Make sure you get reliable network (like Moniepoint or OPay terminal), keep cash secure, and always balance your daily book. Anything else you wan make I explain?"
                    else ->
                        "I hear you loud and clear! On this matter: \"$prompt\"—no shaking at all. SABI understand the gist. Tell me where you wan make we start, and I go break am down for you sharp sharp."
                }
            }
            ConversationMode.PROFESSIONAL -> {
                "Thank you for your inquiry. In relation to \"$prompt\", the most effective approach within the Nigerian corporate environment is to maintain clear documentation, professional deference, and structured objectives.\n\nRecommended actions:\n1. Clearly state the project scope and expected outcomes.\n2. Ensure alignment with regulatory and commercial requirements (e.g., CAC, FIRS, or relevant trade protocols).\n3. Maintain courteous and prompt follow-up.\n\nPlease let me know if you would like me to draft a formal proposal or refine this further."
            }
            ConversationMode.ACADEMIC -> {
                "Let us examine \"$prompt\" systematically from an educational perspective:\n\n1. **Core Concept**: Understanding the fundamental definition and underlying mechanics.\n2. **Nigerian Curriculum Alignment**: For exams such as WAEC, NECO, or JAMB UTME, examiners look for concise definitions, relevant formulas, and standard terminology.\n3. **Practical Example**: Relating the theoretical principle to real-world observations.\n\nWould you like me to generate practice questions or a simplified summary on this topic?"
            }
            ConversationMode.BUSINESS -> {
                "From a commercial and Nigerian market standpoint regarding \"$prompt\":\n\n• **Market Dynamics**: Address supply chain stability, payment settlement (instant bank transfer/POS reconciliation), and customer trust.\n• **Execution Strategy**: Keep overhead costs lean during initial rollout, and prioritize steady cash flow over speculative expansion.\n• **Actionable Step**: Define your primary customer demographic in the target state or commercial hub (e.g., Lagos, Abuja, Kano, Port Harcourt)."
            }
            ConversationMode.SIMPLE -> {
                "Here is the simple explanation of \"$prompt\" without any complicated words:\n\nThink of it like cooking a good pot of Jollof rice: you need the right ingredients, proper timing, and patience. In the same way, this concept works step by step:\n• First, start with the basics.\n• Second, connect each part together.\n• Finally, you get the complete result.\n\nDoes this make it easy to understand?"
            }
            ConversationMode.CASUAL -> {
                "I completely get where you're coming from! Regarding \"$prompt\", it's quite simple when you look at how things typically work here in Nigeria. The key is just having the right information and taking it one step at a time.\n\nFeel free to tell me more details so we can dive into it together!"
            }
        }
    }
}
