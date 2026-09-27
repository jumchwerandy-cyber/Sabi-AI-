package com.example.data.context

import com.example.data.local.DocumentEntity

object NigerianKnowledgeData {

    val allDocuments: List<DocumentEntity> = listOf(
        // ==================== NIGERIAN HISTORY ====================
        DocumentEntity(
            id = "hist_01",
            title = "Pre-Colonial Kingdoms & Empires",
            category = "History",
            content = "Nigeria's pre-colonial era featured world-renowned civilizations:\n" +
                    "• Benin Kingdom: Famous for legendary bronze and brass casting, intricate urban planning with city moats (Iya), and the supreme authority of the Oba of Benin.\n" +
                    "• Oyo Empire: Dominant Yoruba empire with advanced constitutional checks and balances (Alaafin, Oyo Mesi council, and Bashorun) alongside an elite cavalry army.\n" +
                    "• Kanem-Bornu & Hausa States: Major centers of trans-Saharan trade and Islamic scholarship (Kano, Zaria, Gobir, Katsina).\n" +
                    "• Kingdom of Nri & Igbo City-States: Republican democratic governance, age-grade systems, and Igbo-Ukwu bronze archaeology dating to the 9th century.\n" +
                    "• Sokoto Caliphate: Established by Usman dan Fodio in 1804, uniting northern Emirates under structured legal and educational administration.",
            keywords = "benin bronze oyo alaafin sokoto caliphate nri kano empire pre-colonial history civilization"
        ),
        DocumentEntity(
            id = "hist_02",
            title = "The 1914 Amalgamation & Journey to Independence (1960)",
            category = "History",
            content = "Key milestones of modern Nigerian nationhood:\n" +
                    "• 1914: Lord Frederick Lugard amalgamated the Northern and Southern British Protectorates into the Colony and Protectorate of Nigeria.\n" +
                    "• Nationalist Movement: Championed by key founding fathers including Dr. Nnamdi Azikiwe (Zik of Africa), Chief Obafemi Awolowo, Sir Ahmadu Bello (Sardauna of Sokoto), and Sir Abubakar Tafawa Balewa.\n" +
                    "• October 1, 1960: Nigeria achieved full sovereign independence from Great Britain.\n" +
                    "• October 1, 1963: Nigeria became a Federal Republic, severing colonial ties to the British monarchy.",
            keywords = "amalgamation 1914 lugard independence 1960 nnamdi azikiwe awolowo ahmadu bello tafawa balewa history"
        ),
        DocumentEntity(
            id = "hist_03",
            title = "The Nigerian Civil War (1967–1970) & Post-War Rebuilding",
            category = "History",
            content = "The Nigerian Civil War (Biafran War):\n" +
                    "• Root Causes: 1966 military coups, ethnic tensions, and the declaration of the Republic of Biafra by Lt. Col. Chukwuemeka Odumegwu Ojukwu.\n" +
                    "• Conclusion: January 15, 1970 surrender led by Maj. Gen. Philip Effiong.\n" +
                    "• The 3Rs Policy: Head of State Gen. Yakubu Gowon declared 'No victor, no vanquished' and introduced the policy of Reconciliation, Rehabilitation, and Reconstruction, including establishing the NYSC scheme in 1973 to foster inter-ethnic unity.",
            keywords = "civil war biafra ojukwu gowon 1967 1970 nysc 3rs history"
        ),

        // ==================== NIGERIAN GEOGRAPHY ====================
        DocumentEntity(
            id = "geo_01",
            title = "Six Geopolitical Zones & 36 States",
            category = "Geography",
            content = "Nigeria is organized into 36 states and the Federal Capital Territory (FCT) Abuja across 6 geopolitical zones:\n" +
                    "• North Central (Middle Belt): Benue, Kogi, Kwara, Nasarawa, Niger, Plateau, FCT Abuja.\n" +
                    "• North East: Adamawa, Bauchi, Borno, Gombe, Taraba, Yobe.\n" +
                    "• North West: Jigawa, Kaduna, Kano, Katsina, Kebbi, Sokoto, Zamfara.\n" +
                    "• South East: Abia, Anambra, Ebonyi, Enugu, Imo.\n" +
                    "• South South (Niger Delta): Akwa Ibom, Bayelsa, Cross River, Delta, Edo, Rivers.\n" +
                    "• South West: Ekiti, Lagos, Ogun, Ondo, Osun, Oyo.",
            keywords = "geopolitical zones states abuja north south east west geography map regions"
        ),
        DocumentEntity(
            id = "geo_02",
            title = "Major Physical Features & River Confluence",
            category = "Geography",
            content = "Notable topography and water bodies:\n" +
                    "• The Confluence (Lokoja): River Niger (originating in Guinea) and River Benue meet in Lokoja, Kogi State, forming a Y-shape that flows south into the Atlantic.\n" +
                    "• Niger Delta: One of the largest river deltas and mangrove ecosystems in the world, vital for petroleum resources.\n" +
                    "• Jos Plateau: High-altitude plateau with a temperate climate, rocky terrain, and unique micro-climate.\n" +
                    "• Ecological Belts: Transitions from humid coastal mangroves in the south to tropical rainforest, Guinea savannah, Sudan savannah, and Sahel in the far north.",
            keywords = "river niger benue confluence lokoja delta jos plateau geography terrain climate"
        ),
        DocumentEntity(
            id = "geo_03",
            title = "Major Metropolises & Economic Gateways",
            category = "Geography",
            content = "Economic and cultural capitals:\n" +
                    "• Lagos: Mega-city and financial nerve center of West Africa with major seaports (Apapa, Tin Can) and Lekki Deep Sea Port.\n" +
                    "• Abuja: Purpose-built federal capital known for Aso Rock, Zuma Rock, and national administrative institutions.\n" +
                    "• Kano: Ancient trans-Saharan commercial hub with famous Kurmi market and Dawanau grain market.\n" +
                    "• Port Harcourt (Pitakwa): Heart of the oil industry and maritime logistics.\n" +
                    "• Onitsha: Home to Onitsha Main Market, the largest open-air wholesale market in West Africa on the banks of the River Niger.",
            keywords = "lagos abuja kano port harcourt onitsha cities population commerce geography"
        ),

        // ==================== CULTURE & TRADITIONS ====================
        DocumentEntity(
            id = "cult_01",
            title = "Ethnic Diversity, Languages & Pidgin English",
            category = "Culture",
            content = "Cultural tapestry of Nigeria:\n" +
                    "• More than 250 distinct ethnic nationalities and 500 indigenous languages.\n" +
                    "• Three major languages: Hausa (Afroasiatic), Yoruba (Niger-Congo), and Igbo (Niger-Congo).\n" +
                    "• Nigerian Pidgin (Naija): An English-based creole serving as the premier national lingua franca, bridging ethnic, religious, and class lines with rich idioms, proverbs, and humour.\n" +
                    "• Code-Switching: Educated Nigerians fluidly alternate between formal Queen's English, indigenous dialects, and Pidgin depending on social and business contexts.",
            keywords = "ethnicity hausa yoruba igbo pidgin languages lingua franca culture code-switching"
        ),
        DocumentEntity(
            id = "cult_02",
            title = "Cultural Festivals & Heritage Celebrations",
            category = "Culture",
            content = "Iconic Nigerian festivals:\n" +
                    "• Argungu Fishing Festival (Kebbi): Annual competition where thousands of fishermen dive into the Matan Fada river using traditional nets.\n" +
                    "• Osun-Osogbo Festival (Osun): Sacred UNESCO World Heritage celebration in the sacred groves of Osogbo honoring the water goddess Osun.\n" +
                    "• Durbar Festival: Spectacular equestrian parade celebrated at Eid in Kano, Katsina, Zaria, and Bida, displaying royal cavalry and turbans.\n" +
                    "• New Yam Festival (Iri Ji / Ogbalu): Celebrated across Igboland marking the harvest of yams with traditional dances and kola nut blessings.\n" +
                    "• Calabar Carnival: Dubbed 'Africa's Biggest Street Party', held throughout December in Cross River State.",
            keywords = "festivals argungu osun osogbo durbar new yam iri ji calabar carnival culture"
        ),
        DocumentEntity(
            id = "cult_03",
            title = "Culinary Heritage: Jollof, Soups & Street Food",
            category = "Culture",
            content = "Nigerian gastronomy:\n" +
                    "• Nigerian Jollof Rice: Long-grain parboiled rice cooked in a reduction of blended tomatoes, tatashe (red bell pepper), ata rodo (scotch bonnet), onions, bay leaves, and curry/thyme, known for its signature smoky party flavor.\n" +
                    "• Traditional Soups: Egusi (melon seed), Ogbono (draw soup), Banga (palm fruit), Afang, Edikang Ikong, and Oha soup paired with swallows like Pounded Yam, Eba, Amala, and Fufu.\n" +
                    "• Street Delicacies: Suya (spiced grilled beef skewers with yaji pepper and sliced onions), Kilishi (sun-dried spiced meat jerky), Akara (bean fritters), Bole (roasted plantain with spiced palm oil sauce and fish).",
            keywords = "jollof rice egusi pounded yam suya kilishi banga amala food cuisine cooking culture"
        ),
        DocumentEntity(
            id = "cult_04",
            title = "Interpersonal Etiquette, Seniority & Respect",
            category = "Culture",
            content = "Core etiquette principles in Nigeria:\n" +
                    "• Respect for Age: Greetings are mandatory. Younger people do not address elders by their first name; titles like 'Sir', 'Ma', 'Uncle', 'Aunty', 'Chief', or 'Alhaji' are used.\n" +
                    "• Greeting Gestures: In Yoruba culture, males prostrate (idobale) and females kneel (ikunle). In Hausa, a gentle bow with hand over chest is common. In Igbo, warm greetings accompanied by two-handed handshake.\n" +
                    "• Hand Taboo: Never hand items, money, or food to someone using the left hand; always use the right hand or both hands together as a sign of honour.",
            keywords = "respect etiquette elders greetings left hand customs manners culture"
        ),

        // ==================== BUSINESS & COMMERCE ====================
        DocumentEntity(
            id = "biz_01",
            title = "POS Agency Banking & Financial Inclusion",
            category = "Business",
            content = "Point of Sale (POS) retail banking agents:\n" +
                    "• Major Platforms: Moniepoint, OPay, Palmpay, Baxi, Firstmonie, Quickteller.\n" +
                    "• Unit Economics: Average transaction fee of ₦100 per ₦5,000 withdrawal or transfer.\n" +
                    "• Operating Requirements: Secure kiosk or store, high-density location (markets, transit junctions), steady cash float (₦150k - ₦500k), backup battery/power bank, thermal receipt printer.\n" +
                    "• Risk Management: Beware of fake bank transfer alerts, counterfeit currency, and keep terminal admin PINs private.",
            keywords = "pos agency banking moniepoint opay palmpay terminal agent cash float business"
        ),
        DocumentEntity(
            id = "biz_02",
            title = "Famous Commercial Markets & Trading Hubs",
            category = "Business",
            content = "Major Nigerian marketplace hubs:\n" +
                    "• Computer Village (Ikeja, Lagos): Africa's largest consumer electronics and hardware maintenance hub.\n" +
                    "• Alaba International Market (Ojo, Lagos): World center for imported electrical, electronics, and home appliances.\n" +
                    "• Balogun & Mandilas Market (Lagos Island): West African epicenter of textile, fabrics, footwear, and apparel.\n" +
                    "• Dawanau International Grains Market (Kano): West Africa's largest trading depot for grains (millet, sorghum, sesame, cowpeas).\n" +
                    "• Onitsha Main Market (Anambra): Colossal wholesale market for dry goods, pharmaceuticals, cosmetics, and household items.",
            keywords = "computer village alaba balogun onitsha dawanau markets trade retail business"
        ),
        DocumentEntity(
            id = "biz_03",
            title = "CAC Business Registration & Tech Startup Ecosystem",
            category = "Business",
            content = "Entrepreneurship and corporate compliance:\n" +
                    "• Corporate Affairs Commission (CAC): Business name registration or Private Limited Company (Ltd) registration via the Companies and Allied Matters Act (CAMA 2020).\n" +
                    "• Tax & Compliance: Tax Identification Number (TIN), FIRS VAT filing, SCUML certification for designated non-financial businesses.\n" +
                    "• Nigerian Tech Startup Ecosystem: Known as 'Silicon Lagoon' (Yaba, Lagos). Produced global FinTech leaders (Paystack acquired by Stripe, Flutterwave, Interswitch, Moniepoint, PiggyVest).",
            keywords = "cac business registration tax firs scuml fintech yaba paystack flutterwave startups"
        ),

        // ==================== EDUCATION & ACADEMICS ====================
        DocumentEntity(
            id = "edu_01",
            title = "JAMB UTME, WAEC & University Admissions",
            category = "Education",
            content = "Tertiary education pathways in Nigeria:\n" +
                    "• JAMB UTME: Unified Tertiary Matriculation Examination conducted as Computer-Based Test (CBT). Candidates answer 180 questions across 4 subject combinations (Use of English is compulsory).\n" +
                    "• WAEC & NECO: Senior School Certificate Examinations requiring a minimum of 5 credits (including English Language and Mathematics) in relevant subjects.\n" +
                    "• Post-UTME & CAPS: Central Admission Processing System monitors quotas (Merit: 45%, Catchment: 35%, ELDS: 20%).\n" +
                    "• NUC: National Universities Commission accredits academic programs across Federal, State, and Private universities.",
            keywords = "jamb utme waec neco university admission post-utme caps cbt nuc education"
        ),
        DocumentEntity(
            id = "edu_02",
            title = "NYSC Scheme: Camp, CDS & PPA Survival Guide",
            category = "Education",
            content = "National Youth Service Corps (NYSC) 1-year mandatory service for graduates under 30:\n" +
                    "• Orientation Camp: 21-day paramilitary regimentation in one of 37 state camps. Required kit: white t-shirts, white shorts, white tennis shoes, call-up letter, green card, and medical certificate.\n" +
                    "• Primary Place of Assignment (PPA): Corps members are posted to schools, healthcare centers, or public ministries for community service.\n" +
                    "• Community Development Service (CDS): Weekly civic projects and group engagements.\n" +
                    "• Monthly Allowance: Federal government 'allawee' paid directly to designated corps bank accounts.",
            keywords = "nysc allawee orientation camp ppa cds youth corps graduate service education"
        ),

        // ==================== EVERYDAY SITUATIONS ====================
        DocumentEntity(
            id = "sit_01",
            title = "Nigerian Landlord & Tenancy Legal Rights",
            category = "Everyday Situations",
            content = "Tenancy realities and rights in Nigeria:\n" +
                    "• Notice to Quit: By law (e.g., Lagos State Tenancy Law), a yearly tenant is entitled to 6 months written notice to quit; a half-yearly tenant gets 3 months; a monthly tenant gets 1 month.\n" +
                    "• 7-Day Owner's Intention: After the notice to quit expires, the landlord must serve a formal 7-Day Notice of Owner's Intention to Apply to Court for Possession.\n" +
                    "• Self-Help Eviction is Illegal: Landlords cannot remove roofs, lock gates, disconnect water/electricity, or eject tenants forcefully without a court warrant.\n" +
                    "• Caution Deposit: Tenants should inspect facilities upon move-in with photos to ensure caution deposit refunds.",
            keywords = "landlord tenant rent notice to quit lagos tenancy court agreement everyday"
        ),
        DocumentEntity(
            id = "sit_02",
            title = "NEPA / DisCos: Prepaid Meters & Band Tariffs",
            category = "Everyday Situations",
            content = "Electricity distribution in Nigeria:\n" +
                    "• DisCos (Distribution Companies): Regional providers such as EKEDC, IKEDC, AEDC, IBEDC, KEDCO, EEDC, PHED.\n" +
                    "• Service Reflective Tariff Bands: Band A (minimum 20 hours supply/day), Band B (16-20 hours), Band C (12-16 hours), Band D (8-12 hours), Band E (under 8 hours).\n" +
                    "• Token Generation: Prepaid meter tokens generated via banking apps, quickteller, or vendor agents. Includes 20-digit STS token. Key meter types: Conlog, Mojec, Hexing.\n" +
                    "• Tariff Disputes: Customers have the right to lodge complaints with NERC (Nigerian Electricity Regulatory Commission) Forum Offices if band supply hours are not met.",
            keywords = "nepa disco prepaid meter token band a tariff electricity power nerc everyday"
        ),
        DocumentEntity(
            id = "sit_03",
            title = "Commuting & Transit: Danfo, BRT & Interstate Travel",
            category = "Everyday Situations",
            content = "Transit navigation rules:\n" +
                    "• Lagos Yellow Danfo: Always carry smaller denominations (*change*) to avoid fights with the conductor. Listen for the bus conductor's route call (*'Obalende straight, enter with your change!'*).\n" +
                    "• BRT (Bus Rapid Transit): Uses contactless Cowry card ticketing across dedicated corridor lanes.\n" +
                    "• Interstate Travel: Major verified motor parks (God is Good Motors, Young Shall Grow, Peace Mass Transit, ABC Transport). Avoid picking unregistered roadside buses for safety.",
            keywords = "danfo brt cowry card lagos conductor transport travel interstate bus everyday"
        ),
        DocumentEntity(
            id = "sit_04",
            title = "Interbank Transfer Delays & USSD Dispute Resolution",
            category = "Everyday Situations",
            content = "Managing bank network delays in Nigeria:\n" +
                    "• NIBSS Instant Payment (NIP): Most transfers settle in seconds. If debited without credit to beneficiary, note the 30-digit Session ID from the debit receipt.\n" +
                    "• Automated Reversal Window: CBN regulation mandates automated reversal of failed transfers within 24 to 48 hours.\n" +
                    "• Resolution Steps: If unresolved, send an email to your bank's customer support quoting: Date, Amount, Sender Account, Recipient Account, and Session ID.\n" +
                    "• USSD Security: Never share your transaction PIN or OTP (One Time Password), and immediately dial your bank's USSD panic code if your mobile phone is misplaced.",
            keywords = "bank transfer nip delay session id cbn reversal ussd money atm everyday"
        ),
        DocumentEntity(
            id = "sit_05",
            title = "Market Bargaining Tactics (Price No Be Last Price)",
            category = "Everyday Situations",
            content = "Art of street and open-market bargaining:\n" +
                    "• The Anchor Rule: When a vendor quotes an initial price, politely counter at roughly 50% to 60% of the quote, laughing amiably to keep rapport.\n" +
                    "• Common Phrases: 'Customer, abeg look me with good eye', 'Wetin be your last price?', 'Price no be last price'.\n" +
                    "• The Slow Walk-Away: If the seller refuses to compromise, politely say thank you and take two slow steps away. In 70% of cases, the seller will call you back (*'Customer come carry am!'*).\n" +
                    "• Quantity Discount: Group items together to negotiate bulk discount allowances.",
            keywords = "bargaining market price negotiation lagos balogun shopping tactics everyday"
        ),

        // ==================== SLANG & EXPRESSIONS ====================
        DocumentEntity(
            id = "slang_01",
            title = "Comprehensive Nigerian Slang & Local Idioms",
            category = "Local Expressions",
            content = "Essential contemporary Nigerian expressions:\n" +
                    "• Japa: To emigrate or relocate abroad in search of greener pastures.\n" +
                    "• Sapa: Severe lack of money or financial broke-ness.\n" +
                    "• Wahala: Conflict, trouble, complication, or intense stress.\n" +
                    "• Dey Play: A witty admonition meaning 'keep wasting time' or 'do not take serious things lightly'.\n" +
                    "• E Choke: Exclamation of awe, shock, or overwhelming success (popularized by Davido).\n" +
                    "• No Shaking: Everything is in order; zero worries.\n" +
                    "• Comot: To step out, leave, or clear out from a location.\n" +
                    "• Na So: That is indeed the exact truth; an expression of full concurrence.\n" +
                    "• God Abeg: A heartfelt plea for divine grace or relief from life's hurdles.\n" +
                    "• Wotowoto: Relentless, continuous delivery of something (e.g., questions, food, or scolding).\n" +
                    "• Shege: Hardship, intense difficulty, or suffering seen through experience.\n" +
                    "• Chop Life: Enjoying life, spending comfortably, and indulging in leisure.",
            keywords = "japa sapa wahala dey play e choke comot na so god abeg wotowoto shege chop life slang"
        )
    )
}
