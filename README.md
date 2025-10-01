# Speech-to-Text Android App

A modern Android application built with Jetpack Compose that converts speech to text in real-time using Android's SpeechRecognizer API.

## Features

- Real-time speech recognition with partial results
- Modern Material Design 3 UI
- Animated microphone button with visual feedback
- Error handling with user-friendly messages
- Clear and intuitive interface
- Permission handling for microphone access

## Prerequisites

- Android Studio Narwhal (2025.1.1) or newer
- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 34
- An Android device or emulator with Google Services

## Setup

1. Clone or download the project
2. Open in Android Studio
3. Sync Gradle files
4. Run on your device or emulator

## Permissions Required

- `RECORD_AUDIO` - For capturing voice input
- `INTERNET` - For speech recognition service

## Architecture

The app follows modern Android development practices:

- **MVVM Architecture** - Separation of concerns with ViewModel
- **Jetpack Compose** - Declarative UI framework
- **StateFlow** - Reactive state management
- **Kotlin Coroutines** - Asynchronous operations

## Project Structure

```
app/src/main/java/com/yourname/speechtotext/
├── MainActivity.kt          # Main UI screen with Compose
├── MainViewModel.kt         # Business logic and state management
└── SpeechToText.kt         # Speech recognition implementation
```

## Key Components

### SpeechToText Interface
Defines the contract for speech recognition functionality with:
- `text: StateFlow<String>` - Recognized text stream
- `isListening: StateFlow<Boolean>` - Listening state
- `error: StateFlow<String?>` - Error messages
- `start()`, `stop()`, `destroy()` - Control methods

### RealSpeechToText Implementation
Handles Android SpeechRecognizer API:
- RecognitionListener callbacks
- Partial and final results processing
- Comprehensive error handling
- Intent configuration for optimal recognition

### MainViewModel
Manages application state:
- Initializes and coordinates speech recognition
- Exposes StateFlows for UI observation
- Handles lifecycle and cleanup

### MainActivity (Compose UI)
- Permission handling
- Animated UI components
- Status indicators
- Error display with dismissal

## Usage

1. Launch the app
2. Grant microphone permission when prompted
3. Tap the microphone button to start listening
4. Speak clearly into the microphone
5. View real-time transcription on screen
6. Tap again to stop listening
7. Use "Clear" button to reset text

## Error Handling

The app handles various speech recognition errors:
- Audio recording errors
- Network connectivity issues
- No speech detected
- Permission issues
- Service unavailability

## Troubleshooting

**Speech recognition not available:**
- Ensure Google app is installed and updated
- Check internet connection
- Verify device has microphone

**Permission denied:**
- Go to Settings > Apps > [App Name] > Permissions
- Enable microphone permission

**No results:**
- Speak louder and clearer
- Check microphone is not blocked
- Ensure quiet environment
