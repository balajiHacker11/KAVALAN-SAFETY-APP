package com.example.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.telephony.SmsManager
import android.util.Log
import com.example.data.db.GuardianEntity

class SosManager(private val context: Context) {

    fun triggerDirectCall(phoneNumber: String = "1091") {
        try {
            vibrateAlert()
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to dialer if direct CALL permission isn't granted at runtime
            try {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(dialIntent)
            } catch (ex: Exception) {
                Log.e("SosManager", "Failed to open phone dialer: ${ex.message}")
            }
        }
    }

    fun sendEmergencySmsToGuardians(
        guardians: List<GuardianEntity>,
        locationText: String = "Chennai, Tamil Nadu (Lat: 13.0827, Long: 80.2707)",
        customMessage: String? = null
    ): String {
        if (guardians.isEmpty()) {
            return "No guardians registered. Please add emergency contacts first!"
        }

        vibrateAlert()
        val defaultMsg = customMessage ?: "I am in danger please help me"
        var successCount = 0

        val smsManager: SmsManager = try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
        } catch (e: Exception) {
            @Suppress("DEPRECATION")
            SmsManager.getDefault()
        }

        for (guardian in guardians) {
            val phone = guardian.phone.trim()
            if (phone.isNotEmpty()) {
                try {
                    val parts = smsManager.divideMessage(defaultMsg)
                    smsManager.sendMultipartTextMessage(phone, null, parts, null, null)
                    successCount++
                } catch (e: Exception) {
                    Log.e("SosManager", "SMS to $phone failed: ${e.message}")
                }
            }
        }

        return if (successCount > 0) {
            "Emergency SMS alert sent to $successCount guardian(s)!"
        } else {
            "Triggered Emergency Alert: 'I am in danger please help me'."
        }
    }

    fun openWhatsAppAlert(phoneNumber: String, message: String) {
        try {
            val cleanPhone = phoneNumber.replace("+", "").replace(" ", "")
            val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("SosManager", "WhatsApp failed: ${e.message}")
        }
    }

    fun vibrateAlert() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        } catch (e: Exception) {
            Log.e("SosManager", "Vibration failed: ${e.message}")
        }
    }
}
