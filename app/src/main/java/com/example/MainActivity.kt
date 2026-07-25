package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SafetyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val safetyViewModel: SafetyViewModel = viewModel()
                MainContainer(viewModel = safetyViewModel)
            }
        }
    }
}
