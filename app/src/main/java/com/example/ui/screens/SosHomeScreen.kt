package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.db.AudioRecordingEntity
import com.example.data.db.IncidentEvidenceEntity
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.components.PanicButton
import com.example.ui.components.PermissionStatusCard
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.MagentaSecondary
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.SafetyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SosHomeScreen(
    viewModel: SafetyViewModel,
    isSirenActive: Boolean,
    isRecordingAudio: Boolean,
    recordingTimerSeconds: Int,
    audioRecordings: List<AudioRecordingEntity>,
    playingAudioPath: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val strings = remember(currentLanguage) { AppStrings.get(currentLanguage) }

    val isScreamListening by viewModel.isScreamListening.collectAsStateWithLifecycle()
    val showScreamAlertDialog by viewModel.showScreamAlertDialog.collectAsStateWithLifecycle()
    val incidentEvidences by viewModel.incidentEvidencesList.collectAsStateWithLifecycle()
    val currentAmplitude by viewModel.screamDetector.currentAmplitude.collectAsStateWithLifecycle()

    val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.triggerFullMasterSosAlert()
    }

    // Scream Emergency Trigger Dialog
    if (showScreamAlertDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissScreamDialog() },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Distress Alert",
                    tint = CrimsonPrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = strings.screamDialogTitle,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = CrimsonPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = strings.screamDialogMessage,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = CrimsonPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Vibration, contentDescription = "Haptic", tint = CrimsonPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Haptic Emergency Alarm Activated", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissScreamDialog()
                        viewModel.triggerEmergencyCall("1091")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.call1091Btn, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = {
                            viewModel.dismissScreamDialog()
                            viewModel.sendSosSmsToGuardians()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text(strings.alertSmsBtn, fontSize = 11.sp)
                    }
                    OutlinedButton(
                        onClick = { viewModel.dismissScreamDialog() }
                    ) {
                        Text(strings.cancelBtn, fontSize = 11.sp)
                    }
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Camera & Mic Permissions Request Banner
        item {
            PermissionStatusCard(
                strings = strings,
                onPermissionsGranted = {
                    viewModel.showNotice("Permissions granted successfully!")
                }
            )
        }

        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_sos),
                        contentDescription = "TN Safety Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color.Black.copy(alpha = 0.50f))
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = SuccessGreen,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = strings.sosBannerTag,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = strings.sosBannerTitle,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = strings.sosBannerSubtitle,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Giant Pulsing SOS Button
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PanicButton(
                    mainText = if (currentLanguage == AppLanguage.TAMIL) "அவசரம்" else "SOS",
                    subText = if (currentLanguage == AppLanguage.TAMIL) "உதவிக்கு அழுத்தவும்" else "TAP FOR HELP",
                    onClick = {
                        if (requiredPermissions.all { isPermissionGranted(it) }) {
                            viewModel.triggerFullMasterSosAlert()
                        } else {
                            permissionLauncher.launch(requiredPermissions)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = strings.sosInstructionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // 24/7 Scream & Help Voice Detector Toggle Card
        item {
            val cardBg by animateColorAsState(
                targetValue = if (isScreamListening) CrimsonPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                label = "scream_card_bg"
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isScreamListening) 2.dp else 1.dp,
                        color = if (isScreamListening) CrimsonPrimary else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isScreamListening) CrimsonPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = "Scream Detector",
                                    tint = if (isScreamListening) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = strings.screamDetectorTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isScreamListening) strings.screamListeningActive else strings.screamListeningOff,
                                    fontSize = 11.sp,
                                    color = if (isScreamListening) CrimsonPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    fontWeight = if (isScreamListening) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                                    viewModel.toggleScreamDetection()
                                } else {
                                    permissionLauncher.launch(requiredPermissions)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isScreamListening) CrimsonPrimary else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isScreamListening) "Active" else "Enable",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (isScreamListening) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mic Amplitude Level: ",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val normalizedAmp = (currentAmplitude / 32767f).coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = normalizedAmp.coerceAtLeast(0.05f))
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (normalizedAmp > 0.5f) CrimsonPrimary else SuccessGreen)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Quick Access Buttons Grid
        item {
            Text(
                text = strings.quickControlsTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = strings.callPoliceAction,
                    icon = Icons.Default.Call,
                    containerColor = CrimsonPrimary,
                    modifier = Modifier.weight(1f),
                    testTag = "action_call_police",
                    onClick = {
                        if (isPermissionGranted(Manifest.permission.CALL_PHONE)) {
                            viewModel.triggerEmergencyCall("1091")
                        } else {
                            permissionLauncher.launch(requiredPermissions)
                        }
                    }
                )

                val sirenBg by animateColorAsState(
                    targetValue = if (isSirenActive) CrimsonPrimary else AmberWarning,
                    label = "siren_color"
                )
                QuickActionCard(
                    title = if (isSirenActive) strings.stopSirenAction else strings.loudSirenAction,
                    icon = if (isSirenActive) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    containerColor = sirenBg,
                    modifier = Modifier.weight(1f),
                    testTag = "action_toggle_siren",
                    onClick = { viewModel.toggleSiren() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val micBg by animateColorAsState(
                    targetValue = if (isRecordingAudio) CrimsonPrimary else MagentaSecondary,
                    label = "mic_color"
                )
                QuickActionCard(
                    title = if (isRecordingAudio) strings.stopAudioAction else strings.recordAudioAction,
                    icon = if (isRecordingAudio) Icons.Default.MicOff else Icons.Default.Mic,
                    containerColor = micBg,
                    modifier = Modifier.weight(1f),
                    testTag = "action_record_audio",
                    onClick = {
                        if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                            viewModel.toggleAudioRecording()
                        } else {
                            permissionLauncher.launch(requiredPermissions)
                        }
                    }
                )

                QuickActionCard(
                    title = strings.captureIncidentPhotoAction,
                    icon = Icons.Default.CameraAlt,
                    containerColor = Color(0xFF673AB7),
                    modifier = Modifier.weight(1f),
                    testTag = "action_capture_photo",
                    onClick = {
                        if (isPermissionGranted(Manifest.permission.CAMERA)) {
                            viewModel.captureIncidentPhoto()
                        } else {
                            permissionLauncher.launch(requiredPermissions)
                        }
                    }
                )
            }
        }

        // Active Audio Evidence Recording Widget
        item {
            AnimatedVisibility(
                visible = isRecordingAudio,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CrimsonPrimary.copy(alpha = 0.12f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, CrimsonPrimary, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonPrimary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = strings.recordingEvidenceHeader,
                                    color = CrimsonPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                val mins = recordingTimerSeconds / 60
                                val secs = recordingTimerSeconds % 60
                                Text(
                                    text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.toggleAudioRecording() },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "Stop")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(strings.stopBtn)
                        }
                    }
                }
            }
        }

        // Saved Photo Evidence List
        if (incidentEvidences.isNotEmpty()) {
            item {
                Text(
                    text = "Captured Camera Evidence (${incidentEvidences.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(incidentEvidences) { evidence ->
                IncidentEvidenceCard(
                    evidence = evidence,
                    onShare = { viewModel.shareEvidenceToGuardians(context, evidence.filePath) },
                    onDelete = { viewModel.deleteEvidence(evidence) }
                )
            }
        }

        // Saved Evidence Audio List
        if (audioRecordings.isNotEmpty()) {
            item {
                Text(
                    text = "${strings.savedAudioHeader} (${audioRecordings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(audioRecordings) { recording ->
                AudioRecordingCard(
                    recording = recording,
                    isPlaying = playingAudioPath == recording.filePath,
                    onPlay = { viewModel.playRecording(recording) },
                    onShare = { viewModel.shareEvidenceToGuardians(context, recording.filePath) },
                    onDelete = { viewModel.deleteRecording(recording) }
                )
            }
        }

        // Quick Emergency Helplines Section
        item {
            Text(
                text = strings.helplinesTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HelplineBarItem(
                    name = strings.helplineTnPolice,
                    number = "1091",
                    description = strings.helplineTnPoliceDesc,
                    onClick = { viewModel.triggerEmergencyCall("1091") }
                )
                HelplineBarItem(
                    name = strings.helplineErs,
                    number = "112",
                    description = strings.helplineErsDesc,
                    onClick = { viewModel.triggerEmergencyCall("112") }
                )
                HelplineBarItem(
                    name = strings.helplineChild,
                    number = "1098",
                    description = strings.helplineChildDesc,
                    onClick = { viewModel.triggerEmergencyCall("1098") }
                )
                HelplineBarItem(
                    name = strings.helplineNcw,
                    number = "7827170170",
                    description = strings.helplineNcwDesc,
                    onClick = { viewModel.triggerEmergencyCall("7827170170") }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .height(88.dp)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun AudioRecordingCard(
    recording: AudioRecordingEntity,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(recording.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onPlay,
                    modifier = Modifier
                        .size(40.dp)
                        .background(MagentaSecondary, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = "Play Audio",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = recording.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$dateStr • ${recording.durationSeconds}s",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Evidence",
                        tint = SuccessGreen
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Recording",
                        tint = CrimsonPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun IncidentEvidenceCard(
    evidence: IncidentEvidenceEntity,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(evidence.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF673AB7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Photo Evidence",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = evidence.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$dateStr • ${evidence.locationInfo}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Photo Evidence",
                        tint = SuccessGreen
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Evidence",
                        tint = CrimsonPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun HelplineBarItem(
    name: String,
    number: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonPrimary)
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = number, fontWeight = FontWeight.Bold)
            }
        }
    }
}

