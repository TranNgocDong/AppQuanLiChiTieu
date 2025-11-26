package com.example.project_mbp.ui.screens

<<<<<<< HEAD
import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
=======
>>>>>>> main
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
<<<<<<< HEAD
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
=======

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
>>>>>>> main
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
<<<<<<< HEAD
import androidx.navigation.NavController
import com.example.project_mbp.R
=======
import com.example.project_mbp.R

>>>>>>> main
import com.example.project_mbp.viewmodel.ThemeViewModel
import com.example.project_mbp.viewmodel.User_ViewModel

@Composable
fun CaiDatScreen(
    userViewModel: User_ViewModel,
    themeViewModel: ThemeViewModel,
<<<<<<< HEAD
    onLogout: () -> Unit,
    navController: NavController
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

=======
    onLogout: () -> Unit
) {
    var showThemeDialog by remember { mutableStateOf(false) }
>>>>>>> main
    val currentThemeMode by themeViewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
<<<<<<< HEAD
=======
            // SỬA: Dùng màu nền từ theme
>>>>>>> main
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
        // --- Ngôn Ngữ ---
        SettingItem(
            label = stringResource(R.string.language),
            iconResId = R.drawable.ngonngu,
            onClick = { showLanguageDialog = true }
        )

        // --- Thể loại ---
        SettingItem(
            label = stringResource(R.string.category_management),
            iconResId = R.drawable.theloai,
            onClick = { /* TODO */ }
        )

        // --- Giao diện ---
        SettingItem(
            label = stringResource(R.string.system_theme),
            iconResId = R.drawable.giaodien,
            onClick = { showThemeDialog = true }
        )

        SettingItem(
            label = stringResource(R.string.friends),
            iconResId = R.drawable.banbe,
            onClick = { /* TODO */ }
        )
        SettingItem(
            label = stringResource(R.string.account),
            iconResId = R.drawable.taikhoan,
            onClick = { navController.navigate("profile") }
        )
        SettingItem(
            label = stringResource(R.string.notifications),
            iconResId = R.drawable.thongbao,
            onClick = { /* TODO */ }
=======
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
>>>>>>> main
        )

        Spacer(modifier = Modifier.weight(1f))

<<<<<<< HEAD
        // --- Đăng xuất ---
=======
        // Nút Đăng Xuất
>>>>>>> main
        Button(
            onClick = {
                userViewModel.logout()
                onLogout()
            },
<<<<<<< HEAD
=======
            // SỬA: Dùng màu 'error' từ theme
>>>>>>> main
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
<<<<<<< HEAD
            Icon(Icons.Default.Logout, contentDescription = "logout", tint = MaterialTheme.colorScheme.onError)
            Text(
                text = stringResource(R.string.logout),
                color = MaterialTheme.colorScheme.onError,
=======
            // SỬA: Dùng màu 'onError'
            Icon(Icons.Default.Logout, contentDescription = "Đăng xuất", tint = MaterialTheme.colorScheme.onError)
            Text(
                text = "Đăng Xuất",
                color = MaterialTheme.colorScheme.onError, // SỬA
>>>>>>> main
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

<<<<<<< HEAD
    // --- Dialog Theme ---
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentThemeMode,
            onThemeSelected = {
                themeViewModel.setThemeMode(it)
=======
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentThemeMode,
            onThemeSelected = { newTheme ->
                themeViewModel.setThemeMode(newTheme)
>>>>>>> main
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
<<<<<<< HEAD

    // --- Dialog Language ---
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            onDismiss = { showLanguageDialog = false }
        )
    }
}


// =====================================================
// DIALOG CHỌN NGÔN NGỮ — ĐÃ FIX HOÀN CHỈNH
// =====================================================
@Composable
private fun LanguageSelectionDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val currentLocales = AppCompatDelegate.getApplicationLocales()
    val appLangTag = if (!currentLocales.isEmpty) currentLocales.toLanguageTags() else ""

    val systemLocale = LocalConfiguration.current.locales[0]
    val systemLangTag = systemLocale.language

    val isEnglish = appLangTag.contains("en") || (appLangTag.isEmpty() && systemLangTag == "en")
    val isVietnamese = !isEnglish

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.choose_language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                // --- Tiếng Việt ---
                LanguageOptionItem(
                    label = stringResource(R.string.vietnamese),
                    isSelected = isVietnamese,
                    onClick = {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags("vi")
                        )
                        activity?.recreate()
                        onDismiss()
                    }
                )

                // --- English ---
                LanguageOptionItem(
                    label = stringResource(R.string.english),
                    isSelected = isEnglish,
                    onClick = {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags("en")
                        )
                        activity?.recreate()
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageOptionItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(
            text = label,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}


// =====================================================
// DIALOG CHỌN THEME
// =====================================================
=======
}

// Composable cho Dialog chọn Theme
>>>>>>> main
@Composable
private fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
<<<<<<< HEAD
        "LIGHT" to stringResource(R.string.theme_light),
        "DARK" to stringResource(R.string.theme_dark)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(24.dp)) {

                Text(
                    text = stringResource(R.string.choose_theme),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
=======
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
>>>>>>> main
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
<<<<<<< HEAD
                            onClick = { onThemeSelected(key) }
=======
                            onClick = { onThemeSelected(key) },
                            // SỬA: Màu RadioButton cũng theo theme
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
>>>>>>> main
                        )
                        Text(
                            text = label,
                            fontSize = 18.sp,
<<<<<<< HEAD
=======
                            color = MaterialTheme.colorScheme.onSurface, // SỬA
>>>>>>> main
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


<<<<<<< HEAD
// =====================================================
// ITEM TRONG LIST CÀI ĐẶT
// =====================================================
=======
>>>>>>> main
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
<<<<<<< HEAD
=======
        // SỬA: Dùng màu 'surface'
>>>>>>> main
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
<<<<<<< HEAD
                        Badge {
=======
                        // SỬA: Dùng màu 'error' và 'onError'
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
>>>>>>> main
                            Text("1")
                        }
                    }
                },
                modifier = Modifier
                    .size(40.dp)
<<<<<<< HEAD
=======
                    // SỬA: Dùng màu 'primary' mờ để hợp cả 2 theme
>>>>>>> main
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
<<<<<<< HEAD

            Text(
                text = label,
                fontSize = 20.sp,
=======
            Text(
                text = label,
                fontSize = 20.sp,
                // SỬA: Dùng màu 'onSurface'
                color = MaterialTheme.colorScheme.onSurface,
>>>>>>> main
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
<<<<<<< HEAD

            Icon(
                Icons.Default.NavigateNext,
                contentDescription = null,
=======
            Icon(
                Icons.Default.NavigateNext,
                contentDescription = null,
                // SỬA: Dùng màu 'onSurface' mờ
>>>>>>> main
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> main
