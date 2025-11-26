package com.example.project_mbp.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_mbp.R
<<<<<<< HEAD
import com.example.project_mbp.animations.Animations
import com.example.project_mbp.animations.Animations.scaleClickAnimation
=======
import com.example.project_mbp.animations.scaleClickAnimation
>>>>>>> main
import com.example.project_mbp.ui.components.TextField_Custom
import com.example.project_mbp.viewmodel.User_ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
<<<<<<< HEAD

=======
>>>>>>> main

@Composable
fun Login_Screen(
    vm: User_ViewModel = viewModel(),
    googleSignInClient: GoogleSignInClient? = null,
    navController: NavController,
    prefillEmail: String = "",
    prefillPassword: String = ""
) {
    var email by remember { mutableStateOf(prefillEmail) }
    var password by remember { mutableStateOf(prefillPassword) }

    //by TranNgocdDong:
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()//


    val context = LocalContext.current
    val activity = context as? Activity
    val mess by vm.message.collectAsState()

    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()//



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
                vm.setMessage(R.string.error_google_token_null)
            }
        } catch (e: Exception) {
            Log.e("Login_Screen", "Google sign-in failed", e)
            vm.clearMessage()
            vm.setMessage(R.string.error_google_signin)
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
        }

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
                            text = stringResource(R.string.login_title),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(28.dp))
                        TextField_Custom("Email", email, onChange = { email = it })
                        Spacer(Modifier.height(16.dp))
                        TextField_Custom(stringResource(R.string.password_hint), password, onChange = { password = it })

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 12.dp, top = 4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("forgot_password")
                                    },
                                color = Color(0xFF21817B),
                                textDecoration = TextDecoration.Underline,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Khoảng cách này được giảm đi 1 phần do Row "Quên mật khẩu" vừa được thêm
                Spacer(Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        if (mess != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = mess!!),
                                color = Color.Red,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            LaunchedEffect(mess) {
                                delay(3000L)
                                vm.clearMessage()
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    // GỌI animation khi nhấn nút
                                    scaleClickAnimation(scale)
                                    // Sau khi animation xong thì gọi hàm đăng nhập
                                    vm.loginWithEmail(email, password)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp)
<<<<<<< HEAD
                                .scale(scale.value),
=======
                                .scale(scale.value), // Áp dụng hiệu ứng phóng to/thu nhỏ
>>>>>>> main
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF36435)
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.login_button),
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
                        Text(text = stringResource(R.string.or), fontSize = 16.sp, fontWeight = FontWeight.Medium)
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
                                            vm.setMessage(R.string.error_config_client)
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
                            Text(stringResource(R.string.no_account))
                            Text(
                                stringResource(R.string.register_link),
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("register")
                                    },
                                color = Color(0xFF21817B),
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
<<<<<<< HEAD
        } // main box
    } // main column

}
=======
        }
    }
}
>>>>>>> main
