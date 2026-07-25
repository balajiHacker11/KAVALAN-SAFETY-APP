package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.math.sign

class SirenPlayer(private val context: Context? = null) {
    private var playingJob: Job? = null
    private var audioTrack: AudioTrack? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun isPlaying(): Boolean = playingJob?.isActive == true

    fun startSiren() {
        if (isPlaying()) return

        // Maximize alarm audio volume if context is available
        context?.let { ctx ->
            try {
                val audioManager = ctx.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                audioManager?.let { am ->
                    val maxVol = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                    am.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, 0)
                }
            } catch (e: Exception) {
                Log.e("SirenPlayer", "Failed to maximize volume: ${e.message}")
            }
        }

        playingJob = scope.launch {
            try {
                val sampleRate = 44100
                val numSamplesPerFreq = sampleRate / 10
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

                var currentFreq = 650.0
                var goingUp = true

                while (isActive) {
                    val buffer = ShortArray(numSamplesPerFreq)
                    for (i in buffer.indices) {
                        val t = i.toDouble() / sampleRate
                        val primaryAngle = 2.0 * Math.PI * currentFreq * t
                        val harmonicAngle = 2.0 * Math.PI * (currentFreq * 1.5) * t

                        // Mix sine + square harmonic saturation for piercing police buzzer alarm sound
                        val sineWave = sin(primaryAngle)
                        val harmonicWave = sin(harmonicAngle) * 0.3
                        val squareWave = sign(sineWave) * 0.2
                        val combined = (sineWave * 0.7 + harmonicWave + squareWave).coerceIn(-1.0, 1.0)

                        buffer[i] = (combined * Short.MAX_VALUE * 0.95).toInt().toShort()
                    }
                    audioTrack?.write(buffer, 0, buffer.size)

                    if (goingUp) {
                        currentFreq += 120.0
                        if (currentFreq >= 1850.0) goingUp = false
                    } else {
                        currentFreq -= 120.0
                        if (currentFreq <= 650.0) goingUp = true
                    }
                    delay(15)
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
