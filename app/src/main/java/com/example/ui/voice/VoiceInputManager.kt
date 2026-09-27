package com.example.ui.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class VoiceState {
    object Idle : VoiceState()
    object Listening : VoiceState()
    data class Transcribed(val text: String) : VoiceState()
    data class Error(val message: String) : VoiceState()
}

class VoiceInputManager(private val context: Context) {

    companion object {
        private const val TAG = "VoiceInputManager"

        val SAMPLE_VOICE_PROMPTS = listOf(
            "How much be 1 dollar to naira black market today?",
            "Abeg help me write a polite email to my landlord for repair",
            "Tell me about the ancient Benin Kingdom and bronze art",
            "How can I register my small business with CAC in Nigeria?",
            "Explain how POS agency banking works with OPay or Moniepoint"
        )
    }

    private var speechRecognizer: SpeechRecognizer? = null

    private val _voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _currentRmsDb = MutableStateFlow(0f)
    val currentRmsDb: StateFlow<Float> = _currentRmsDb.asStateFlow()

    fun isSpeechAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    fun startListening(
        onPartialResult: (String) -> Unit = {},
        onFinalResult: (String) -> Unit
    ) {
        if (!isSpeechAvailable()) {
            _voiceState.value = VoiceState.Error("Speech recognition is not available on this emulator/device.")
            return
        }

        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        Log.d(TAG, "onReadyForSpeech: Ready to capture voice")
                        _voiceState.value = VoiceState.Listening
                    }

                    override fun onBeginningOfSpeech() {
                        Log.d(TAG, "onBeginningOfSpeech: User started speaking")
                        _voiceState.value = VoiceState.Listening
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        _currentRmsDb.value = rmsdB
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        Log.d(TAG, "onEndOfSpeech: User stopped speaking")
                    }

                    override fun onError(error: Int) {
                        val message = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error. Check your microphone."
                            SpeechRecognizer.ERROR_CLIENT -> "Speech recognition client error. Please retry."
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
                            SpeechRecognizer.ERROR_NETWORK -> "Network error. Please check your internet connection."
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout while processing speech."
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected. Abeg speak clearly into mic."
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech service is busy. Please try again."
                            SpeechRecognizer.ERROR_SERVER -> "Google speech server error. Try again shortly."
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech heard before timeout."
                            else -> "Voice recognition error (code $error)."
                        }
                        Log.w(TAG, "SpeechRecognizer error: $message")
                        _voiceState.value = VoiceState.Error(message)
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()?.trim()
                        if (!text.isNullOrBlank()) {
                            Log.d(TAG, "Transcribed speech: $text")
                            _voiceState.value = VoiceState.Transcribed(text)
                            onFinalResult(text)
                        } else {
                            _voiceState.value = VoiceState.Error("Could not recognize clear speech.")
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val partial = matches?.firstOrNull()?.trim()
                        if (!partial.isNullOrBlank()) {
                            Log.d(TAG, "Partial speech: $partial")
                            _voiceState.value = VoiceState.Transcribed(partial)
                            // Live stream directly to chat message input field
                            onPartialResult(partial)
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                // Support Nigerian English / West African English locale
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-NG")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-NG")
                putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, arrayListOf("en-NG", "en-GB", "en-US"))
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to SABI AI in English, Pidgin, or Hausa/Yoruba/Igbo")
            }

            speechRecognizer?.startListening(intent)
            _voiceState.value = VoiceState.Listening
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start speech recognizer", e)
            _voiceState.value = VoiceState.Error("Failed to initialize microphone: ${e.message}")
        }
    }

    // Overload for simple single callback
    fun startListening(onResult: (String) -> Unit) {
        startListening(
            onPartialResult = onResult,
            onFinalResult = onResult
        )
    }

    fun transcribeSample(sample: String, onResult: (String) -> Unit) {
        _voiceState.value = VoiceState.Transcribed(sample)
        onResult(sample)
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping recognizer", e)
        } finally {
            speechRecognizer = null
            if (_voiceState.value is VoiceState.Listening) {
                _voiceState.value = VoiceState.Idle
            }
        }
    }

    fun resetState() {
        _voiceState.value = VoiceState.Idle
    }
}
