package com.jmcoding.speechapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val speechToText: SpeechToText = RealSpeechToText(application)

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            speechToText.text.collect { text ->
                _recognizedText.value = text
            }
        }

        viewModelScope.launch {
            speechToText.isListening.collect { listening ->
                _isListening.value = listening
            }
        }

        viewModelScope.launch {
            speechToText.error.collect { error ->
                _error.value = error
            }
        }
    }

    fun startListening() {
        _recognizedText.value = ""
        _error.value = null
        speechToText.start()
    }

    fun stopListening() {
        speechToText.stop()
    }

    fun clearText() {
        _recognizedText.value = ""
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        speechToText.destroy()
    }
}