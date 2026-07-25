package com.example.service

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class SirenPlayer {
    private var playingJob: Job? = null
    private var audioTrack: AudioTrack? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun isPlaying(): Boolean = playingJob?.isActive == true

    fun startSiren() {
        if (isPlaying()) return

        playingJob = scope.launch {
            try {
                val sampleRate = 44100
                val numSamplesPerFreq = sampleRate / 8
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(sampleRate / 4)

                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack?.play()

                var currentFreq = 800.0
                var goingUp = true

                while (isActive) {
                    val buffer = ShortArray(numSamplesPerFreq)
                    for (i in buffer.indices) {
                        val angle = 2.0 * Math.PI * i * currentFreq / sampleRate
                        buffer[i] = (sin(angle) * Short.MAX_VALUE * 0.9).toInt().toShort()
                    }
                    audioTrack?.write(buffer, 0, buffer.size)

                    if (goingUp) {
                        currentFreq += 100.0
                        if (currentFreq >= 1800.0) goingUp = false
                    } else {
                        currentFreq -= 100.0
                        if (currentFreq <= 700.0) goingUp = true
                    }
                    delay(20)
                }
            } catch (e: Exception) {
                Log.e("SirenPlayer", "Error playing siren tone: ${e.message}")
            } finally {
                stopInternal()
            }
        }
    }

    fun stopSiren() {
        playingJob?.cancel()
        playingJob = null
        stopInternal()
    }

    private fun stopInternal() {
        try {
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            Log.e("SirenPlayer", "Error releasing AudioTrack: ${e.message}")
        } finally {
            audioTrack = null
        }
    }
}
