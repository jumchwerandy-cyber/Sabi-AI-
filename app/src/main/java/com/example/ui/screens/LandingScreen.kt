package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onStartChatting: () -> Unit,
    onExploreFeature: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .testTag("landing_screen_column"),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // Generated Hero Visual
                Image(
                    painter = painterResource(id = R.drawable.img_sabi_hero),
                    contentDescription = "SABI AI Neural Context Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for modern contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SabiNavyDark.copy(alpha = 0.85f),
                                    SabiNavyDark
                                )
                            )
                        )
                )

                // Hero Badges
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 20.dp, top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SabiNavyDark.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🇳🇬", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SABI AI",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SabiGreenGlow
                            )
                        }
                    }
                }
            }
        }

        // Hero Titles & CTAs
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp)
            ) {
                Text(
                    text = "Meet SABI AI",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = SabiTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "The AI That Understands You.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SabiGreenGlow
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "An AI assistant built to understand Nigerian English, Pidgin, culture and everyday conversations.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SabiTextSecondary,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Built for Nigerian conversations. Designed for African context.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SabiGold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // CTA Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onStartChatting,
                        colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("start_chatting_button")
                    ) {
                        Icon(
                            Icons.Default.ChatBubble,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start Chatting",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { onExploreFeature("explore") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SabiTextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("explore_sabi_button")
                    ) {
                        Text(
                            text = "Explore SABI AI",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Quick Nigerian Question Starter Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "TRY ASKING SABI",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SabiTextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                val samplePrompts = listOf(
                    "Abeg explain this to me.",
                    "Help me write a professional message.",
                    "Translate this to Igbo.",
                    "Explain this assignment simply.",
                    "Make this sound more Nigerian."
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    samplePrompts.forEach { prompt ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onExploreFeature(prompt) }
                                .testTag("sample_prompt_${prompt.take(6).lowercase()}"),
                            color = SabiNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = SabiGreenGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SabiTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Feature Section Title
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "CORE CAPABILITIES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SabiGreenGlow
                )
                Text(
                    text = "Designed for Everyday African Life",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SabiTextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Feature Cards Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    emoji = "🗣",
                    title = "Nigerian Pidgin",
                    description = "Understand natural Nigerian Pidgin and conversational expressions without awkward or forced translations.",
                    tag = "Fully Active"
                )
                FeatureCard(
                    emoji = "🇳🇬",
                    title = "Nigerian Context",
                    description = "Understand Nigerian situations, cultural expressions, societal etiquette, and localized day-to-day realities.",
                    tag = "Native Engine"
                )
                FeatureCard(
                    emoji = "🌍",
                    title = "Languages",
                    description = "Support English, Pidgin, and progressively add major Nigerian languages like Igbo, Yoruba, and Hausa.",
                    tag = "Roadmap V2"
                )
                FeatureCard(
                    emoji = "✍️",
                    title = "Writing Assistant",
                    description = "Write, rewrite, summarize, and improve messages. Convert street Pidgin into high-level corporate correspondence.",
                    tag = "Writing Tool"
                )
                FeatureCard(
                    emoji = "🎓",
                    title = "Student Assistant",
                    description = "Help students understand academic concepts, assignments, JAMB/WAEC curriculum, and thesis writing simply.",
                    tag = "Academics"
                )
                FeatureCard(
                    emoji = "💼",
                    title = "Business Assistant",
                    description = "Help with MSME ideas, customer communication, POS agency setups, Nigerian tenders, and market negotiations.",
                    tag = "Commerce"
                )
            }
        }

        // Future Roadmap Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = SabiGold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "SABI AI Future Roadmap",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SabiTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    RoadmapItem(
                        version = "V2",
                        title = "Nigerian Languages & Voice Input",
                        desc = "Direct translation for Igbo, Yoruba, Hausa, speech-to-text with Nigerian accent tuning, and document uploads."
                    )
                    RoadmapItem(
                        version = "V3",
                        title = "Custom SABI Fine-Tuned Model",
                        desc = "Open-weights foundation (Qwen) with dedicated Nigerian cultural fine-tuning weights and offline mobile deployment."
                    )
                    RoadmapItem(
                        version = "V4",
                        title = "Pan-African & Enterprise AI",
                        desc = "Multi-African language support, enterprise developer API, and specialized education AI tutors."
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    emoji: String,
    title: String,
    description: String,
    tag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SabiGreenContainer
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = SabiGreenGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = SabiTextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun RoadmapItem(version: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = SabiNavyElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
        ) {
            Text(
                text = version,
                color = SabiGreenGlow,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = SabiTextPrimary)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = SabiTextSecondary)
        }
    }
}
