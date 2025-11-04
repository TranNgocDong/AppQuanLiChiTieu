package com.example.project_mbp.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_mbp.R
import com.example.project_mbp.ui.components.TextField_Custom
import com.example.project_mbp.viewmodel.User_ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay

@Composable
fun Register_Screen(
    vm: User_ViewModel = viewModel(),
    navController: NavController,
    googleSignInClient: GoogleSignInClient? = null
) {
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity
    val mess by vm.message.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

            account?.idToken?.let { idToken ->
                vm.loginWithGoogle(idToken)
            } ?: run {
                vm.clearMessage()
                vm.setMessage("Đăng nhập Google thất bại: idToken null")
            }
        } catch (e: Exception) {
            Log.e("Login_Screen", "Google sign-in failed", e)
            vm.clearMessage()
            vm.setMessage("Đăng nhập Google thất bại")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC9F0FF)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_login),
                contentDescription = "logo_login",
                modifier = Modifier.size(170.dp)
            )
        } // box 1

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
                .background(Color(0xFF9DCFE3), shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Đăng Ký",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(28.dp))
                        TextField_Custom("Nhập email...", email, onChange = { email = it })
                        Spacer(Modifier.height(16.dp))
                        TextField_Custom(
                            "Nhập mật khẩu...",
                            password1,
                            onChange = { password1 = it })
                        Spacer(Modifier.height(16.dp))
                        TextField_Custom(
                            "Xác Nhận Mật khẩu",
                            password2,
                            onChange = { password2 = it })
                    }
                }

                Spacer(Modifier.height(28.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        if (mess != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = mess!!, color = Color.Red, fontSize = 16.sp)
                            LaunchedEffect(mess) {
                                delay(3000L)
                                vm.clearMessage()
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Button(
                            onClick = {
                                vm.registerWithEmail(email, password1, password2, "Người dùng")
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF36435)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                text = "Đăng Ký",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 2.dp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(text = "Hoặc", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(52.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo_google),
                                contentDescription = "logo_google",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Gray, CircleShape)
                                    .clickable {
                                        googleSignInClient?.signOut()?.addOnCompleteListener {
                                            val signInIntent = googleSignInClient.signInIntent
                                            launcher.launch(signInIntent)
                                        } ?: run {
                                            vm.setMessage("GoogleSignInClient chưa được cấu hình.")
                                        }
                                    }
                            )

                            Image(
                                painter = painterResource(R.drawable.logo_facebook),
                                contentDescription = "logo_facebook",
                                modifier = Modifier.size(47.dp)
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                        Row {
                            Text("Bạn đã có tài khoản? ")
                            Text(
                                "Đăng nhập",
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("login?email=$email&password=$password1") {
                                            popUpTo("register_screen") { inclusive = true }
                                        }
                                    },
                                color = Color(0xFF21817B),
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    } // column box 3
                } // box 3
            }
        } // main box
    } // main column

}

