package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.GuardiansScreen
import com.example.ui.screens.PoliceStationsScreen
import com.example.ui.screens.SafetyGuideScreen
import com.example.ui.screens.SosHomeScreen
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.MagentaSecondary
import com.example.viewmodel.SafetyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    viewModel: SafetyViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val isSirenActive by viewModel.isSirenActive.collectAsStateWithLifecycle()
    val isRecordingAudio by viewModel.isRecordingAudio.collectAsStateWithLifecycle()
    val recordingTimerSeconds by viewModel.recordingTimerSeconds.collectAsStateWithLifecycle()
    val audioRecordings by viewModel.audioRecordingsList.collectAsStateWithLifecycle()
    val playingAudioPath by viewModel.playingAudioPath.collectAsStateWithLifecycle()

    val guardians by viewModel.guardiansList.collectAsStateWithLifecycle()

    val stationSearchQuery by viewModel.stationSearchQuery.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val filteredStations by viewModel.filteredPoliceStations.collectAsStateWithLifecycle()

    val threatPrompt by viewModel.threatPrompt.collectAsStateWithLifecycle()
    val isEvaluatingThreat by viewModel.isEvaluatingThreat.collectAsStateWithLifecycle()
    val threatResult by viewModel.threatResult.collectAsStateWithLifecycle()

    val userNotice by viewModel.userNotice.collectAsStateWithLifecycle()

    LaunchedEffect(userNotice) {
        userNotice?.let { notice ->
            snackbarHostState.showSnackbar(notice)
            viewModel.clearNotice()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TN Kavalan SOS",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = CrimsonPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "1091",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.triggerEmergencyCall("1091") },
                        modifier = Modifier.testTag("top_app_bar_call_1091")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Police 1091",
                            tint = CrimsonPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Warning, contentDescription = "SOS") },
                    label = { Text("SOS", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CrimsonPrimary,
                        selectedTextColor = CrimsonPrimary,
                        indicatorColor = CrimsonPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_sos")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Escape") },
                    label = { Text("AI Escape", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MagentaSecondary,
                        selectedTextColor = MagentaSecondary,
                        indicatorColor = MagentaSecondary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_ai")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.LocalPolice, contentDescription = "AWPS Police") },
                    label = { Text("AWPS Police", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CrimsonPrimary,
                        selectedTextColor = CrimsonPrimary,
                        indicatorColor = CrimsonPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_awps")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.People, contentDescription = "Guardians") },
                    label = { Text("Guardians", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CrimsonPrimary,
                        selectedTextColor = CrimsonPrimary,
                        indicatorColor = CrimsonPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_guardians")
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Safety Guide") },
                    label = { Text("Guide", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CrimsonPrimary,
                        selectedTextColor = CrimsonPrimary,
                        indicatorColor = CrimsonPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_guide")
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = CrimsonPrimary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> SosHomeScreen(
                    viewModel = viewModel,
                    isSirenActive = isSirenActive,
                    isRecordingAudio = isRecordingAudio,
                    recordingTimerSeconds = recordingTimerSeconds,
                    audioRecordings = audioRecordings,
                    playingAudioPath = playingAudioPath
                )
                1 -> AiAssistantScreen(
                    viewModel = viewModel,
                    threatPrompt = threatPrompt,
                    isEvaluatingThreat = isEvaluatingThreat,
                    threatResult = threatResult
                )
                2 -> PoliceStationsScreen(
                    viewModel = viewModel,
                    searchQuery = stationSearchQuery,
                    selectedDistrict = selectedDistrict,
                    filteredStations = filteredStations
                )
                3 -> GuardiansScreen(
                    viewModel = viewModel,
                    guardians = guardians
                )
                4 -> SafetyGuideScreen()
            }
        }
    }
}
