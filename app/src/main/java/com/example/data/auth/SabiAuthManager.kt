package com.example.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.local.UserPreferencesEntity
import com.example.data.repository.SabiRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUserState(
    val isSignedIn: Boolean = false,
    val displayName: String = "Nigerian Innovator",
    val email: String = "jumchwerandy@gmail.com",
    val photoUrl: String? = null,
    val isGoogleUser: Boolean = false,
    val uid: String? = null
)

class SabiAuthManager(
    private val context: Context,
    private val repository: SabiRepository,
    private val scope: CoroutineScope
) {

    companion object {
        private const val TAG = "SabiAuthManager"
        // Default Google Web Client ID placeholder - users can configure in their Firebase Console
        private const val DEFAULT_SERVER_CLIENT_ID = "925461946595-sabi-ai-nigeria.apps.googleusercontent.com"
    }

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    private val firebaseAuth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        Log.w(TAG, "FirebaseAuth not initialized or google-services.json not configured yet", e)
        null
    }

    private val _authState = MutableStateFlow(
        AuthUserState(
            isSignedIn = true,
            displayName = "Nigerian Innovator",
            email = "jumchwerandy@gmail.com",
            isGoogleUser = true
        )
    )
    val authState: StateFlow<AuthUserState> = _authState.asStateFlow()

    init {
        // Observe current Firebase User if available
        firebaseAuth?.addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                updateStateFromFirebaseUser(user)
            }
        }
    }

    private fun updateStateFromFirebaseUser(user: FirebaseUser) {
        _authState.value = AuthUserState(
            isSignedIn = true,
            displayName = user.displayName ?: "Nigerian Innovator",
            email = user.email ?: "user@sabiai.ng",
            photoUrl = user.photoUrl?.toString(),
            isGoogleUser = user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID },
            uid = user.uid
        )
        // Sync with local repository user_preferences
        scope.launch(Dispatchers.IO) {
            repository.saveUserPreferences(
                UserPreferencesEntity(
                    id = "default_user",
                    displayName = user.displayName ?: "Nigerian Innovator",
                    email = user.email ?: "user@sabiai.ng",
                    defaultLanguage = "pidgin",
                    defaultMode = "casual",
                    isGoogleLinked = true
                )
            )
        }
    }

    suspend fun signInWithGoogle(
        activityContext: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(DEFAULT_SERVER_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activityContext, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val email = googleIdTokenCredential.id
                val displayName = googleIdTokenCredential.displayName ?: email.substringBefore("@")

                Log.d(TAG, "Google Credential obtained: email=$email, displayName=$displayName")

                // If Firebase Auth is ready, link token with Firebase
                if (firebaseAuth != null && idToken.isNotBlank()) {
                    try {
                        val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                        val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                        authResult.user?.let { updateStateFromFirebaseUser(it) }
                    } catch (e: Exception) {
                        Log.w(TAG, "Firebase signInWithCredential failed, using local Google profile", e)
                        // Fallback to local profile state
                        setLocalGoogleUser(displayName, email)
                    }
                } else {
                    setLocalGoogleUser(displayName, email)
                }

                onSuccess("Signed in with Google successfully as $displayName! 🇳🇬")
            } else {
                onError("Received unexpected credential type from Google.")
            }
        } catch (e: GetCredentialCancellationException) {
            Log.d(TAG, "User canceled Google Sign-In")
            onError("Sign-in was canceled.")
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException during Google Sign-In", e)
            onError("Google Sign-In failed: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during Google Sign-In", e)
            onError("Authentication error: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    private fun setLocalGoogleUser(displayName: String, email: String) {
        _authState.value = AuthUserState(
            isSignedIn = true,
            displayName = displayName,
            email = email,
            isGoogleUser = true
        )
        scope.launch(Dispatchers.IO) {
            repository.saveUserPreferences(
                UserPreferencesEntity(
                    id = "default_user",
                    displayName = displayName,
                    email = email,
                    defaultLanguage = "pidgin",
                    defaultMode = "casual",
                    isGoogleLinked = true
                )
            )
        }
    }

    fun signOut(onComplete: () -> Unit) {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            Log.w(TAG, "Error during Firebase signOut", e)
        }
        _authState.value = AuthUserState(
            isSignedIn = false,
            displayName = "Guest User",
            email = "guest@sabiai.ng",
            isGoogleUser = false
        )
        scope.launch(Dispatchers.IO) {
            repository.saveUserPreferences(
                UserPreferencesEntity(
                    id = "default_user",
                    displayName = "Guest User",
                    email = "guest@sabiai.ng",
                    defaultLanguage = "english",
                    defaultMode = "casual",
                    isGoogleLinked = false
                )
            )
        }
        onComplete()
    }
}
