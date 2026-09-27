package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DocumentEntity
import com.example.ui.theme.*

@Composable
fun ContextEngineScreen(
    searchQuery: String,
    documents: List<DocumentEntity>,
    onSearchChange: (String) -> Unit,
    onAskSabi: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showArchitectureTab by remember { mutableStateOf(false) }

    val categories = listOf("All", "Local Expressions", "Business & Trade", "Culture", "Education", "Everyday Situations")

    val filteredDocs = documents.filter { doc ->
        (selectedCategory == "All" || doc.category.equals(selectedCategory, ignoreCase = true)) &&
                (searchQuery.isBlank() || doc.title.contains(searchQuery, ignoreCase = true) ||
                        doc.content.contains(searchQuery, ignoreCase = true) ||
                        doc.keywords.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .padding(16.dp)
            .testTag("context_engine_column"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Nigerian Context Engine",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SabiTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🇳🇬", fontSize = 20.sp)
                    }
                    Text(
                        text = "RAG Knowledge Base & Cultural Nuance Architecture",
                        style = MaterialTheme.typography.bodySmall,
                        color = SabiTextSecondary
                    )
                }
            }
        }

        // Mode switch: Knowledge Base vs Technical Architecture
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !showArchitectureTab,
                    onClick = { showArchitectureTab = false },
                    label = { Text("Knowledge Base") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SabiGreenContainer,
                        selectedLabelColor = SabiGreenGlow,
                        containerColor = SabiNavySurface,
                        labelColor = SabiTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_knowledge_base")
                )
                FilterChip(
                    selected = showArchitectureTab,
                    onClick = { showArchitectureTab = true },
                    label = { Text("AI / RAG Architecture") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SabiGreenContainer,
                        selectedLabelColor = SabiGreenGlow,
                        containerColor = SabiNavySurface,
                        labelColor = SabiTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_architecture")
                )
            }
        }

        if (showArchitectureTab) {
            // Architecture Diagram & Explanation
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Internal Context & RAG Architecture",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SabiGreenGlow
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "SABI AI isolates the frontend from the underlying model provider via a unified `/api/chat` contract. This enables non-disruptive switching between Base LLMs (Gemini), Nigerian Vector RAG, and our custom open-weights model.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SabiTextPrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val archLayers = listOf(
                            Pair("Layer 1: Input & Code-Switching", "Detects Nigerian English, Pidgin, and mixes. Normalizes vernacular slang without stripping context."),
                            Pair("Layer 2: RAG Vector Knowledge Base", "Queries Nigerian database covering CAC procedures, NYSC protocols, market dynamics, and regional proverbs."),
                            Pair("Layer 3: Model Execution Layer", "Seamless abstraction: routes to Gemini 3.5 Flash or SABI Qwen Fine-Tuned Nigerian weights."),
                            Pair("Layer 4: Tone & Cultural Filter", "Ensures answers avoid caricatured slang while honoring authentic Nigerian nuances.")
                        )

                        archLayers.forEachIndexed { index, (layer, desc) ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                color = SabiNavyElevated
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = layer, fontWeight = FontWeight.Bold, color = SabiGreenGlow, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = desc, color = SabiTextSecondary, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search slang, institutions, culture, exams...", color = SabiTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SabiTextSecondary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("context_engine_search_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SabiGreenPrimary,
                        unfocusedBorderColor = SabiNavyBorder,
                        focusedTextColor = SabiTextPrimary,
                        unfocusedTextColor = SabiTextPrimary
                    )
                )
            }

            // Categories Row
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = cat }
                                .testTag("cat_chip_${cat.take(4).lowercase()}"),
                            color = if (isSelected) SabiGreenContainer else SabiNavySurface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SabiGreenPrimary else SabiNavyBorder
                            )
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SabiGreenGlow else SabiTextSecondary
                            )
                        }
                    }
                }
            }

            // Document Cards
            if (filteredDocs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching knowledge entries found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SabiTextMuted
                        )
                    }
                }
            } else {
                items(filteredDocs, key = { it.id }) { doc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SabiNavyElevated
                                ) {
                                    Text(
                                        text = doc.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SabiGreenGlow,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                TextButton(
                                    onClick = { onAskSabi("Explain ${doc.title} with Nigerian context") },
                                    modifier = Modifier.testTag("ask_sabi_${doc.id}")
                                ) {
                                    Icon(Icons.Default.ChatBubble, contentDescription = null, tint = SabiGreenGlow, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ask in Chat", color = SabiGreenGlow, style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = doc.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SabiTextPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = doc.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = SabiTextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
