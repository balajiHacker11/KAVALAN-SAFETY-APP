package com.example.data.model

object AppStrings {

    fun get(language: AppLanguage): Strings {
        return when (language) {
            AppLanguage.ENGLISH -> EnglishStrings
            AppLanguage.TAMIL -> TamilStrings
        }
    }

    interface Strings {
        // App & Navigation
        val appTitle: String
        val topAppBarCall: String
        val selectLanguage: String
        val languageDialogTitle: String
        val languageDialogSubtitle: String
        val confirmLanguage: String
        val tabSos: String
        val tabAi: String
        val tabAwps: String
        val tabGuardians: String
        val tabGuide: String

        // SOS Screen
        val sosBannerTag: String
        val sosBannerTitle: String
        val sosBannerSubtitle: String
        val sosInstructionText: String
        val sosButtonDefault: String
        val sosButtonActive: String
        val quickControlsTitle: String
        val callPoliceAction: String
        val loudSirenAction: String
        val stopSirenAction: String
        val recordAudioAction: String
        val stopAudioAction: String
        val smsGuardiansAction: String
        val recordingEvidenceHeader: String
        val stopBtn: String
        val savedAudioHeader: String
        val helplinesTitle: String
        val helplineTnPolice: String
        val helplineTnPoliceDesc: String
        val helplineErs: String
        val helplineErsDesc: String
        val helplineChild: String
        val helplineChildDesc: String
        val helplineNcw: String
        val helplineNcwDesc: String

        // Guardians Screen
        val guardiansHeaderTitle: String
        val guardiansHeaderSubtitle: String
        val testSmsBtn: String
        val savedGuardiansTitle: String
        val addNewBtn: String
        val noGuardiansTitle: String
        val noGuardiansSubtitle: String
        val addFirstGuardianBtn: String
        val addGuardianDialogTitle: String
        val guardianNameLabel: String
        val guardianPhoneLabel: String
        val guardianRelationLabel: String
        val setPrimaryContact: String
        val saveGuardianBtn: String
        val cancelBtn: String
        val primaryLabel: String

        // AWPS Screen
        val awpsTitle: String
        val awpsSubtitle: String
        val awpsSearchPlaceholder: String
        val awpsFoundCount: String
        val jurisdictionPrefix: String
        val callStationBtn: String
        val directionsBtn: String

        // AI Assistant Screen
        val aiHeaderTitle: String
        val aiHeaderSubtitle: String
        val quickScenariosTitle: String
        val aiInputPlaceholder: String
        val analyzeBtnText: String
        val evaluatingText: String
        val dangerScorePrefix: String
        val threatAnalysisTitle: String
        val tacticalEscapeTitle: String
        val deescalationTitle: String
        val call1091Btn: String
        val alertSmsBtn: String

        // Guide Screen
        val guideHeaderTitle: String
        val guideHeaderSubtitle: String
        val tabSelfDefense: String
        val tabTravelSafety: String
        val tabLegalRights: String
        val targetPrefix: String
        val executionStepsTitle: String
        val tipPrefix: String
    }

    private object EnglishStrings : Strings {
        override val appTitle = "TN Kavalan SOS"
        override val topAppBarCall = "Call Police 1091"
        override val selectLanguage = "Language / மொழி"
        override val languageDialogTitle = "Select App Language / மொழியைத் தேர்ந்தெடுக்கவும்"
        override val languageDialogSubtitle = "Choose your preferred language for safety alerts and navigation"
        override val confirmLanguage = "Confirm Language / உறுதிப்படுத்துக"
        override val tabSos = "SOS"
        override val tabAi = "AI Escape"
        override val tabAwps = "AWPS Police"
        override val tabGuardians = "Guardians"
        override val tabGuide = "Guide"

        override val sosBannerTag = "TAMIL NADU 24/7 ACTIVE"
        override val sosBannerTitle = "Tamil Nadu Women Police SOS Guard"
        override val sosBannerSubtitle = "Direct connection to 1091 / 112 & Registered Guardians"
        override val sosInstructionText = "Press button to call 1091, alert Guardians, sound Siren & Record Audio"
        override val sosButtonDefault = "TAP FOR EMERGENCY SOS"
        override val sosButtonActive = "EMERGENCY ALERT SENT!"
        override val quickControlsTitle = "Emergency Quick Controls"
        override val callPoliceAction = "Call Police\n1091"
        override val loudSirenAction = "Loud Siren\nAlarm"
        override val stopSirenAction = "STOP\nSiren"
        override val recordAudioAction = "Record Audio\nEvidence"
        override val stopAudioAction = "Stop Audio\nRecord"
        override val smsGuardiansAction = "SMS Alert\nGuardians"
        override val recordingEvidenceHeader = "RECORDING EVIDENCE AUDIO..."
        override val stopBtn = "STOP"
        override val savedAudioHeader = "Saved Audio Evidence"
        override val helplinesTitle = "Tamil Nadu & National Helplines"
        override val helplineTnPolice = "TN Women Police Helpline"
        override val helplineTnPoliceDesc = "24/7 Immediate Tamil Nadu Women Distress Helpline"
        override val helplineErs = "Emergency Response System"
        override val helplineErsDesc = "National Emergency Number (Police, Ambulance, Fire)"
        override val helplineChild = "Childline / Girls Safety"
        override val helplineChildDesc = "24/7 Child and Girl Child Emergency Helpline"
        override val helplineNcw = "National Commission for Women"
        override val helplineNcwDesc = "24/7 NCW Helpline for Women in Distress"

        override val guardiansHeaderTitle = "Emergency Guardian Network"
        override val guardiansHeaderSubtitle = "Registered guardians will receive immediate SMS alerts with your live location during SOS."
        override val testSmsBtn = "TEST EMERGENCY ALERT SMS NOW"
        override val savedGuardiansTitle = "Saved Guardians"
        override val addNewBtn = "Add New"
        override val noGuardiansTitle = "No Guardians Added Yet"
        override val noGuardiansSubtitle = "Add family members or trusted friends so they receive automatic location SMS during emergency."
        override val addFirstGuardianBtn = "+ Add First Guardian"
        override val addGuardianDialogTitle = "Add Emergency Guardian"
        override val guardianNameLabel = "Guardian Name"
        override val guardianPhoneLabel = "Mobile Number (e.g. +91 9876543210)"
        override val guardianRelationLabel = "Relationship (e.g. Mother, Father, Spouse)"
        override val setPrimaryContact = "Set as Primary Contact"
        override val saveGuardianBtn = "Save Guardian"
        override val cancelBtn = "Cancel"
        override val primaryLabel = "PRIMARY"

        override val awpsTitle = "TN All Women Police Stations (AWPS)"
        override val awpsSubtitle = "Search and contact 24/7 AWPS stations across Tamil Nadu"
        override val awpsSearchPlaceholder = "Search by station, district, or pincode..."
        override val awpsFoundCount = "AWPS Station(s) Found"
        override val jurisdictionPrefix = "Jurisdiction: "
        override val callStationBtn = "Call Station"
        override val directionsBtn = "Directions"

        override val aiHeaderTitle = "AI Attack Threat & Escape Route Finder"
        override val aiHeaderSubtitle = "Describe your situation for real-time risk level analysis & tactical escape route steps"
        override val quickScenariosTitle = "Quick Threat Scenarios"
        override val aiInputPlaceholder = "e.g. Walking home in Madurai and two strangers on a scooter are trailing me..."
        override val analyzeBtnText = "ANALYZE RISK & GET ESCAPE ROUTE"
        override val evaluatingText = "Evaluating Attack Threat Level..."
        override val dangerScorePrefix = "Danger Score: "
        override val threatAnalysisTitle = "Threat Analysis"
        override val tacticalEscapeTitle = "Immediate Tactical Escape Route Steps"
        override val deescalationTitle = "De-escalation & Defense Advice"
        override val call1091Btn = "CALL 1091"
        override val alertSmsBtn = "ALERT SMS"

        override val guideHeaderTitle = "Women Tactical Safety Guide"
        override val guideHeaderSubtitle = "Essential moves, transit rules, and legal protections"
        override val tabSelfDefense = "Self-Defense Moves"
        override val tabTravelSafety = "Travel Safety"
        override val tabLegalRights = "Rights & Laws"
        override val targetPrefix = "Target: "
        override val executionStepsTitle = "Key Execution Steps:"
        override val tipPrefix = "Tip: "
    }

    private object TamilStrings : Strings {
        override val appTitle = "TN காவலன் SOS"
        override val topAppBarCall = "மகளிர் காவல் 1091"
        override val selectLanguage = "மொழி / Language"
        override val languageDialogTitle = "பயன்பாட்டு மொழியைத் தேர்ந்தெடுக்கவும்"
        override val languageDialogSubtitle = "பாதுகாப்பு எச்சரிக்கைகள் மற்றும் வழிமுறைகளுக்கு உங்கள் விருப்ப மொழியைத் தேர்ந்தெடுக்கவும்"
        override val confirmLanguage = "மொழியை உறுதிப்படுத்துக"
        override val tabSos = "SOS உதவி"
        override val tabAi = "AI உதவி"
        override val tabAwps = "மகளிர் காவல்"
        override val tabGuardians = "பாதுகாவலர்"
        override val tabGuide = "வழிகாட்டி"

        override val sosBannerTag = "தமிழ்நாடு 24/7 இயங்குகிறது"
        override val sosBannerTitle = "தமிழ்நாடு மகளிர் காவல்துறை SOS பாதுகாப்பு"
        override val sosBannerSubtitle = "1091 / 112 மற்றும் பதிவுசெய்த பாதுகாவலர்களுக்கு நேரடி இணைப்பு"
        override val sosInstructionText = "1091 ஐ அழைக்கவும், பாதுகாவலர்களை எச்சரிக்கவும், சைரன் மற்றும் ஒலிப்பதிவு செய்ய அழுத்தவும்"
        override val sosButtonDefault = "அவசர உதவிக்கு அழுத்தவும்"
        override val sosButtonActive = "அவசர எச்சரிக்கை அனுப்பப்பட்டது!"
        override val quickControlsTitle = "அவசரக்கால கட்டுப்பாடுகள்"
        override val callPoliceAction = "காவல்துறை\n1091 அழை"
        override val loudSirenAction = "சத்தமான சைரன்\nஎச்சரிக்கை"
        override val stopSirenAction = "சைரன்\nநிறுத்து"
        override val recordAudioAction = "ஒலி ஆதாரங்களை\nபதிவு செய்"
        override val stopAudioAction = "பதிவை\nநிறுத்து"
        override val smsGuardiansAction = "SMS மூலம்\nஎச்சரி"
        override val recordingEvidenceHeader = "ஒலி ஆதாரங்கள் பதிவு செய்யப்படுகின்றன..."
        override val stopBtn = "நிறுத்து"
        override val savedAudioHeader = "சேமிக்கப்பட்ட ஒலி ஆதாரங்கள்"
        override val helplinesTitle = "தமிழ்நாடு மற்றும் தேசிய உதவி எண்கள்"
        override val helplineTnPolice = "தமிழ்நாடு மகளிர் காவல் உதவி எண்"
        override val helplineTnPoliceDesc = "24/7 உடனடி பெண்கள் அவசர பாதுகாப்பு உதவி எண்"
        override val helplineErs = "தேசிய அவசரக்கால பதில் எண் (112)"
        override val helplineErsDesc = "காவல்துறை, ஆம்புலன்ஸ், தீயணைப்பு அவசர எண்"
        override val helplineChild = "குழந்தைகள் மற்றும் பெண்கள் பாதுகாப்பு"
        override val helplineChildDesc = "24/7 குழந்தைகள் மற்றும் சிறுமிகள் பாதுகாப்பு உதவி எண்"
        override val helplineNcw = "தேசிய மகளிர் ஆணையம்"
        override val helplineNcwDesc = "24/7 பெண்கள் பாதுகாப்பு தேசிய ஆணையத்தின் உதவி எண்"

        override val guardiansHeaderTitle = "அவசரகால பாதுகாவலர் பிணையம்"
        override val guardiansHeaderSubtitle = "SOS இன் போது பதிவுசெய்த பாதுகாவலர்கள் உங்கள் நேரடி இருப்பிடத்துடன் SMS எச்சரிக்கைகளைப் பெறுவார்கள்."
        override val testSmsBtn = "அவசர எச்சரிக்கை SMS ஐ சோதனை செய்"
        override val savedGuardiansTitle = "சேமிக்கப்பட்ட பாதுகாவலர்கள்"
        override val addNewBtn = "+ புதிய பாதுகாவலர்"
        override val noGuardiansTitle = "பாதுகாவலர்கள் இன்னும் சேர்க்கப்படவில்லை"
        override val noGuardiansSubtitle = "குடும்ப உறுப்பினர்கள் அல்லது நம்பகமான நண்பர்களைச் சேர்க்கவும், அவசரக்காலத்தில் தானியங்கி SMS செல்லும்."
        override val addFirstGuardianBtn = "+ முதல் பாதுகாவலரைச் சேர்"
        override val addGuardianDialogTitle = "அவசரகால பாதுகாவலரைச் சேர்"
        override val guardianNameLabel = "பாதுகாவலர் பெயர்"
        override val guardianPhoneLabel = "கைபேசி எண் (எ.கா. +91 9876543210)"
        override val guardianRelationLabel = "உறவுமுறை (எ.கா. தாய், தந்தை, கணவர்)"
        override val setPrimaryContact = "முதன்மை தொடர்பாக அமை"
        override val saveGuardianBtn = "பாதுகாவலரை சேமிக்கவும்"
        override val cancelBtn = "ரத்துசெய்"
        override val primaryLabel = "முதன்மை"

        override val awpsTitle = "தமிழ்நாடு அனைத்து மகளிர் காவல் நிலையங்கள் (AWPS)"
        override val awpsSubtitle = "தமிழ்நாடு முழுவதும் உள்ள 24/7 மகளிர் காவல் நிலையங்களைத் தொடர்புகொள்ளவும்"
        override val awpsSearchPlaceholder = "காவல் நிலையம், மாவட்டம் அல்லது பின்கோட் மூலம் தேடுக..."
        override val awpsFoundCount = "மகளிர் காவல் நிலையங்கள் கண்டறியப்பட்டன"
        override val jurisdictionPrefix = "எல்லை / பகுதி: "
        override val callStationBtn = "நிலையத்திற்கு அழை"
        override val directionsBtn = "வழித்தடம்"

        override val aiHeaderTitle = "AI அச்சுறுத்தல் பகுப்பாய்வு & தப்பிக்கும் வழி"
        override val aiHeaderSubtitle = "நேரலை ஆபத்து பகுப்பாய்வு மற்றும் தப்பிக்கும் வழிகளுக்கு உங்கள் சூழ்நிலையை விவரிக்கவும்"
        override val quickScenariosTitle = "விரைவு அச்சுறுத்தல் சூழ்நிலைகள்"
        override val aiInputPlaceholder = "எ.கா. மதுரையில் தனியாக நடந்து செல்லும்போது இருவர் பின்தொடர்கிறார்கள்..."
        override val analyzeBtnText = "ஆபத்தை ஆராய்ந்து தப்பிக்கும் வழியைப் பெறுக"
        override val evaluatingText = "ஆபத்து நிலை ஆராயப்படுகிறது..."
        override val dangerScorePrefix = "ஆபத்து அளவு: "
        override val threatAnalysisTitle = "அச்சுறுத்தல் பகுப்பாய்வு"
        override val tacticalEscapeTitle = "உடனடி தப்பிக்கும் வழிமுறைகள்"
        override val deescalationTitle = "தற்காப்பு மற்றும் தப்பிக்கும் ஆலோசனைகள்"
        override val call1091Btn = "1091 ஐ அழை"
        override val alertSmsBtn = "SMS அனுப்பு"

        override val guideHeaderTitle = "பெண்கள் பாதுகாப்பு மற்றும் உரிமை வழிகாட்டி"
        override val guideHeaderSubtitle = "முக்கிய தற்காப்பு முறைகள், பயண விதிகள் மற்றும் சட்ட உரிமைகள்"
        override val tabSelfDefense = "தற்காப்பு முறைகள்"
        override val tabTravelSafety = "பயண பாதுகாப்பு"
        override val tabLegalRights = "சட்ட உரிமைகள்"
        override val targetPrefix = "இலக்கு: "
        override val executionStepsTitle = "முக்கிய வழிமுறைகள்:"
        override val tipPrefix = "குறிப்பு: "
    }
}
