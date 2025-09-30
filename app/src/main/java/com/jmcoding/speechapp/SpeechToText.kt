package com.jmcoding.speechapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

interface SpeechToText {
    val text: StateFlow<String>
    val isListening: StateFlow<Boolean>
    val error: StateFlow<String?>
    fun start()
    fun stop()
    fun destroy()
}

class RealSpeechToText(context: Context) : SpeechToText {
    override val text = MutableStateFlow("")
    override val isListening = MutableStateFlow(false)
    override val error = MutableStateFlow<String?>(null)

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
        setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                isListening.value = true
                error.value = null
                Log.d(TAG, "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "Beginning of speech")
            }

            override fun onRmsChanged(p0: Float) = Unit

            override fun onBufferReceived(p0: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                isListening.value = false
                Log.d(TAG, "End of speech")
            }

            override fun onResults(results: Bundle?) {
                val matches = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0) ?: ""
                Log.d(TAG, "Final result: $matches")
                text.value = matches
            }

            override fun onEvent(p0: Int, p1: Bundle?) = Unit

            override fun onPartialResults(results: Bundle?) {
                val partial = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0) ?: ""
                Log.d(TAG, "Partial result: $partial")
                text.value = partial
            }

            override fun onError(errorCode: Int) {
                isListening.value = false
                val message = when (errorCode) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT -> "Cannot check support"
                    SpeechRecognizer.ERROR_CLIENT -> "Client error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "Language not supported"
                    SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "Language unavailable"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "Server disconnected"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "Too many requests"
                    else -> "Unknown error"
                }
                error.value = message
                Log.e(TAG, "STT Error: $message")
            }
        })
    }

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            true
        )
        putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            5000
        )
    }

    override fun start() {
        error.value = null
        speechRecognizer.startListening(intent)
    }

    override fun stop() {
        speechRecognizer.stopListening()
    }

    override fun destroy() {
        speechRecognizer.destroy()
    }

    companion object {
        private const val TAG = "SpeechRecognizer"
    }
}