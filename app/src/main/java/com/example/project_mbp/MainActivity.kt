package com.example.project_mbp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_mbp.navigation.Login_Navigation
import com.example.project_mbp.ui.theme.Project_mbpTheme
import com.example.project_mbp.viewmodel.ThemeViewModel
import com.example.project_mbp.viewmodel.ThemeViewModelFactory
import com.example.project_mbp.viewmodel.User_ViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    private val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-Edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Firebase
        FirebaseApp.initializeApp(this)

        // Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()
            val useDarkTheme = when (themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }

            // System UI Controller để set màu status bar & navigation bar
            val systemUiController = rememberSystemUiController()
            val appBackgroundColor = if (useDarkTheme) Color.Black else Color(0xFF3B8A83)

            LaunchedEffect(useDarkTheme) {
                systemUiController.setStatusBarColor(appBackgroundColor, darkIcons = !useDarkTheme)
                systemUiController.setNavigationBarColor(appBackgroundColor, darkIcons = !useDarkTheme)
            }

            Project_mbpTheme(darkTheme = useDarkTheme) {
                // Nền phủ toàn màn hình
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appBackgroundColor)
                ) {
                    Scaffold(
                        containerColor = Color.Transparent, // để nền Box hiện ra
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            val vm: User_ViewModel = viewModel()
                            Login_Navigation(vm, googleSignInClient, themeViewModel)
                        }
                    }
                }
            }
        }
    }
}
