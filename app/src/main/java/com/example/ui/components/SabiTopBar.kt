package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.ConversationMode
import com.example.data.model.SabiLanguage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SabiTopBar(
    title: String,
    selectedLanguage: SabiLanguage,
    selectedMode: ConversationMode,
    activeModelId: String,
    onMenuClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onModeClick: () -> Unit,
    onModelChange: (String) -> Unit
) {
    var showModelMenu by remember { mutableStateOf(false) }

    Surface(
        color = SabiNavyDark,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("topbar_menu_button")
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Open Menu",
                            tint = SabiTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SabiTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🇳🇬", fontSize = 16.sp)
                        }
                    }
                }

                // Top Actions: Language Chip & Mode Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language Chip
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onLanguageClick)
                            .testTag("topbar_language_chip"),
                        color = SabiNavyElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedLanguage.displayName.take(7),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SabiGreenGlow
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = SabiGreenGlow,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Mode Chip
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onModeClick)
                            .testTag("topbar_mode_chip"),
                        color = SabiNavyElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedMode.iconEmoji} ${selectedMode.title}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SabiTextPrimary
                            )
                        }
                    }

                    // Model Selector Dropdown
                    Box {
                        IconButton(
                            onClick = { showModelMenu = true },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("topbar_model_menu_button")
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = "Model Settings",
                                tint = if (activeModelId == "sabi_qwen") SabiGold else SabiGreenGlow
                            )
                        }

                        DropdownMenu(
                            expanded = showModelMenu,
                            onDismissRequest = { showModelMenu = false },
                            modifier = Modifier.background(SabiNavyElevated)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("SABI Engine v1 (Gemini 3.5 Flash)", fontWeight = FontWeight.Bold, color = SabiTextPrimary)
                                        Text("Active Production Model", style = MaterialTheme.typography.labelSmall, color = SabiGreenGlow)
                                    }
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = SabiGreenGlow)
                                },
                                onClick = {
                                    onModelChange("sabi_v1")
                                    showModelMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("SABI Fine-Tuned (Qwen-7B Nigerian)", fontWeight = FontWeight.Bold, color = SabiTextPrimary)
                                        Text("Open-Weights Sandbox Preview", style = MaterialTheme.typography.labelSmall, color = SabiGold)
                                    }
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Science, contentDescription = null, tint = SabiGold)
                                },
                                onClick = {
                                    onModelChange("sabi_qwen")
                                    showModelMenu = false
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = SabiNavyBorder, thickness = 1.dp)
        }
    }
}
