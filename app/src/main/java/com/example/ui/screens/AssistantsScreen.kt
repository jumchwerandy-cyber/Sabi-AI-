package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AssistantsScreen(
    inputText: String,
    resultText: String,
    selectedTool: String,
    isProcessing: Boolean,
    onInputChange: (String) -> Unit,
    onToolChange: (String) -> Unit,
    onRunRewrite: () -> Unit,
    onSendToChat: (String) -> Unit
) {
    val context = LocalContext.current

    val tools = listOf(
        Pair("nigerianize", "Make It Nigerian 🇳🇬"),
        Pair("professional", "Make Corporate / Formal 💼"),
        Pair("simplify", "Simplify Completely 💡"),
        Pair("translate", "Pidgin & English Translator 🗣")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .padding(16.dp)
            .testTag("assistants_screen_column"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Writing & Language Assistant",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SabiTextPrimary
            )
            Text(
                text = "Rewrite messages, convert tone between street Pidgin and executive corporate English, or translate with Nigerian nuance.",
                style = MaterialTheme.typography.bodySmall,
                color = SabiTextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Tool Selector Pills
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tools.forEach { (key, label) ->
                    val isSelected = selectedTool == key
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onToolChange(key) }
                            .testTag("tool_chip_$key"),
                        color = if (isSelected) SabiGreenContainer else SabiNavySurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) SabiGreenPrimary else SabiNavyBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SabiGreenGlow else SabiTextPrimary
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = SabiGreenGlow)
                            }
                        }
                    }
                }
            }
        }

        // Input Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Input Text",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputChange,
                        placeholder = {
                            Text(
                                when (selectedTool) {
                                    "nigerianize" -> "Enter standard text to give it an authentic Nigerian conversational touch..."
                                    "professional" -> "Paste Pidgin or informal notes to convert into high-level formal corporate English..."
                                    "simplify" -> "Paste a complex concept or assignment to simplify with Nigerian examples..."
                                    else -> "Paste text to translate or analyze..."
                                },
                                color = SabiTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("assistant_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SabiGreenPrimary,
                            unfocusedBorderColor = SabiNavyBorder,
                            focusedTextColor = SabiTextPrimary,
                            unfocusedTextColor = SabiTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onRunRewrite,
                        enabled = inputText.isNotBlank() && !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("run_assistant_button")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("SABI is rewriting...", color = Color.Black, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Process with SABI", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Result Card
        if (resultText.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SabiNavyElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SabiGreenDark),
                    modifier = Modifier.testTag("assistant_result_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SABI Output",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SabiGreenGlow
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("SABI Rewrite", resultText)
                                    clipboard.setPrimaryClip(clip)
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = SabiTextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SabiTextPrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = { onSendToChat(resultText) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = SabiGreenGlow)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continue Discussion in Chat", color = SabiGreenGlow)
                        }
                    }
                }
            }
        }

        // Nigerian Quick Templates
        item {
            Text(
                text = "POPULAR NIGERIAN TEMPLATES",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SabiTextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))

            val templates = listOf(
                Pair("Polite Landlord Maintenance Notice", "Dear Landlord, I hope you are well. I wish to kindly bring to your notice a maintenance need regarding..."),
                Pair("POS Agency Business Plan Summary", "Objective: Establish a reliable financial services terminal at a high-traffic junction with ₦250k initial float..."),
                Pair("Polite Follow-up to Nigerian Client", "Good day sir. Trust your week is going smoothly. Following up on our earlier discussion regarding..."),
                Pair("Simple NYSC Exemption/PPA Letter", "Respectfully requesting permission to address our service documentation...")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                templates.forEach { (title, content) ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onInputChange(content)
                            }
                            .testTag("template_${title.take(6).lowercase()}"),
                        color = SabiNavySurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SabiGreenGlow)
                            Text(text = content, style = MaterialTheme.typography.bodySmall, color = SabiTextSecondary, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}
