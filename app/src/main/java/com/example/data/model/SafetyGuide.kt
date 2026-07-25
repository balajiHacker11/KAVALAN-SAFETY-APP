package com.example.data.model

data class SelfDefenseTechnique(
    val title: String,
    val targetArea: String,
    val description: String,
    val actionSteps: List<String>,
    val tips: String
)

data class SafetyChecklist(
    val category: String,
    val title: String,
    val points: List<String>
)

data class LegalRight(
    val title: String,
    val description: String,
    val actOrLaw: String
)

object SafetyGuideProvider {
    val selfDefenseTechniques = listOf(
        SelfDefenseTechnique(
            title = "Palm Heel Strike to Nose",
            targetArea = "Nose / Facial Bridge",
            description = "A powerful upward thrust with the heel of your palm forcing attacker back and causing immediate eye tearing.",
            actionSteps = listOf(
                "Keep fingers bent slightly backward to protect your fingernails",
                "Drive palm heel upwards hard under offender's nose",
                "Follow through with immediate backwards sprint to open space"
            ),
            tips = "Effective regardless of attacker size or weight."
        ),
        SelfDefenseTechnique(
            title = "Eye Gouge / Thumb Drive",
            targetArea = "Eyes",
            description = "Direct target to attacker's vision to disorient and force instantaneous release.",
            actionSteps = listOf(
                "Grasp attacker's cheek or head firmly",
                "Drive thumbs directly into corner of eyes",
                "Yell 'HELP! POLICE!' continuously to attract attention"
            ),
            tips = "Forces automatic pain reaction allowing immediate escape."
        ),
        SelfDefenseTechnique(
            title = "Groin Kick & Knee Strike",
            targetArea = "Groin",
            description = "Upward knee drive or swift instep kick to disable attacker instantly.",
            actionSteps = listOf(
                "Grab attacker's shoulders or arms for leverage",
                "Drive your knee directly upward into the groin area with maximum speed",
                "Turn immediately and run towards well-lit public area"
            ),
            tips = "Never turn your back until you kick or break grip."
        ),
        SelfDefenseTechnique(
            title = "Solar Plexus Elbow Strike",
            targetArea = "Upper Abdomen / Solar Plexus",
            description = "Used when grabbed from behind or side. Elbow is your hardest bone target.",
            actionSteps = listOf(
                "Shift weight slightly to lower your center of gravity",
                "Drive elbow horizontally backwards into attacker's chest or stomach",
                "Follow with a foot stamp directly onto attacker's instep"
            ),
            tips = "Elbow strikes carry tremendous momentum in close quarters."
        )
    )

    val safetyChecklists = listOf(
        SafetyChecklist(
            category = "Cab & Auto Travel",
            title = "Night Taxi / Auto Safety Rules",
            points = listOf(
                "Verify vehicle registration plate matches app booking before stepping in",
                "Share trip status link & live WhatsApp location with family or Primary Guardian",
                "Check child lock on auto/cab rear doors (ensure handle works from inside)",
                "Avoid sitting directly behind driver or sharing rides with unknown co-passengers at night",
                "Keep phone loaded with emergency quick-dial and Kavalan SOS app open"
            )
        ),
        SafetyChecklist(
            category = "Walking Alone",
            title = "Street & Public Night Transit Safety",
            points = listOf(
                "Walk facing incoming traffic so vehicles cannot pull up behind you unnoticed",
                "Keep both hands free (do not keep hands buried in pockets or wear noise-cancelling headphones)",
                "If followed, cross the street immediately or head directly into open shop / petrol bunk / tea stall",
                "Hold keys firmly between knuckles or hold phone with thumb on SOS button",
                "Trust your instinct: if an area feels wrong, immediately turn into a bright public zone"
            )
        ),
        SafetyChecklist(
            category = "Digital & Home",
            title = "Elevator & Home Entry Vigilance",
            points = listOf(
                "In elevators with suspicious strangers, stand near the button panel; exit immediately if uncomfortable",
                "Keep emergency numbers (1091, 112, AWPS) saved on speed dial",
                "Ensure home entrance and balcony are well-lit with functioning locks",
                "Enable secondary lock / latch when inside hotel or rental rooms"
            )
        )
    )

    val legalRights = listOf(
        LegalRight(
            title = "Zero FIR Right",
            description = "A victim of crime can register a Zero FIR at ANY police station regardless of jurisdiction or area location. The station MUST accept the complaint and transfer it.",
            actOrLaw = "Section 154 Code of Criminal Procedure / BNS"
        ),
        LegalRight(
            title = "24/7 Police Assistance & Protection",
            description = "All Women Police Stations (AWPS) in Tamil Nadu operate 24/7 to provide immediate protection, medical assistance, and lodging of FIR without delay.",
            actOrLaw = "Tamil Nadu Police Special Order & Helpline 1091"
        ),
        LegalRight(
            title = "Right Against Night Arrest for Women",
            description = "Women cannot be arrested after sunset (6 PM) and before sunrise (6 AM) except under extraordinary circumstances with prior written permission of a Judicial Magistrate.",
            actOrLaw = "Section 46(4) CrPC"
        ),
        LegalRight(
            title = "Free Legal Aid & Female Officer Statement",
            description = "Statement of female victims of sexual violence must be recorded by a female police officer or in the presence of a woman officer, with free legal assistance provided.",
            actOrLaw = "Section 157 CrPC & Legal Services Authorities Act"
        )
    )
}
