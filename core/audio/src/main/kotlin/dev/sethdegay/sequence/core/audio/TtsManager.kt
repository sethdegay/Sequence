package dev.sethdegay.sequence.core.audio

import android.content.Context
import android.content.Intent
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

        fun hasTtsEngineInstalled(context: Context): Boolean {
            val intent = Intent(TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE)
            val resolveInfo = context.packageManager.queryIntentServices(intent, 0)
            return resolveInfo.isNotEmpty()
        }
    }

    private var tts: TextToSpeech? = null

    fun initialize(): Flow<Boolean> = callbackFlow {
        val hasTtsEngineInstalled = hasTtsEngineInstalled(context)
        if (hasTtsEngineInstalled) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.apply {
                        setSpeechRate(1f)
                        setPitch(1f)
                        setAudioAttributes(attributes)
                    }
                    trySend(true)
                } else {
                    trySend(false)
                }
                channel.close()
            }
        } else {
            trySend(true)
            channel.close()
        }

        awaitClose {
            Log.d(
                "TTS",
                "Flow collection cancelled" + if (hasTtsEngineInstalled && tts != null && tts!!.engines.isNotEmpty()) ", but engine stays alive" else ""
            )
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        tts?.speak(text, queueMode, null, UTTERANCE_ID)
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        stop()
        tts?.shutdown()
    }
}