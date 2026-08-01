package com.example.ai

data class ThreatParameterMatch(
    val categoryName: String,
    val matchedKeywords: List<String>,
    val riskWeight: Int
)

data class ParameterizedAnalysisResult(
    val threatLevel: ThreatLevel,
    val scorePercentage: Int,
    val primaryCategory: String,
    val detectedParameters: List<ThreatParameterMatch>,
    val summary: String,
    val immediateEscapeSteps: List<String>,
    val tacticalDeescalationAdvice: List<String>,
    val recommendedActions: List<String>
)

object KeywordThreatAnalyzer {

    private val STALKING_KEYWORDS = listOf(
        "follow", "following", "chase", "chasing", "behind", "shadow", "stalk", "stalker",
        "trailing", "corner", "staring", "tailgating", "watching", "துரத்துறாங்க", "பின்னால வர்றான்", "பின்தொடர்",
        "நிழல்", "வழி மறிக்கிறான்", "தொடர்கிறார்", "பின்தொடர்கிறார்கள்", "துரத்துகிறான்", "பின்னடியே வாரான்", "ஓடி வர்றான்"
    )

    private val PHYSICAL_ASSAULT_KEYWORDS = listOf(
        "attack", "hit", "slap", "grab", "force", "weapon", "knife", "gun", "blood", "kill",
        "harm", "beat", "touch", "touched", "groped", "scream", "help", "strangle", "choke", "அடிக்கிறான்",
        "தொடுறான்", "தாக்குறான்", "கத்தி", "கொலை", "பயம்", "காப்பாத்துங்க", "உதவி", "கைப்பற்றுகிறான்", "துப்பாக்கி",
        "பலவந்தம்", "அடிக்க வர்றான்", "பிடிச்சி இழுக்குறான்"
    )

    private val TRANSIT_CAB_KEYWORDS = listOf(
        "cab", "taxi", "uber", "ola", "auto", "driver", "wrong route", "door lock", "isolated",
        "autorickshaw", "bus", "night travel", "train", "expressway", "ஆட்டோ", "டாக்ஸி", "டிரைவர்", "வழி மாறுறான்",
        "கதவை பூட்டுறான்", "தனி வழி", "தவறான பாதை", "பஸ்", "பேருந்து", "ரயில்"
    )

    private val VERBAL_HARASSMENT_KEYWORDS = listOf(
        "harass", "stare", "comments", "shout", "shouting", "drunk", "threaten", "abusing",
        "catcall", "eve teasing", "veeshiduran", "வேணும்னே பேசுறான்", "மிரட்டுறான்", "குடிபோதை", "முறைக்கிறான்",
        "கேலி", "அசிங்கமா பேசுறான்", "கிண்டல்", "கலாட்டா"
    )

    private val ISOLATED_NIGHT_KEYWORDS = listOf(
        "dark", "alone", "night", "no street light", "scared", "empty road", "deserted",
        "subway", "alley", "இருட்டு", "தனியா", "இரவு", "ஆள் நடமாட்டம் இல்லை", "பயமா இருக்கு", "யாருமில்லை", "தெரு விளக்கு இல்லை"
    )

    private val DOMESTIC_WORKPLACE_KEYWORDS = listOf(
        "office", "boss", "colleague", "workplace", "lock room", "threaten job", "posh", "husband", "in laws",
        "domestic violence", "அலுவலகம்", "அறை பூட்டு", "வேலை மிரட்டல்", "வீட்டு வன்முறை", "கணவன் அடிக்கிறான்"
    )

    private val CYBER_ONLINE_THREAT_KEYWORDS = listOf(
        "blackmail", "photo leak", "online threat", "video threat", "fake profile", "cyber stalking", "morphing",
        "பிளாக்மெயில்", "புகைப்படம்", "வீடியோ மிரட்டல்", "ஆன்லைன் அச்சுறுத்தல்", "போலி கணக்கு"
    )

    fun analyze(prompt: String, isTamil: Boolean = false): ParameterizedAnalysisResult {
        val clean = prompt.lowercase()
        val detected = mutableListOf<ThreatParameterMatch>()

        // 1. Physical Assault check
        val assaultMatches = PHYSICAL_ASSAULT_KEYWORDS.filter { clean.contains(it) }
        if (assaultMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("PHYSICAL_ATTACK_DISTRESS", assaultMatches, 95))
        }

        // 2. Stalking check
        val stalkingMatches = STALKING_KEYWORDS.filter { clean.contains(it) }
        if (stalkingMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("ACTIVE_STALKING_FOLLOWING", stalkingMatches, 80))
        }

        // 3. Transit cab check
        val transitMatches = TRANSIT_CAB_KEYWORDS.filter { clean.contains(it) }
        if (transitMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("CAB_TRANSIT_ANOMALY", transitMatches, 75))
        }

        // 4. Domestic & Workplace threat check
        val domesticMatches = DOMESTIC_WORKPLACE_KEYWORDS.filter { clean.contains(it) }
        if (domesticMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("WORKPLACE_DOMESTIC_THREAT", domesticMatches, 70))
        }

        // 5. Cyber threat check
        val cyberMatches = CYBER_ONLINE_THREAT_KEYWORDS.filter { clean.contains(it) }
        if (cyberMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("CYBER_HARASSMENT_BLACKMAIL", cyberMatches, 65))
        }

        // 6. Verbal Harassment check
        val harassmentMatches = VERBAL_HARASSMENT_KEYWORDS.filter { clean.contains(it) }
        if (harassmentMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("VERBAL_HARASSMENT", harassmentMatches, 60))
        }

        // 7. Isolated Night check
        val nightMatches = ISOLATED_NIGHT_KEYWORDS.filter { clean.contains(it) }
        if (nightMatches.isNotEmpty()) {
            detected.add(ThreatParameterMatch("ISOLATED_DARK_ZONE", nightMatches, 50))
        }

        val maxRisk = detected.maxOfOrNull { it.riskWeight } ?: 35
        val level = when {
            maxRisk >= 88 -> ThreatLevel.CRITICAL
            maxRisk >= 72 -> ThreatLevel.HIGH
            maxRisk >= 48 -> ThreatLevel.MEDIUM
            else -> ThreatLevel.LOW
        }

        val primaryCat = detected.firstOrNull()?.categoryName ?: "GENERAL_SAFETY_QUERY"

        val summary = if (isTamil) {
            when (level) {
                ThreatLevel.CRITICAL -> "🚨 அதிகபட்ச ஆபத்து கண்டறியப்பட்டது! உடனடி தற்காப்பு மற்றும் காவல்துறை உதவி தேவை."
                ThreatLevel.HIGH -> "⚠️ அதிக ஆபத்து எச்சரிக்கை: பாதுகாப்பான மக்கள் நடமாட்டம் உள்ள பகுதிக்கு செல்லவும்."
                ThreatLevel.MEDIUM -> "🟡 மிதமான ஆபத்து: சுற்றிலும் கவனமாக இருக்கவும்."
                ThreatLevel.LOW -> "🟢 குறைந்த ஆபத்து: விழிப்புடன் இருங்கள்."
            }
        } else {
            when (level) {
                ThreatLevel.CRITICAL -> "🚨 CRITICAL THREAT DETECTED: Immediate physical safety and police intervention required!"
                ThreatLevel.HIGH -> "⚠️ HIGH THREAT ALERT: High risk of interception or forced confrontation."
                ThreatLevel.MEDIUM -> "🟡 MODERATE THREAT: Elevated risk detected. Stay vigilant in public lit spaces."
                ThreatLevel.LOW -> "🟢 LOW THREAT: Standard situational awareness recommended."
            }
        }

        val escapeSteps = if (isTamil) {
            when (level) {
                ThreatLevel.CRITICAL -> listOf(
                    "உடனடியாக அருகில் உள்ள கடை, தேனீர் கடை அல்லது மக்கள் நடமாட்டம் உள்ள இடத்திற்கு செல்லவும்.",
                    "1091 மகளிர் காவல் எண்ணை உடனடியாக அழைக்கவும்.",
                    "உரத்த சைரன் அலாரத்தை இயக்கவும்.",
                    "பாதுகாவலர்களுக்கு அவசர குரல் பதிவு மற்றும் இருப்பிடத்தை அனுப்பவும்."
                )
                ThreatLevel.HIGH -> listOf(
                    "சாலையின் எதிர்ப்பக்கத்திற்கு மாறி நடக்கவும்.",
                    "அருகில் உள்ள அனைத்து மகளிர் காவல் நிலையத்தை நோக்கி செல்லவும்.",
                    "கைபேசியில் அவசர SOS பொத்தானை தயாராக வைக்கவும்."
                )
                else -> listOf(
                    "வெளிச்சமான பாதையில் நடக்கவும்.",
                    "ஹெட்போன் அணிவதை தவிர்க்கவும்.",
                    "பாதுகாவலருக்கு தற்போதைய இருப்பிடத்தை பகிரவும்."
                )
            }
        } else {
            when (level) {
                ThreatLevel.CRITICAL -> listOf(
                    "Step 1: Instantly enter the nearest open store, hotel, or crowd cluster.",
                    "Step 2: Press SOS to call TN Women Helpline 1091.",
                    "Step 3: Trigger the 110dB loud siren alarm to command public intervention.",
                    "Step 4: Dispatch instant 10s voice recording evidence to registered Guardians."
                )
                ThreatLevel.HIGH -> listOf(
                    "Step 1: Immediately cross the street towards light & commercial activity.",
                    "Step 2: Fake an urgent voice phone call loudly mentioning police tracking.",
                    "Step 3: Keep finger on the Sentinel AI voice recorder button."
                )
                else -> listOf(
                    "Step 1: Walk facing oncoming traffic in well-lit areas.",
                    "Step 2: Avoid looking down at your phone or using headphones.",
                    "Step 3: Keep primary guardian speed dial ready."
                )
            }
        }

        val tacticalAdvice = if (isTamil) {
            listOf(
                "பயப்படாதீர்கள். உரத்த குரலில் 'தீ!' அல்லது 'போலீஸ்!' என்று கத்தவும்.",
                "அருகில் உள்ள All Women Police Station (AWPS) நிலையத்தை தொடர்பு கொள்ளவும்."
            )
        } else {
            listOf(
                "Yell 'FIRE!' or 'POLICE!' instead of 'help' to command crowd action.",
                "Utilize Sentinel AI AWPS Directory to locate the nearest 24/7 women station."
            )
        }

        val actions = listOf("CALL_1091", "RECORD_VOICE_GUARDIANS", "SOUND_SIREN")

        return ParameterizedAnalysisResult(
            threatLevel = level,
            scorePercentage = maxRisk,
            primaryCategory = primaryCat,
            detectedParameters = detected,
            summary = summary,
            immediateEscapeSteps = escapeSteps,
            tacticalDeescalationAdvice = tacticalAdvice,
            recommendedActions = actions
        )
    }
}
