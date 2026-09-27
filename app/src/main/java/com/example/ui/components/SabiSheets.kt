package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun LanguageSelectorSheet(
    selectedLanguage: SabiLanguage,
    onLanguageSelected: (SabiLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SabiNavyElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = SabiNavyBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .testTag("language_selector_sheet")
        ) {
            Text(
                text = "Select Conversation Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SabiTextPrimary
            )
            Text(
                text = "SABI AI supports natural Nigerian English and Pidgin. Major regional languages are currently in active training.",
                style = MaterialTheme.typography.bodySmall,
                color = SabiTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(SabiLanguage.entries) { lang ->
                    val isSelected = lang == selectedLanguage
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                onLanguageSelected(lang)
                            }
                            .testTag("language_item_${lang.id}"),
                        color = if (isSelected) SabiGreenContainer else SabiNavySurface,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = lang.displayName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) SabiGreenGlow else SabiTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "(${lang.nativeName})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SabiTextSecondary
                                    )
                                }
                                if (!lang.isSupported) {
                                    Text(
                                        text = "Training custom Nigerian model dataset",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SabiGold
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!lang.isSupported) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF451A03),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiGold)
                                    ) {
                                        Text(
                                            text = "Coming Soon",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SabiGold
                                        )
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SabiGreenContainer
                                    ) {
                                        Text(
                                            text = "Active",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SabiGreenGlow
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = SabiGreenGlow
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeSelectorSheet(
    selectedMode: ConversationMode,
    onModeSelected: (ConversationMode) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SabiNavyElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = SabiNavyBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .testTag("mode_selector_sheet")
        ) {
            Text(
                text = "Response Style & Mode",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SabiTextPrimary
            )
            Text(
                text = "Choose how SABI AI should respond. Tone adapts naturally without forcing slang.",
                style = MaterialTheme.typography.bodySmall,
                color = SabiTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ConversationMode.entries) { mode ->
                    val isSelected = mode == selectedMode
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                onModeSelected(mode)
                            }
                            .testTag("mode_item_${mode.id}"),
                        color = if (isSelected) SabiGreenContainer else SabiNavySurface,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = mode.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) SabiGreenGlow else SabiTextPrimary
                                )
                                Text(
                                    text = mode.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SabiTextSecondary
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SabiGreenGlow
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
