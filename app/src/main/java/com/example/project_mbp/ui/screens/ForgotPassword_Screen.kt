package com.example.project_mbp.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_mbp.R
import com.example.project_mbp.animations.Animations.scaleClickAnimation
import com.example.project_mbp.ui.components.TextField_Custom
import com.example.project_mbp.viewmodel.User_ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPassword_Screen(
    vm: User_ViewModel = viewModel(),
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    val mess by vm.message.collectAsState()

    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // Xóa thông báo sau 3 giây (như trong Login_Screen)
    LaunchedEffect(mess) {
        if (mess != null) {
            delay(3000L)
            vm.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC9F0FF)), // Màu nền từ Login/Register
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Nút Back
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black // Tùy chỉnh màu
                )
            }
        }

        Spacer(Modifier.height(0.dp))

        // Logo
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_login), // Sử dụng logo từ màn hình khác
                contentDescription = "logo_login",
                modifier = Modifier.size(170.dp)
            )
        }

        // Khung nội dung chính
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
                Text(
                    text = stringResource(R.string.forgot_password),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // Hướng dẫn
                Text(
                    text = stringResource(R.string.password_reset_sent),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(28.dp))

                // Trường nhập Email
                TextField_Custom("Email", email, onChange = { email = it })

                Spacer(Modifier.height(28.dp))

                // Thông báo lỗi/thành công
                if (mess != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = mess!!),
                        color = if (mess == R.string.password_reset_sent) Color(0xFF388E3C) else Color.Red,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Nút Gửi
                Button(
                    onClick = {
                        scope.launch {
                            scaleClickAnimation(scale)
                            vm.sendPasswordReset(email)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .scale(scale.value),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF36435) // Màu nút từ Login
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = stringResource(R.string.send_link),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Quay về Đăng nhập
                Text(
                    stringResource(R.string.back_to_login),
                    modifier = Modifier
                        .clickable {
                            navController.navigate("login") {
                                popUpTo("forgot_password") { inclusive = true } // Đóng màn hình này
                            }
                        },
                    color = Color(0xFF21817B),
                    fontSize = 16.sp
                )
            }
        }
    }
}