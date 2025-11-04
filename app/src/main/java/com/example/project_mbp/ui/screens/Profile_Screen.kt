package com.example.project_mbp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.project_mbp.R
import com.example.project_mbp.model.User
import com.example.project_mbp.viewmodel.User_ViewModel

@Composable
fun Profile_Screen(vm: User_ViewModel = viewModel()) {
    val profile by vm.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(profile?.avatarUrl ?: R.drawable.avt),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(24.dp))

        Box {
            Column(
                Modifier.fillMaxWidth(0.9f)
            ) {
                Text(
                    "Name: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
                UserInfoRow(user = profile, Label = "Name")

                Spacer(Modifier.height(8.dp))

                Text(
                    "Email: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
                UserInfoRow(user = profile, Label = "Email")
            }
        }
    }
}

@Composable
fun UserInfoRow(user: User?, Label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
    ) {
        user?.let { u ->
            if (Label.contains("Name", ignoreCase = true)) {
                Text(
                    text = u.name,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(15.dp)
                )
            } else if (Label.contains("Email", ignoreCase = true)) {
                Text(
                    text = u.email,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(15.dp)
                )
            }
        } ?: run {
            Text(
                text = "Chưa có thông tin người dùng...",
                fontSize = 16.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}
