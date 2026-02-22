package dev.sethdegay.sequence.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SfxManager @Inject constructor(@param:ApplicationContext private val context: Context) {
    companion object {
        private const val SOUND_POOL_MAX_STREAMS = 1

        private val attributes: AudioAttributes by lazy {
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        }

        private fun buildSoundPoolInstance(): SoundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(SOUND_POOL_MAX_STREAMS)
            .build()
    }

    private val ids = hashMapOf<SfxResource, Int>()

    private val soundPool = buildSoundPoolInstance()

    fun initialize(): Flow<Boolean> = callbackFlow {
        var loadedCount = 0
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                loadedCount++
                if (loadedCount == SfxResource.entries.size) {
                    trySend(true)
                    channel.close()
                }
            } else {
                trySend(false)
                channel.close()
            }
        }

        SfxResource.entries.forEach { key ->
            ids[key] = soundPool.load(context, key.id, 1)
        }

        awaitClose {
            soundPool.setOnLoadCompleteListener(null)
        }
    }

    private fun play(
        sfxResource: SfxResource,
        leftVolume: Float = 1f,
        rightVolume: Float = 1f,
        priority: Int = 1,
        loop: Int = 0,
        rate: Float = 1f,
    ) {
        ids[sfxResource]?.apply {
            soundPool.play(this, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    fun playBell() {
        play(SfxResource.BELL)
    }

    fun playTickOdd() {
        play(SfxResource.TICK_ODD)
    }

    fun playTickEven() {
        play(SfxResource.TICK_EVEN)
    }

    fun resume() {
        soundPool.autoResume()
    }

    fun pause() {
        soundPool.autoPause()
    }

    fun release() {
        soundPool.release()
    }
}