package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.ConversationMode
import com.example.data.model.FeedbackCategory
import com.example.data.model.SabiLanguage
import com.example.ui.theme.*

@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit,
    onSubmit: (FeedbackCategory, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(FeedbackCategory.WRONG_EXPRESSION) }
    var explanation by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("feedback_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SabiNavyElevated)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "What went wrong?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_feedback_dialog_button")
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SabiTextSecondary
                        )
                    }
                }

                Text(
                    text = "Help us improve SABI's Nigerian context and cultural accuracy. Your report directly trains our next dataset.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SabiTextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Category options
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeedbackCategory.entries.forEach { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = category }
                                .testTag("feedback_cat_${category.id}"),
                            color = if (isSelected) SabiGreenContainer else SabiNavySurface,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary) else null
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedCategory = category },
                                    colors = RadioButtonDefaults.colors(selectedColor = SabiGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = category.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) SabiGreenGlow else SabiTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("How should SABI have responded?") },
                    placeholder = { Text("E.g., In this context, 'dey play' is a friendly warning, not an insult...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("feedback_explanation_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SabiGreenPrimary,
                        unfocusedBorderColor = SabiNavyBorder,
                        focusedTextColor = SabiTextPrimary,
                        unfocusedTextColor = SabiTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_feedback_button")
                    ) {
                        Text("Cancel", color = SabiTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSubmit(selectedCategory, explanation) },
                        colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_feedback_button")
                    ) {
                        Text("Submit Feedback", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("voice_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SabiNavyElevated)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(SabiGreenContainer)
                        .border(2.dp, SabiGreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Voice Mic",
                        tint = SabiGreenGlow,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SABI Voice & Speech (V2/V3)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SabiTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We are training custom acoustic models tuned specifically for Nigerian English accents, Pidgin phonetics, and African regional speech dynamics (Lagos, Warri, Benin, Abuja).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SabiTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SabiNavySurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = SabiGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Status: Dataset gathering in progress across Nigerian university campuses.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SabiTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("dismiss_voice_button")
                ) {
                    Text("Got It", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AttachmentDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("attachment_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SabiNavyElevated)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Document Context (RAG)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SabiTextSecondary)
                    }
                }

                Text(
                    text = "Attach documents to give SABI instant context for schoolwork, business proposals, or Nigerian legal inquiries.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SabiTextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                val options = listOf(
                    Triple(Icons.Default.School, "Academic Syllabus / Assignment", "JAMB, WAEC, or University course material"),
                    Triple(Icons.Default.Description, "Business Proposal / POS Plan", "MSME cashflow, business plan, or contract"),
                    Triple(Icons.AutoMirrored.Filled.Assignment, "Tenancy / Agreement Document", "Review Nigerian rent terms and letters"),
                    Triple(Icons.AutoMirrored.Filled.Article, "General Text / Notes", "Paste or load text snippets")
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.forEach { (icon, title, desc) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onDismiss() }
                                .testTag("attach_option_${title.take(6).lowercase()}"),
                            color = SabiNavySurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(SabiGreenContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(icon, contentDescription = null, tint = SabiGreenGlow, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = SabiTextPrimary)
                                    Text(text = desc, style = MaterialTheme.typography.bodySmall, color = SabiTextSecondary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close", color = SabiGreenGlow)
                }
            }
        }
    }
}
