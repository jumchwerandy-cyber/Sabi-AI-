package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ConversationEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.SabiScreen

@Composable
fun SabiDrawerContent(
    currentScreen: SabiScreen,
    conversations: List<ConversationEntity>,
    activeConversationId: String?,
    onNavigate: (SabiScreen) -> Unit,
    onSelectConversation: (String) -> Unit,
    onNewChat: () -> Unit,
    onDeleteConversation: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = SabiNavyDark,
        drawerContentColor = SabiTextPrimary,
        modifier = Modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Brand Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(SabiScreen.LANDING)
                        onCloseDrawer()
                    }
                    .padding(vertical = 8.dp)
                    .testTag("drawer_brand_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SabiGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🇳🇬", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SABI AI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SabiTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SabiGreenContainer
                        ) {
                            Text(
                                text = "v1.2",
                                color = SabiGreenGlow,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "The AI That Understands You",
                        style = MaterialTheme.typography.labelSmall,
                        color = SabiTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // New Chat Button
            Button(
                onClick = {
                    onNewChat()
                    onCloseDrawer()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("drawer_new_chat_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "New Chat",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Links
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                DrawerNavItem(
                    icon = Icons.Default.ChatBubbleOutline,
                    label = "Chat Interface",
                    isSelected = currentScreen == SabiScreen.CHAT,
                    onClick = {
                        onNavigate(SabiScreen.CHAT)
                        onCloseDrawer()
                    }
                )
                DrawerNavItem(
                    icon = Icons.Default.AutoFixHigh,
                    label = "Assistants & Rewriter",
                    isSelected = currentScreen == SabiScreen.ASSISTANTS,
                    onClick = {
                        onNavigate(SabiScreen.ASSISTANTS)
                        onCloseDrawer()
                    }
                )
                DrawerNavItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "Nigerian Context Engine",
                    isSelected = currentScreen == SabiScreen.CONTEXT_ENGINE,
                    onClick = {
                        onNavigate(SabiScreen.CONTEXT_ENGINE)
                        onCloseDrawer()
                    }
                )
                DrawerNavItem(
                    icon = Icons.Default.Dashboard,
                    label = "Admin & Feedback Desk",
                    isSelected = currentScreen == SabiScreen.ADMIN,
                    onClick = {
                        onNavigate(SabiScreen.ADMIN)
                        onCloseDrawer()
                    }
                )
                DrawerNavItem(
                    icon = Icons.Default.PersonOutline,
                    label = "Profile & Settings",
                    isSelected = currentScreen == SabiScreen.PROFILE,
                    onClick = {
                        onNavigate(SabiScreen.PROFILE)
                        onCloseDrawer()
                    }
                )
            }

            HorizontalDivider(
                color = SabiNavyBorder,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Conversation History Title
            Text(
                text = "CONVERSATIONS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SabiTextMuted,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // History List
            if (conversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chats yet. Start a conversation!",
                        style = MaterialTheme.typography.bodySmall,
                        color = SabiTextMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(conversations, key = { it.id }) { conv ->
                        val isSelected = conv.id == activeConversationId && currentScreen == SabiScreen.CHAT
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onSelectConversation(conv.id)
                                    onCloseDrawer()
                                }
                                .testTag("conv_item_${conv.id}"),
                            color = if (isSelected) SabiNavyElevated else Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (conv.isPinned) Icons.Default.PushPin else Icons.Default.ChatBubble,
                                    contentDescription = null,
                                    tint = if (isSelected) SabiGreenGlow else SabiTextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = conv.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) SabiTextPrimary else SabiTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { onDeleteConversation(conv.id) },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("delete_conv_${conv.id}")
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Delete",
                                        tint = SabiTextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Footer
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = SabiNavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SabiGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = SabiGreenGlow,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "African Context Engine",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SabiTextPrimary
                        )
                        Text(
                            text = "English • Pidgin • Igbo/Yoruba/Hausa (Soon)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SabiTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("nav_item_${label.take(6).lowercase()}"),
        color = if (isSelected) SabiGreenContainer else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) SabiGreenGlow else SabiTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) SabiGreenGlow else SabiTextPrimary
            )
        }
    }
}
