package com.example.service

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log

class SirenPlayer(private val context: Context? = null) {
    private var mediaPlayer: MediaPlayer? = null
    private var ringtone: Ringtone? = null
    private var isPlayingSiren = false

    fun isPlaying(): Boolean = isPlayingSiren

    fun maximizeVolume() {
        context?.let { ctx ->
            try {
                val audioManager = ctx.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                audioManager?.let { am ->
                    val maxAlarm = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                    am.setStreamVolume(AudioManager.STREAM_ALARM, maxAlarm, 0)
                    val maxMusic = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusic, 0)
                    val maxRing = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    am.setStreamVolume(AudioManager.STREAM_RING, maxRing, 0)
                }
            } catch (e: Exception) {
                Log.e("SirenPlayer", "Failed to maximize volume: ${e.message}")
            }
        }
    }

    fun startSiren() {
        if (isPlayingSiren) return

        maximizeVolume()

        val ctx = context ?: return

        try {
            var alarmUri: Uri? = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_ALARM)
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE)
            }
            if (alarmUri == null) {
                alarmUri = Settings.System.DEFAULT_ALARM_ALERT_URI
            }

            ringtone = RingtoneManager.getRingtone(ctx, alarmUri)
            if (ringtone != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone?.isLooping = true
                }
                ringtone?.play()
                isPlayingSiren = true
            } else {
                mediaPlayer = MediaPlayer.create(ctx, alarmUri)?.apply {
                    isLooping = true
                    start()
                }
                isPlayingSiren = mediaPlayer?.isPlaying == true
            }
        } catch (e: Exception) {
            Log.e("SirenPlayer", "Error playing alarm siren: ${e.message}")
            isPlayingSiren = false
        }
    }

    fun stopSiren() {
        try {
            ringtone?.stop()
            ringtone = null

            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("SirenPlayer", "Error stopping siren: ${e.message}")
        } finally {
            isPlayingSiren = false
        }
    }
}
