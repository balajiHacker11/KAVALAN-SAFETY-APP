package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.example.ai.GeminiThreatAssistant
import com.example.ai.ThreatAnalysisResult
import com.example.data.db.AppDatabase
import com.example.data.db.AudioRecordingEntity
import com.example.data.db.GuardianEntity
import com.example.data.model.AppLanguage
import com.example.data.model.PoliceStation
import com.example.data.model.PoliceStationProvider
import com.example.service.AudioRecorder
import com.example.service.SirenPlayer
import com.example.service.SosManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class SafetyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val guardianDao = db.guardianDao()
    private val audioDao = db.audioRecordingDao()

    private val prefs = application.getSharedPreferences("tn_safety_prefs", Context.MODE_PRIVATE)

    // Language State
    private val initialLangCode = prefs.getString("selected_language", null)
    private val _currentLanguage = MutableStateFlow(
        if (initialLangCode == "ta") AppLanguage.TAMIL else AppLanguage.ENGLISH
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(initialLangCode == null)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    fun selectLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.edit().putString("selected_language", language.code).apply()
        _showLanguageDialog.value = false
        val msg = if (language == AppLanguage.TAMIL) "மொழி தமிழுக்கு மாற்றப்பட்டது ✅" else "Language set to English ✅"
        showNotice(msg)
    }

    fun openLanguageSelection() {
        _showLanguageDialog.value = true
    }

    fun closeLanguageSelection() {
        _showLanguageDialog.value = false
    }

    val sosManager = SosManager(application)
    val sirenPlayer = SirenPlayer(application)
    val audioRecorder = AudioRecorder(application)
    val geminiAssistant = GeminiThreatAssistant()

    // Database flows
    val guardiansList: StateFlow<List<GuardianEntity>> = guardianDao.getAllGuardians()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val audioRecordingsList: StateFlow<List<AudioRecordingEntity>> = audioDao.getAllRecordings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Siren state
    private val _isSirenActive = MutableStateFlow(false)
    val isSirenActive: StateFlow<Boolean> = _isSirenActive.asStateFlow()

    // Audio recording state
    private val _isRecordingAudio = MutableStateFlow(false)
    val isRecordingAudio: StateFlow<Boolean> = _isRecordingAudio.asStateFlow()

    private val _recordingTimerSeconds = MutableStateFlow(0)
    val recordingTimerSeconds: StateFlow<Int> = _recordingTimerSeconds.asStateFlow()

    private var recordingTimerJob: Job? = null

    // Audio playback state
    private val _playingAudioPath = MutableStateFlow<String?>(null)
    val playingAudioPath: StateFlow<String?> = _playingAudioPath.asStateFlow()

    // Police stations filter
    private val _stationSearchQuery = MutableStateFlow("")
    val stationSearchQuery: StateFlow<String> = _stationSearchQuery.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("All Districts")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    val filteredPoliceStations: StateFlow<List<PoliceStation>> = combine(
        _stationSearchQuery,
        _selectedDistrict
    ) { query, district ->
        PoliceStationProvider.tnPoliceStations.filter { station ->
            val matchesDistrict = district == "All Districts" || station.district.equals(district, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    station.name.contains(query, ignoreCase = true) ||
                    station.district.contains(query, ignoreCase = true) ||
                    station.address.contains(query, ignoreCase = true) ||
                    station.pincode.contains(query)
            matchesDistrict && matchesQuery
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PoliceStationProvider.tnPoliceStations
    )

    // AI Threat Assistant state
    private val _threatPrompt = MutableStateFlow("")
    val threatPrompt: StateFlow<String> = _threatPrompt.asStateFlow()

    private val _isEvaluatingThreat = MutableStateFlow(false)
    val isEvaluatingThreat: StateFlow<Boolean> = _isEvaluatingThreat.asStateFlow()

    private val _threatResult = MutableStateFlow<ThreatAnalysisResult?>(null)
    val threatResult: StateFlow<ThreatAnalysisResult?> = _threatResult.asStateFlow()

    // User notice state
    private val _userNotice = MutableStateFlow<String?>(null)
    val userNotice: StateFlow<String?> = _userNotice.asStateFlow()

    fun showNotice(message: String) {
        _userNotice.value = message
    }

    fun clearNotice() {
        _userNotice.value = null
    }

    // --- SOS ACTIONS ---

    fun triggerFullMasterSosAlert() {
        // 1. Direct call 1091
        sosManager.triggerDirectCall("1091")

        // 2. Automatically send SMS emergency alert to guardians
        viewModelScope.launch {
            val guardians = guardiansList.value
            val result = sosManager.sendEmergencySmsToGuardians(guardians)
            showNotice("🚨 FULL SOS ACTIVATED!\n• Direct call to 1091 initiated\n• $result\n• Siren sounding\n• Audio evidence recording started")
        }

        // 3. Automatically enable loud buzzer alarm if not already active
        if (!_isSirenActive.value) {
            sirenPlayer.startSiren()
            _isSirenActive.value = true
        }

        // 4. Automatically enable audio evidence recording if not already active
        if (!_isRecordingAudio.value) {
            startAudioEvidenceRecording()
        }
    }

    fun triggerEmergencyCall(phone: String = "1091") {
        sosManager.triggerDirectCall(phone)
        showNotice("Initiating direct call to TN Women Police ($phone)")
    }

    fun sendSosSmsToGuardians() {
        viewModelScope.launch {
            val guardians = guardiansList.value
            val result = sosManager.sendEmergencySmsToGuardians(guardians)
            showNotice(result)
        }
    }

    fun toggleSiren() {
        if (_isSirenActive.value) {
            sirenPlayer.stopSiren()
            _isSirenActive.value = false
            showNotice("Loud Siren Stopped")
        } else {
            sirenPlayer.startSiren()
            _isSirenActive.value = true
            showNotice("🚨 LOUD SIREN ACTIVATED!")
        }
    }

    // --- AUDIO RECORDING ACTIONS ---

    fun toggleAudioRecording() {
        if (_isRecordingAudio.value) {
            stopAudioEvidenceRecording()
        } else {
            startAudioEvidenceRecording()
        }
    }

    private fun startAudioEvidenceRecording() {
        val file = audioRecorder.startRecording()
        if (file != null) {
            _isRecordingAudio.value = true
            _recordingTimerSeconds.value = 0
            showNotice("🎙️ Audio Evidence Recording Started...")

            recordingTimerJob = viewModelScope.launch {
                while (_isRecordingAudio.value) {
                    delay(1000)
                    _recordingTimerSeconds.value += 1
                }
            }
        } else {
            showNotice("Unable to access microphone for recording")
        }
    }

    private fun stopAudioEvidenceRecording() {
        recordingTimerJob?.cancel()
        recordingTimerJob = null

        val duration = _recordingTimerSeconds.value
        val file: File? = audioRecorder.stopRecording()
        _isRecordingAudio.value = false

        if (file != null && file.exists()) {
            val count = audioRecordingsList.value.size + 1
            val entity = AudioRecordingEntity(
                title = "Evidence Audio #$count",
                filePath = file.absolutePath,
                durationSeconds = duration.coerceAtLeast(1)
            )
            viewModelScope.launch {
                audioDao.insertRecording(entity)
                showNotice("✅ Evidence Audio saved ($duration sec)")
            }
        } else {
            showNotice("Audio recording stopped")
        }
    }

    fun playRecording(recording: AudioRecordingEntity) {
        if (_playingAudioPath.value == recording.filePath) {
            audioRecorder.stopPlayback()
            _playingAudioPath.value = null
        } else {
            _playingAudioPath.value = recording.filePath
            audioRecorder.playAudio(recording.filePath) {
                _playingAudioPath.value = null
            }
        }
    }

    fun deleteRecording(recording: AudioRecordingEntity) {
        viewModelScope.launch {
            try {
                val file = File(recording.filePath)
                if (file.exists()) file.delete()
            } catch (e: Exception) {
                // Ignore file delete errors
            }
            audioDao.deleteRecording(recording)
            showNotice("Deleted recording")
        }
    }

    // --- GUARDIANS ACTIONS ---

    fun addGuardian(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        if (name.isBlank() || phone.isBlank()) {
            showNotice("Please provide both name and phone number")
            return
        }

        viewModelScope.launch {
            val entity = GuardianEntity(
                name = name.trim(),
                phone = phone.trim(),
                relationship = if (relationship.isBlank()) "Family" else relationship.trim(),
                isPrimary = isPrimary
            )
            guardianDao.insertGuardian(entity)
            showNotice("Saved guardian contact: ${entity.name}")
        }
    }

    fun deleteGuardian(guardian: GuardianEntity) {
        viewModelScope.launch {
            guardianDao.deleteGuardian(guardian)
            showNotice("Removed ${guardian.name}")
        }
    }

    // --- POLICE STATIONS FILTERS ---

    fun updateStationSearch(query: String) {
        _stationSearchQuery.value = query
    }

    fun selectDistrict(district: String) {
        _selectedDistrict.value = district
    }

    // --- AI THREAT ASSISTANT ---

    fun updateThreatPrompt(text: String) {
        _threatPrompt.value = text
    }

    fun evaluateThreat(scenarioOverride: String? = null) {
        val prompt = scenarioOverride ?: _threatPrompt.value
        if (prompt.isBlank()) {
            showNotice("Please describe your current situation or select a quick scenario.")
            return
        }

        _isEvaluatingThreat.value = true
        _threatPrompt.value = prompt

        viewModelScope.launch {
            val result = geminiAssistant.evaluateAttackThreat(prompt)
            _threatResult.value = result
            _isEvaluatingThreat.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        sirenPlayer.stopSiren()
        audioRecorder.stopPlayback()
        if (_isRecordingAudio.value) {
            audioRecorder.stopRecording()
        }
    }
}
