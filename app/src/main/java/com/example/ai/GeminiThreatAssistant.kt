package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class ThreatLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

data class ThreatAnalysisResult(
    val threatLevel: ThreatLevel,
    val scorePercentage: Int, // 0 - 100
    val summary: String,
    val immediateEscapeSteps: List<String>,
    val tacticalDeescalationAdvice: List<String>,
    val recommendedHelpline: String = "1091 (TN Women Helpline) / 112"
)

class GeminiThreatAssistant {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun evaluateAttackThreat(userSituation: String): ThreatAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineTacticalFallback(userSituation)
        }

        val systemPrompt = """
            You are TN Kavalan AI Emergency Threat Evaluator and Tactical Escape Assistant for Women Safety in Tamil Nadu.
            Your task: Analyze the user's situation prompt and determine threat level, tactical escape route, and action steps.
            
            OUTPUT RULES:
            Begin response with one line specifying the threat level strictly in this format:
            THREAT_LEVEL: [LOW | MEDIUM | HIGH | CRITICAL] | SCORE: [number 0-100]
            
            Then provide:
            SUMMARY: Short 1-2 sentence risk analysis.
            ESCAPE_STEPS:
            - Step 1
            - Step 2
            - Step 3
            
            TACTICAL_ADVICE:
            - Advice 1
            - Advice 2
            
            Keep advice crisp, actionable, and focused on immediate survival and escape in Tamil Nadu environments (e.g. well-lit Tea Stalls, Petrol Bunks, Bus Stands, AWPS police stations, 1091 helpline, siren, crowds).
        """.trimIndent()

        try {
            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().put("text", "User Situation: $userSituation"))
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val sysInstructionObj = JSONObject().apply {
                    val sysPartsArray = JSONArray().apply {
                        put(JSONObject().put("text", systemPrompt))
                    }
                    put("parts", sysPartsArray)
                }
                put("systemInstruction", sysInstructionObj)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val rootJson = JSONObject(responseBody)
                val candidates = rootJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val responseText = parts.getJSONObject(0).optString("text")
                        if (responseText.isNotBlank()) {
                            return@withContext parseGeminiResponse(responseText, userSituation)
                        }
                    }
                }
            }
            getOfflineTacticalFallback(userSituation)
        } catch (e: Exception) {
            getOfflineTacticalFallback(userSituation)
        }
    }

    private fun parseGeminiResponse(text: String, originalPrompt: String): ThreatAnalysisResult {
        var level = ThreatLevel.MEDIUM
        var score = 60
        var summary = "Threat evaluation completed."
        val escapeSteps = mutableListOf<String>()
        val tacticalAdvice = mutableListOf<String>()

        val lines = text.lines()
        var currentSection = ""

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("THREAT_LEVEL:", ignoreCase = true)) {
                if (trimmed.contains("CRITICAL", ignoreCase = true)) {
                    level = ThreatLevel.CRITICAL
                    score = 92
                } else if (trimmed.contains("HIGH", ignoreCase = true)) {
                    level = ThreatLevel.HIGH
                    score = 78
                } else if (trimmed.contains("LOW", ignoreCase = true)) {
                    level = ThreatLevel.LOW
                    score = 25
                } else {
                    level = ThreatLevel.MEDIUM
                    score = 55
                }
            } else if (trimmed.startsWith("SUMMARY:", ignoreCase = true)) {
                summary = trimmed.substringAfter("SUMMARY:").trim()
            } else if (trimmed.startsWith("ESCAPE_STEPS:", ignoreCase = true)) {
                currentSection = "ESCAPE"
            } else if (trimmed.startsWith("TACTICAL_ADVICE:", ignoreCase = true)) {
                currentSection = "TACTICAL"
            } else if (trimmed.startsWith("-") || trimmed.startsWith("*") || trimmed.matches(Regex("^\\d+\\..*"))) {
                val cleanStep = trimmed.replace(Regex("^[-*\\d.]+\\s*"), "")
                if (cleanStep.isNotBlank()) {
                    if (currentSection == "ESCAPE") {
                        escapeSteps.add(cleanStep)
                    } else if (currentSection == "TACTICAL") {
                        tacticalAdvice.add(cleanStep)
                    }
                }
            }
        }

        if (escapeSteps.isEmpty()) {
            escapeSteps.addAll(listOf(
                "Head immediately towards a well-lit tea shop, petrol bunk, or bus stop.",
                "Keep phone in hand with finger ready on the TN Kavalan SOS button.",
                "Cross the street to test if person is actively following your path."
            ))
        }

        if (tacticalAdvice.isEmpty()) {
            tacticalAdvice.addAll(listOf(
                "Trigger the built-in Loud Siren to draw public attention.",
                "Call 1091 (TN Women Helpline) or your registered Guardians instantly."
            ))
        }

        return ThreatAnalysisResult(
            threatLevel = level,
            scorePercentage = score,
            summary = if (summary.length > 200) summary.take(200) + "..." else summary,
            immediateEscapeSteps = escapeSteps,
            tacticalDeescalationAdvice = tacticalAdvice
        )
    }

    private fun getOfflineTacticalFallback(prompt: String): ThreatAnalysisResult {
        val lower = prompt.lowercase()
        return when {
            lower.contains("follow") || lower.contains("chase") || lower.contains("gun") || lower.contains("knife") || lower.contains("grab") || lower.contains("corner") -> {
                ThreatAnalysisResult(
                    threatLevel = ThreatLevel.CRITICAL,
                    scorePercentage = 95,
                    summary = "CRITICAL RISK: Potential direct confrontation or active stalking detected.",
                    immediateEscapeSteps = listOf(
                        "DO NOT panic: Immediately step into nearest shop, hotel, or lit area.",
                        "Press giant red SOS button to auto-call 1091 TN Women Police.",
                        "Activate the high-decibel Siren to alert surrounding crowd.",
                        "If grabbed, use Palm Heel Strike to nose or Groin Kick and sprint towards people."
                    ),
                    tacticalDeescalationAdvice = listOf(
                        "Shout 'FIRE!' or 'POLICE!' loudly rather than 'help' to command crowd intervention.",
                        "Share live location link to Guardians via Emergency SMS."
                    )
                )
            }
            lower.contains("auto") || lower.contains("cab") || lower.contains("driver") || lower.contains("wrong route") || lower.contains("night") -> {
                ThreatAnalysisResult(
                    threatLevel = ThreatLevel.HIGH,
                    scorePercentage = 75,
                    summary = "HIGH RISK: Unsafe vehicle or route anomaly detected during transit.",
                    immediateEscapeSteps = listOf(
                        "Assertively tell driver: 'My family and police are tracking this route live'.",
                        "If vehicle stops in dark spot, open door immediately and exit towards lit zone.",
                        "Send current location link via Guardian SMS.",
                        "Hold phone with one-tap Audio Evidence Recorder active."
                    ),
                    tacticalDeescalationAdvice = listOf(
                        "Fake an urgent phone call describing driver details and vehicle number.",
                        "Keep hand on inner door handle."
                    )
                )
            }
            else -> {
                ThreatAnalysisResult(
                    threatLevel = ThreatLevel.MEDIUM,
                    scorePercentage = 50,
                    summary = "MODERATE RISK: Elevated situational alertness advised.",
                    immediateEscapeSteps = listOf(
                        "Scan surroundings for nearest All Women Police Station (AWPS) or police booth.",
                        "Keep phone unlocked on TN Kavalan home screen.",
                        "Walk briskly facing incoming traffic."
                    ),
                    tacticalDeescalationAdvice = listOf(
                        "Avoid wearing earplugs or looking down at phone while walking alone.",
                        "Keep primary guardian on quick speed dial."
                    )
                )
            }
        }
    }
}
