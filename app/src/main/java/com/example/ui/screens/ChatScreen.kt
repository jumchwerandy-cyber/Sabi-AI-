package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.data.model.ConversationMode
import com.example.data.model.SabiLanguage
import com.example.ui.theme.*
import com.example.ui.voice.VoiceState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    messages: List<MessageEntity>,
    inputText: String,
    isLoading: Boolean,
    selectedLanguage: SabiLanguage,
    selectedMode: ConversationMode,
    voiceState: VoiceState,
    rmsDb: Float,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    onSelectMode: (ConversationMode) -> Unit,
    onVoiceClick: () -> Unit,
    onStopVoice: () -> Unit,
    onCancelVoice: () -> Unit,
    onAttachClick: () -> Unit,
    onPositiveFeedback: (MessageEntity) -> Unit,
    onNegativeFeedback: (MessageEntity) -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    val suggestionChips = listOf(
        "Abeg explain this to me.",
        "Help me write a professional message.",
        "Translate this to Igbo.",
        "Explain this assignment simply.",
        "Make this sound more Nigerian."
    )

    val isListening = voiceState is VoiceState.Listening

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .imePadding()
    ) {
        // Mode Selector Quick Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(SabiNavySurface)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ConversationMode.entries) { mode ->
                val isSelected = mode == selectedMode
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectMode(mode) }
                        .testTag("mode_quick_pill_${mode.id}"),
                    color = if (isSelected) SabiGreenContainer else SabiNavyElevated,
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SabiGreenPrimary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(mode.iconEmoji, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mode.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SabiGreenGlow else SabiTextSecondary
                        )
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("chat_messages_list"),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("SABI AI Message", message.content)
                        clipboard.setPrimaryClip(clip)
                    },
                    onPositiveFeedback = { onPositiveFeedback(message) },
                    onNegativeFeedback = { onNegativeFeedback(message) }
                )
            }

            if (isLoading) {
                item {
                    LoadingMessageBubble()
                }
            }
        }

        // Live Voice Listening Overlay Card
        AnimatedVisibility(visible = isListening || voiceState is VoiceState.Transcribed || voiceState is VoiceState.Error) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("voice_input_active_card"),
                shape = RoundedCornerShape(18.dp),
                color = SabiNavyElevated,
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (voiceState is VoiceState.Error) SabiError else SabiGreenPrimary
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    when (voiceState) {
                        is VoiceState.Listening -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    PulsingMicIcon()
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Listening to you...",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SabiGreenGlow
                                        )
                                        Text(
                                            text = "Speak in Nigerian English, Pidgin, or mix both",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SabiTextSecondary
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    IconButton(
                                        onClick = onStopVoice,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(SabiGreenPrimary)
                                            .testTag("voice_stop_button")
                                    ) {
                                        Icon(
                                            Icons.Default.Stop,
                                            contentDescription = "Stop",
                                            tint = Color.Black,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = onCancelVoice,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("voice_cancel_button")
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Cancel",
                                            tint = SabiTextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        is VoiceState.Transcribed -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Voice Transcribed:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SabiGreenGlow,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "\"${voiceState.text}\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = SabiTextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onSendMessage(voiceState.text)
                                        onCancelVoice()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("voice_send_transcribed_button")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Send", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        is VoiceState.Error -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = SabiError)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = voiceState.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SabiTextPrimary
                                    )
                                }
                                TextButton(
                                    onClick = onCancelVoice,
                                    modifier = Modifier.testTag("voice_error_dismiss")
                                ) {
                                    Text("Dismiss", color = SabiTextSecondary)
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        // Suggestions beneath conversation / above input
        AnimatedVisibility(visible = messages.size <= 2 && !isLoading && !isListening) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Suggestions:",
                    style = MaterialTheme.typography.labelSmall,
                    color = SabiTextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(suggestionChips) { chip ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onSendMessage(chip) }
                                .testTag("suggestion_chip_${chip.take(8).lowercase()}"),
                            color = SabiNavyElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                        ) {
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.labelMedium,
                                color = SabiTextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Input Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = SabiNavySurface,
            tonalElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isListening) SabiGreenPrimary else SabiNavyBorder
            )
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment Button
                    IconButton(
                        onClick = onAttachClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("chat_attachment_button")
                    ) {
                        Icon(
                            Icons.Default.AttachFile,
                            contentDescription = "Attach Document",
                            tint = SabiTextSecondary
                        )
                    }

                    // Voice Input Button (Toggles Listening)
                    IconButton(
                        onClick = onVoiceClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isListening) SabiGreenContainer else Color.Transparent)
                            .testTag("chat_voice_button")
                    ) {
                        Icon(
                            if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (isListening) SabiGreenGlow else SabiTextSecondary
                        )
                    }

                    // Text Input Field with dynamic Voice Placeholder
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = onInputTextChange,
                        placeholder = {
                            Text(
                                text = if (isListening) "🎤 Listening... Speak now (Pidgin or English)" else "Ask SABI anything...",
                                color = if (isListening) SabiGreenGlow else SabiTextMuted,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .testTag("chat_input_textfield"),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isListening) SabiGreenPrimary else SabiGreenPrimary,
                            unfocusedBorderColor = if (isListening) SabiGreenGlow else SabiNavyBorder,
                            focusedTextColor = SabiTextPrimary,
                            unfocusedTextColor = SabiTextPrimary,
                            cursorColor = SabiGreenPrimary
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isLoading) {
                                onSendMessage(null)
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank() && !isLoading) SabiGreenPrimary else SabiNavyElevated)
                            .testTag("chat_send_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SabiGreenGlow,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (inputText.isNotBlank()) Color.Black else SabiTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PulsingMicIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    Box(
        modifier = Modifier
            .size(36.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(SabiGreenContainer)
            .border(2.dp, SabiGreenPrimary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Mic,
            contentDescription = "Active Recording",
            tint = SabiGreenGlow,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ChatMessageItem(
    message: MessageEntity,
    onCopy: () -> Unit,
    onPositiveFeedback: () -> Unit,
    onNegativeFeedback: () -> Unit
) {
    val isUser = message.role == "user"
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SabiGreenContainer)
                    .border(1.dp, SabiGreenPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🇳🇬", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isUser) 18.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 18.dp
                ),
                color = if (isUser) SabiNavyElevated else SabiNavySurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isUser) SabiNavyBorder else SabiGreenDark.copy(alpha = 0.4f)
                ),
                modifier = Modifier.testTag("chat_bubble_${message.id.take(6)}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header metadata for Assistant message
                    if (!isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "SABI AI",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SabiGreenGlow
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SabiNavyDark
                            ) {
                                Text(
                                    text = message.mode.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SabiTextSecondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Message Content
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SabiTextPrimary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Timestamp
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = SabiTextMuted,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            // Assistant Action Bar: Copy & Feedback buttons
            if (!isUser) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                ) {
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("copy_message_${message.id.take(6)}")
                    ) {
                        Icon(
                            Icons.Outlined.ContentCopy,
                            contentDescription = "Copy text",
                            tint = SabiTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // 👍 Helpful
                    IconButton(
                        onClick = onPositiveFeedback,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("feedback_up_${message.id.take(6)}")
                    ) {
                        Icon(
                            Icons.Outlined.ThumbUp,
                            contentDescription = "Helpful",
                            tint = if (message.feedbackState == 1) SabiGreenGlow else SabiTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // 👎 Not helpful / SABI didn't understand me
                    IconButton(
                        onClick = onNegativeFeedback,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("feedback_down_${message.id.take(6)}")
                    ) {
                        Icon(
                            Icons.Outlined.ThumbDown,
                            contentDescription = "Not helpful",
                            tint = if (message.feedbackState == -1) SabiError else SabiTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "SABI didn't understand me?",
                        style = MaterialTheme.typography.labelSmall,
                        color = SabiGold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(onClick = onNegativeFeedback)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .testTag("not_understand_button_${message.id.take(6)}")
                    )
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SabiNavyElevated)
                    .border(1.dp, SabiNavyBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User",
                    tint = SabiTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingMessageBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(SabiGreenContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("🇳🇬", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SabiNavySurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = SabiGreenGlow,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "SABI is thinking in Nigerian context...",
                    style = MaterialTheme.typography.bodySmall,
                    color = SabiTextSecondary
                )
            }
        }
    }
}
