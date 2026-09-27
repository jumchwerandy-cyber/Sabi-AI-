package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.local.UserPreferencesEntity
import com.example.data.model.ConversationMode
import com.example.data.model.SabiLanguage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userPreferences: UserPreferencesEntity?,
    isGoogleSignedIn: Boolean = true,
    onGoogleSignIn: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onSavePreferences: (displayName: String, email: String, defaultLang: String, defaultMode: String) -> Unit
) {
    var displayName by remember(userPreferences) {
        mutableStateOf(userPreferences?.displayName ?: "Nigerian Innovator")
    }
    var email by remember(userPreferences) {
        mutableStateOf(userPreferences?.email ?: "jumchwerandy@gmail.com")
    }
    var defaultLanguage by remember(userPreferences) {
        mutableStateOf(SabiLanguage.fromId(userPreferences?.defaultLanguage ?: "pidgin"))
    }
    var defaultMode by remember(userPreferences) {
        mutableStateOf(ConversationMode.fromId(userPreferences?.defaultMode ?: "casual"))
    }

    var showLangDropdown by remember { mutableStateOf(false) }
    var showModeDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SabiNavyDark)
            .padding(16.dp)
            .testTag("profile_screen_column"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Identity Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(SabiGreenContainer)
                            .border(2.dp, SabiGreenPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🇳🇬", fontSize = 36.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SabiTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isGoogleSignedIn) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SabiNavyElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SabiGreenGlow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Google Identity Connected",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SabiGreenGlow,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = onSignOut,
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("google_sign_out_button"),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                            ) {
                                Text("Sign Out", style = MaterialTheme.typography.labelSmall, color = SabiTextSecondary)
                            }
                        }
                    } else {
                        Button(
                            onClick = onGoogleSignIn,
                            colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("google_sign_in_button")
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign in with Google", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Account Settings Form
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "User Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SabiTextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Display Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_display_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SabiGreenPrimary,
                            unfocusedBorderColor = SabiNavyBorder,
                            focusedTextColor = SabiTextPrimary,
                            unfocusedTextColor = SabiTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Account Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_email_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SabiGreenPrimary,
                            unfocusedBorderColor = SabiNavyBorder,
                            focusedTextColor = SabiTextPrimary,
                            unfocusedTextColor = SabiTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Default Language Picker
                    Text(
                        text = "Default Conversation Language",
                        style = MaterialTheme.typography.labelMedium,
                        color = SabiTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showLangDropdown = true }
                                .testTag("profile_lang_picker"),
                            color = SabiNavyElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = defaultLanguage.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SabiTextPrimary
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SabiTextSecondary)
                            }
                        }

                        DropdownMenu(
                            expanded = showLangDropdown,
                            onDismissRequest = { showLangDropdown = false },
                            modifier = Modifier.background(SabiNavyElevated)
                        ) {
                            SabiLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${lang.displayName} ${if (!lang.isSupported) "(Coming Soon)" else ""}",
                                            color = SabiTextPrimary
                                        )
                                    },
                                    onClick = {
                                        defaultLanguage = lang
                                        showLangDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Default Response Style
                    Text(
                        text = "Default Response Style",
                        style = MaterialTheme.typography.labelMedium,
                        color = SabiTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showModeDropdown = true }
                                .testTag("profile_mode_picker"),
                            color = SabiNavyElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${defaultMode.iconEmoji} ${defaultMode.title}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SabiTextPrimary
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SabiTextSecondary)
                            }
                        }

                        DropdownMenu(
                            expanded = showModeDropdown,
                            onDismissRequest = { showModeDropdown = false },
                            modifier = Modifier.background(SabiNavyElevated)
                        ) {
                            ConversationMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${mode.iconEmoji} ${mode.title} - ${mode.description}",
                                            color = SabiTextPrimary
                                        )
                                    },
                                    onClick = {
                                        defaultMode = mode
                                        showModeDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onSavePreferences(displayName, email, defaultLanguage.id, defaultMode.id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SabiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_preferences_button")
                    ) {
                        Text("Save Preferences", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Backend & Database Integration Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SabiNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SabiNavyBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = SabiGreenGlow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Database & Supabase Readiness",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SabiTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Prepared tables:\n• users & user_preferences\n• conversations & messages\n• feedback & model evaluation dataset\n• documents (Nigerian context RAG)\n• language_settings",
                        style = MaterialTheme.typography.bodySmall,
                        color = SabiTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
