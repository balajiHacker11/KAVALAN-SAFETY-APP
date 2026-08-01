package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
        }
    }

    fun speak(text: String, language: AppLanguage) {
        if (!isInitialized || tts == null) return

        val containsTamilScript = text.any { it.code in 0x0B80..0x0BFF }
        val isTamilMode = language == AppLanguage.TAMIL || containsTamilScript

        val primaryLocale = if (isTamilMode) Locale("ta", "IN") else Locale.US
        var langResult = tts?.setLanguage(primaryLocale)

        if (isTamilMode && (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED)) {
            langResult = tts?.setLanguage(Locale("ta"))
        }

        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale.US)
        }

        _isSpeaking.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SentinelAiVoiceResponse")
    }

    fun stop() {
        _isSpeaking.value = false
        tts?.stop()
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
