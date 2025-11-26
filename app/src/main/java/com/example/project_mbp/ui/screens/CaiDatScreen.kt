package com.example.project_mbp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.project_mbp.R

import com.example.project_mbp.viewmodel.ThemeViewModel
import com.example.project_mbp.viewmodel.User_ViewModel

@Composable
fun CaiDatScreen(
    userViewModel: User_ViewModel,
    themeViewModel: ThemeViewModel,
    onLogout: () -> Unit
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    val currentThemeMode by themeViewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // SỬA: Dùng màu nền từ theme
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SettingItem(
            label = "Ngôn ngữ",
            iconResId = R.drawable.ngonngu,
            onClick = { /* TODO: Xử lý chuyển ngôn ngữ */ }
        )
        SettingItem(
            label = "Quản lý thể loại",
            iconResId = R.drawable.theloai,
            onClick = { /* TODO: Mở màn hình quản lý thể loại */ }
        )
        SettingItem(
            label = "Giao diện hệ thống",
            iconResId = R.drawable.giaodien,
            onClick = { showThemeDialog = true } // Mở Dialog
        )
        SettingItem(
            label = "Bạn bè",
            iconResId = R.drawable.banbe,
            onClick = { /* TODO: Mở màn hình Bạn bè */ }
        )
        SettingItem(
            label = "Tài khoản",
            iconResId = R.drawable.taikhoan,
            onClick = { /* TODO: Mở màn hình Bạn bè */ }
        )
        SettingItem(
            label = "Thông báo",
            iconResId = R.drawable.thongbao,
            onClick = { /* TODO: Mở màn hình Bạn bè */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Nút Đăng Xuất
        Button(
            onClick = {
                userViewModel.logout()
                onLogout()
            },
            // SỬA: Dùng màu 'error' từ theme
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // SỬA: Dùng màu 'onError'
            Icon(Icons.Default.Logout, contentDescription = "Đăng xuất", tint = MaterialTheme.colorScheme.onError)
            Text(
                text = "Đăng Xuất",
                color = MaterialTheme.colorScheme.onError, // SỬA
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentThemeMode,
            onThemeSelected = { newTheme ->
                themeViewModel.setThemeMode(newTheme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

// Composable cho Dialog chọn Theme
@Composable
private fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        "LIGHT" to "Sáng",
        "DARK" to "Tối",
    )

    Dialog(onDismissRequest = onDismiss) {
        // SỬA: Dùng màu 'surface'
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Chọn giao diện",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface // SỬA
                )
                Spacer(modifier = Modifier.height(20.dp))

                options.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(key) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (currentTheme == key),
                            onClick = { onThemeSelected(key) },
                            // SỬA: Màu RadioButton cũng theo theme
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                        Text(
                            text = label,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface, // SỬA
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingItem(
    label: String,
    iconResId: Int,
    onClick: () -> Unit,
    showBadge: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        // SỬA: Dùng màu 'surface'
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgedBox(
                badge = {
                    if (showBadge) {
                        // SỬA: Dùng màu 'error' và 'onError'
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text("1")
                        }
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    // SỬA: Dùng màu 'primary' mờ để hợp cả 2 theme
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = label,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = label,
                fontSize = 20.sp,
                // SỬA: Dùng màu 'onSurface'
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
            Icon(
                Icons.Default.NavigateNext,
                contentDescription = null,
                // SỬA: Dùng màu 'onSurface' mờ
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}