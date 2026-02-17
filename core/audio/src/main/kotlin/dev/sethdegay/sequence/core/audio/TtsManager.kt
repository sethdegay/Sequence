package dev.sethdegay.sequence.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TtsManager @Inject constructor(@param:ApplicationContext private val context: Context) {
    companion object {
        private const val UTTERANCE_ID = "TTS_DEFAULT"

        private val attributes: AudioAttributes by lazy {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        }
    }

    private lateinit var tts: TextToSpeech

    fun initialize(): Flow<Boolean> = callbackFlow {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.setSpeechRate(1f)
                tts.setPitch(1f)
                tts.setAudioAttributes(attributes)
                trySend(true)
            } else {
                trySend(false)
            }
            channel.close()
        }
        awaitClose {
            Log.d("TTS", "Flow collection cancelled, but engine stays alive")
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        tts.speak(text, queueMode, null, UTTERANCE_ID)
    }

    fun stop() {
        tts.stop()
    }

    fun release() {
        if (::tts.isInitialized) {
            stop()
            tts.shutdown()
        }
    }
}