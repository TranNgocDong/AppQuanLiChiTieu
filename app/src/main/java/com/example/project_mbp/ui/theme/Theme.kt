package com.example.project_mbp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// === BẢNG MÀU TỐI ===
// Định nghĩa màu sắc khi ở Chế độ tối
private val DarkColorScheme = darkColorScheme(
    primary = ButtonBlue, // Nút bấm chính
    secondary = ChartLightBlue,
    tertiary = IncomeGreen, // Màu thu nhập
    background = Color(0xFF121212), // Nền ứng dụng (màu đen/xám đậm)
    surface = Color(0xFF1E1E1E), // Nền cho thẻ, dialog (hơi sáng hơn background)
    onPrimary = Color.White, // Chữ trên nút chính
    onBackground = Color.White, // Chữ trên nền ứng dụng
    onSurface = Color.White, // Chữ trên thẻ, dialog
    error = ButtonRed, // Màu cho nút Hủy, Đăng xuất
    onError = Color.White // Chữ trên nút lỗi
)

// === BẢNG MÀU SÁNG ===
// Định nghĩa màu sắc khi ở Chế độ sáng (Dựa trên thiết kế hiện tại của bạn)
private val LightColorScheme = lightColorScheme(
    primary = ButtonBlue,
    secondary = ChartLightBlue,
    tertiary = IncomeGreen,
    background = TealBackground, // Nền ứng dụng (màu Teal)
    surface = Color(0xFFD2E0DC).copy(alpha = 0.9f), // Nền cho thẻ, dialog (màu trắng)
    onPrimary = Color.White,
    onBackground = TextLight, // Chữ trên nền Teal (màu trắng)
    onSurface = Color.Black, // Chữ trên thẻ, dialog (màu đen)
    error = ButtonRed,
    onError = Color.White

    /* Các màu mặc định khác bạn có thể ghi đè
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    */
)

@Composable
fun Project_mbpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color (Android 12+)
    dynamicColor: Boolean = false, // Tắt dynamic color để dùng màu của riêng bạn
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme // <--- Dùng bảng màu Tối
        else -> LightColorScheme // <--- Dùng bảng màu Sáng
    }

    MaterialTheme(
        colorScheme = colorScheme, // Áp dụng bảng màu đã chọn
        typography = Typography,
        content = content
    )
}