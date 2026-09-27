package com.example

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.SabiDatabase
import com.example.data.repository.SabiRepository
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.SabiScreen
import com.example.ui.viewmodel.SabiViewModel
import com.example.ui.viewmodel.SabiViewModelFactory
import com.example.ui.voice.VoiceInputManager
import com.example.ui.voice.VoiceState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SabiDatabase.getDatabase(applicationContext, lifecycleScope)
        val preferencesDataStore = com.example.data.local.SabiPreferencesDataStore(applicationContext)
        val repository = SabiRepository(database.sabiDao(), preferencesDataStore = preferencesDataStore)
        val factory = SabiViewModelFactory(repository)

        setContent {
            SabiAiTheme {
                val viewModel: SabiViewModel = viewModel(factory = factory)
                SabiAppContent(viewModel, repository)
            }
        }
    }
}

@Composable
fun SabiAppContent(viewModel: SabiViewModel, repository: SabiRepository) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val conversationSearchQuery by viewModel.conversationSearchQuery.collectAsStateWithLifecycle()
    val activeConversationId by viewModel.activeConversationId.collectAsStateWithLifecycle()
    val activeMessages by viewModel.activeMessages.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedMode by viewModel.selectedMode.collectAsStateWithLifecycle()
    val activeModelId by viewModel.activeModelId.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val showFeedbackDialog by viewModel.showFeedbackDialog.collectAsStateWithLifecycle()
    val showVoiceDialog by viewModel.showVoiceDialog.collectAsStateWithLifecycle()
    val showAttachmentDialog by viewModel.showAttachmentDialog.collectAsStateWithLifecycle()
    val showLanguageSheet by viewModel.showLanguageSheet.collectAsStateWithLifecycle()
    val showModeSheet by viewModel.showModeSheet.collectAsStateWithLifecycle()

    val knowledgeQuery by viewModel.knowledgeQuery.collectAsStateWithLifecycle()
    val knowledgeDocs by viewModel.knowledgeDocuments.collectAsStateWithLifecycle()
    val assistantInput by viewModel.assistantInputText.collectAsStateWithLifecycle()
    val assistantResult by viewModel.assistantResultText.collectAsStateWithLifecycle()
    val assistantTool by viewModel.assistantToolType.collectAsStateWithLifecycle()
    val isAssistantProcessing by viewModel.isAssistantProcessing.collectAsStateWithLifecycle()
    val firestoreMetrics by viewModel.firestoreMetrics.collectAsStateWithLifecycle()
    val isLoadingMetrics by viewModel.isLoadingMetrics.collectAsStateWithLifecycle()

    val allFeedback by viewModel.allFeedback.collectAsStateWithLifecycle()
    val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Firebase Auth & Credential Manager
    val authManager = remember { com.example.data.auth.SabiAuthManager(context, repository, coroutineScope) }
    val authState by authManager.authState.collectAsStateWithLifecycle()

    // Voice Input Manager
    val voiceInputManager = remember { VoiceInputManager(context) }
    val voiceState by voiceInputManager.voiceState.collectAsStateWithLifecycle()
    val rmsDb by voiceInputManager.currentRmsDb.collectAsStateWithLifecycle()

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            voiceInputManager.startListening(
                onPartialResult = { partial ->
                    viewModel.setInputText(partial)
                },
                onFinalResult = { transcribedText ->
                    viewModel.setInputText(transcribedText)
                }
            )
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Microphone permission is needed to speak with SABI AI.")
            }
        }
    }

    // Display snackbar alerts
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button
    BackHandler {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (currentScreen != SabiScreen.LANDING) {
            viewModel.navigateTo(SabiScreen.LANDING)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SabiDrawerContent(
                currentScreen = currentScreen,
                conversations = conversations,
                activeConversationId = activeConversationId,
                searchQuery = conversationSearchQuery,
                onSearchQueryChange = { query -> viewModel.setConversationSearchQuery(query) },
                onTogglePinConversation = { id, pinned -> viewModel.togglePinConversation(id, pinned) },
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                onSelectConversation = { convId -> viewModel.selectConversation(convId) },
                onNewChat = { viewModel.startNewChat() },
                onDeleteConversation = { convId -> viewModel.deleteConversation(convId) },
                onCloseDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SabiTopBar(
                    title = when (currentScreen) {
                        SabiScreen.LANDING -> "SABI AI"
                        SabiScreen.CHAT -> "Chat with SABI"
                        SabiScreen.ASSISTANTS -> "Assistants & Rewriter"
                        SabiScreen.CONTEXT_ENGINE -> "Context Engine"
                        SabiScreen.ADMIN -> "Admin Desk"
                        SabiScreen.PROFILE -> "User Profile"
                    },
                    selectedLanguage = selectedLanguage,
                    selectedMode = selectedMode,
                    activeModelId = activeModelId,
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onLanguageClick = { viewModel.openLanguageSheet(true) },
                    onModeClick = { viewModel.openModeSheet(true) },
                    onModelChange = { modelId -> viewModel.setModel(modelId) }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = SabiNavySurface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentScreen == SabiScreen.LANDING,
                        onClick = { viewModel.navigateTo(SabiScreen.LANDING) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SabiGreenGlow,
                            selectedTextColor = SabiGreenGlow,
                            indicatorColor = SabiGreenContainer,
                            unselectedIconColor = SabiTextMuted,
                            unselectedTextColor = SabiTextMuted
                        ),
                        modifier = Modifier.testTag("nav_bottom_home")
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat") },
                        label = { Text("Chat") },
                        selected = currentScreen == SabiScreen.CHAT,
                        onClick = { viewModel.navigateTo(SabiScreen.CHAT) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SabiGreenGlow,
                            selectedTextColor = SabiGreenGlow,
                            indicatorColor = SabiGreenContainer,
                            unselectedIconColor = SabiTextMuted,
                            unselectedTextColor = SabiTextMuted
                        ),
                        modifier = Modifier.testTag("nav_bottom_chat")
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AutoFixHigh, contentDescription = "Assistants") },
                        label = { Text("Assistants") },
                        selected = currentScreen == SabiScreen.ASSISTANTS,
                        onClick = { viewModel.navigateTo(SabiScreen.ASSISTANTS) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SabiGreenGlow,
                            selectedTextColor = SabiGreenGlow,
                            indicatorColor = SabiGreenContainer,
                            unselectedIconColor = SabiTextMuted,
                            unselectedTextColor = SabiTextMuted
                        ),
                        modifier = Modifier.testTag("nav_bottom_assistants")
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Context") },
                        label = { Text("Context") },
                        selected = currentScreen == SabiScreen.CONTEXT_ENGINE,
                        onClick = { viewModel.navigateTo(SabiScreen.CONTEXT_ENGINE) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SabiGreenGlow,
                            selectedTextColor = SabiGreenGlow,
                            indicatorColor = SabiGreenContainer,
                            unselectedIconColor = SabiTextMuted,
                            unselectedTextColor = SabiTextMuted
                        ),
                        modifier = Modifier.testTag("nav_bottom_context")
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.PersonOutline, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentScreen == SabiScreen.PROFILE,
                        onClick = { viewModel.navigateTo(SabiScreen.PROFILE) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SabiGreenGlow,
                            selectedTextColor = SabiGreenGlow,
                            indicatorColor = SabiGreenContainer,
                            unselectedIconColor = SabiTextMuted,
                            unselectedTextColor = SabiTextMuted
                        ),
                        modifier = Modifier.testTag("nav_bottom_profile")
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = SabiNavyDark
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    SabiScreen.LANDING -> {
                        LandingScreen(
                            onStartChatting = { viewModel.navigateTo(SabiScreen.CHAT) },
                            onExploreFeature = { promptOrFeature ->
                                if (promptOrFeature == "explore") {
                                    viewModel.navigateTo(SabiScreen.CONTEXT_ENGINE)
                                } else {
                                    viewModel.startNewChat(promptOrFeature)
                                }
                            }
                        )
                    }
                    SabiScreen.CHAT -> {
                        ChatScreen(
                            messages = activeMessages,
                            inputText = inputText,
                            isLoading = isLoading,
                            selectedLanguage = selectedLanguage,
                            selectedMode = selectedMode,
                            voiceState = voiceState,
                            rmsDb = rmsDb,
                            onInputTextChange = { text -> viewModel.setInputText(text) },
                            onSendMessage = { overridePrompt -> viewModel.sendUserMessage(overridePrompt) },
                            onSelectMode = { mode -> viewModel.setMode(mode) },
                            onVoiceClick = {
                                if (voiceState is VoiceState.Listening) {
                                    voiceInputManager.stopListening()
                                } else {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasPermission) {
                                        voiceInputManager.startListening(
                                            onPartialResult = { partial ->
                                                viewModel.setInputText(partial)
                                            },
                                            onFinalResult = { transcribedText ->
                                                viewModel.setInputText(transcribedText)
                                            }
                                        )
                                    } else {
                                        audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            },
                            onStopVoice = { voiceInputManager.stopListening() },
                            onCancelVoice = {
                                voiceInputManager.stopListening()
                                voiceInputManager.resetState()
                            },
                            onAttachClick = { viewModel.openAttachmentDialog(true) },
                            onPositiveFeedback = { msg -> viewModel.submitFeedbackPositive(msg) },
                            onNegativeFeedback = { msg -> viewModel.openNegativeFeedbackDialog(msg) }
                        )
                    }
                    SabiScreen.ASSISTANTS -> {
                        AssistantsScreen(
                            inputText = assistantInput,
                            resultText = assistantResult,
                            selectedTool = assistantTool,
                            isProcessing = isAssistantProcessing,
                            onInputChange = { text -> viewModel.setAssistantInput(text) },
                            onToolChange = { tool -> viewModel.setAssistantTool(tool) },
                            onRunRewrite = { viewModel.runAssistantRewrite() },
                            onSendToChat = { prompt ->
                                viewModel.startNewChat(prompt)
                            }
                        )
                    }
                    SabiScreen.CONTEXT_ENGINE -> {
                        ContextEngineScreen(
                            searchQuery = knowledgeQuery,
                            documents = knowledgeDocs,
                            onSearchChange = { query -> viewModel.setKnowledgeSearchQuery(query) },
                            onAskSabi = { query -> viewModel.startNewChat(query) }
                        )
                    }
                    SabiScreen.ADMIN -> {
                        AdminScreen(
                            conversations = conversations,
                            feedbackList = allFeedback,
                            metrics = firestoreMetrics,
                            isLoadingMetrics = isLoadingMetrics,
                            onRefreshMetrics = { viewModel.refreshFirestoreMetrics() },
                            onMarkReviewed = { feedbackId -> viewModel.markAdminFeedbackReviewed(feedbackId) }
                        )
                    }
                    SabiScreen.PROFILE -> {
                        ProfileScreen(
                            userPreferences = userPreferences,
                            isGoogleSignedIn = authState.isSignedIn && authState.isGoogleUser,
                            onGoogleSignIn = {
                                coroutineScope.launch {
                                    authManager.signInWithGoogle(
                                        activityContext = context,
                                        onSuccess = { msg ->
                                            coroutineScope.launch { snackbarHostState.showSnackbar(msg) }
                                        },
                                        onError = { err ->
                                            coroutineScope.launch { snackbarHostState.showSnackbar(err) }
                                        }
                                    )
                                }
                            },
                            onSignOut = {
                                authManager.signOut {
                                    coroutineScope.launch { snackbarHostState.showSnackbar("Signed out successfully.") }
                                }
                            },
                            onSavePreferences = { name, email, lang, mode ->
                                viewModel.saveProfilePreferences(name, email, lang, mode)
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal dialogs and sheets
    if (showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { viewModel.closeFeedbackDialog() },
            onSubmit = { category, explanation ->
                viewModel.submitNegativeFeedback(category, explanation)
            }
        )
    }

    if (showVoiceDialog) {
        VoiceDialog(onDismiss = { viewModel.openVoiceDialog(false) })
    }

    if (showAttachmentDialog) {
        AttachmentDialog(onDismiss = { viewModel.openAttachmentDialog(false) })
    }

    if (showLanguageSheet) {
        LanguageSelectorSheet(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
            onDismiss = { viewModel.openLanguageSheet(false) }
        )
    }

    if (showModeSheet) {
        ModeSelectorSheet(
            selectedMode = selectedMode,
            onModeSelected = { mode -> viewModel.setMode(mode) },
            onDismiss = { viewModel.openModeSheet(false) }
        )
    }
}
