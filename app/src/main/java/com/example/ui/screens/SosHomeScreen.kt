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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.R
import com.example.data.db.AudioRecordingEntity
import com.example.ui.components.PanicButton
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

    val requiredPermissions = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECORD_AUDIO,
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
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

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
                                        text = "TAMIL NADU 24/7 ACTIVE",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tamil Nadu Women Police SOS Guard",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Direct connection to 1091 / 112 & Registered Guardians",
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
                    text = "Press button to call 1091, alert Guardians, sound Siren & Record Audio",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Action Quick Access Buttons Grid
        item {
            Text(
                text = "Emergency Quick Controls",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = "Call Police\n1091",
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
                    title = if (isSirenActive) "STOP\nSiren" else "Loud Siren\nAlarm",
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
                    title = if (isRecordingAudio) "Stop Audio\nRecord" else "Record Audio\nEvidence",
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
                    title = "SMS Alert\nGuardians",
                    icon = Icons.Default.Send,
                    containerColor = SuccessGreen,
                    modifier = Modifier.weight(1f),
                    testTag = "action_sms_guardians",
                    onClick = {
                        if (isPermissionGranted(Manifest.permission.SEND_SMS)) {
                            viewModel.sendSosSmsToGuardians()
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
                                    text = "RECORDING EVIDENCE AUDIO...",
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
                            Text("STOP")
                        }
                    }
                }
            }
        }

        // Saved Evidence Audio List
        if (audioRecordings.isNotEmpty()) {
            item {
                Text(
                    text = "Saved Audio Evidence (${audioRecordings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(audioRecordings) { recording ->
                AudioRecordingCard(
                    recording = recording,
                    isPlaying = playingAudioPath == recording.filePath,
                    onPlay = { viewModel.playRecording(recording) },
                    onDelete = { viewModel.deleteRecording(recording) }
                )
            }
        }

        // Quick Emergency Helplines Section
        item {
            Text(
                text = "Tamil Nadu & National Helplines",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HelplineBarItem(
                    name = "TN Women Police Helpline",
                    number = "1091",
                    description = "24/7 Immediate Tamil Nadu Women Distress Helpline",
                    onClick = { viewModel.triggerEmergencyCall("1091") }
                )
                HelplineBarItem(
                    name = "Emergency Response System",
                    number = "112",
                    description = "National Emergency Number (Police, Ambulance, Fire)",
                    onClick = { viewModel.triggerEmergencyCall("112") }
                )
                HelplineBarItem(
                    name = "Childline / Girls Safety",
                    number = "1098",
                    description = "24/7 Child and Girl Child Emergency Helpline",
                    onClick = { viewModel.triggerEmergencyCall("1098") }
                )
                HelplineBarItem(
                    name = "National Commission for Women",
                    number = "7827170170",
                    description = "24/7 NCW Helpline for Women in Distress",
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
