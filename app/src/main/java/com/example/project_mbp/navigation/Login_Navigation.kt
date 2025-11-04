package com.example.project_mbp.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_mbp.ui.screens.Login_Screen
import com.example.project_mbp.ui.screens.Profile_Screen
import com.example.project_mbp.ui.screens.Register_Screen
import com.example.project_mbp.viewmodel.User_ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun Login_Navigation(vm: User_ViewModel, googleSignInClient: GoogleSignInClient) {
    val navController = rememberNavController()
    val isLogined by vm.isLogined.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val mess by vm.message.collectAsState()

    // navigate sang profile
    LaunchedEffect(isLogined) {
        if (isLogined) {
            navController.navigate("profile") {
                popUpTo("login") { inclusive = true }
            }
        }
    }


    NavHost(navController = navController, startDestination = "login") {
        composable(
            route = "login?email={email}&password={password}",
            arguments = listOf(
                navArgument("email") { defaultValue = "" },
                navArgument("password") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val emailArg = backStackEntry.arguments?.getString("email") ?: ""
            val passwordArg = backStackEntry.arguments?.getString("password") ?: ""
            Login_Screen(
                vm = vm,
                googleSignInClient = googleSignInClient,
                navController = navController,
                prefillEmail = emailArg,
                prefillPassword = passwordArg
            )
        }

        composable("profile") { Profile_Screen(vm = vm) }
        composable("register") {Register_Screen(vm = vm, navController, googleSignInClient = googleSignInClient)  }

    }
}