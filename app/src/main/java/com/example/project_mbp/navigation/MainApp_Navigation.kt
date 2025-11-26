package com.example.project_mbp.navigation

// import androidx.compose.animation.core.animateFloatAsState // <-- KHÔNG CẦN NỮA
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.draw.scale // <-- KHÔNG CẦN NỮA
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project_mbp.R
import com.example.project_mbp.ui.screens.CaiDatScreen
import com.example.project_mbp.ui.screens.GiaoDichMoiScreen
import com.example.project_mbp.ui.screens.SoGiaoDichScreen
import com.example.project_mbp.ui.screens.ThongKeScreen
import com.example.project_mbp.ui.screens.ViTienScreen
import com.example.project_mbp.viewmodel.ThemeViewModel // <-- THÊM IMPORT NÀY
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import com.example.project_mbp.viewmodel.User_ViewModel

// ==================== DANH SÁCH MÀN HÌNH ====================
sealed class MainScreen(
    val route: String,
    val label: String,
    val image: Int
) {
    object SoGiaoDich : MainScreen("trang_chu", "Trang chủ", R.drawable.sogiaodich)
    object ViTien : MainScreen("vi_tien", "Ví tiền", R.drawable.vitien)
    object GiaoDichMoi : MainScreen("giao_dich_moi", "Thêm", R.drawable.themgiaodich)
    object ThongKe : MainScreen("thong_ke", "Thống kê", R.drawable.thongke)
    object CaiDat : MainScreen("cai_dat", "Cài đặt", R.drawable.caidat)
}

@Composable
fun MainApp_Navigation(
    mainNavController: NavController,
    userViewModel: User_ViewModel,
    themeViewModel: ThemeViewModel // <-- THÊM THAM SỐ NÀY
) {
    val navController = rememberNavController()
    val transactionViewModel: Transaction_ViewModel = viewModel()

    val items = listOf(
        MainScreen.SoGiaoDich,
        MainScreen.ViTien,
        MainScreen.GiaoDichMoi,
        MainScreen.ThongKe,
        MainScreen.CaiDat
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF333333),
                contentColor = Color.Gray
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    // === XÓA BỎ HIỆU ỨNG PHÓNG TO ===
                    // val scale by animateFloatAsState( ... )

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { navigateToScreen(navController, screen.route) },
                        icon = {
                            if (screen.route == MainScreen.GiaoDichMoi.route) {
                                // Nút trung tâm "+" (Giữ nguyên)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .background(Color(0xFF3F51B5), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Thêm",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            } else {
                                // Các tab khác (PNG icon)
                                val backgroundColor =
                                    if (isSelected) Color(0xFF3F51B5).copy(alpha = 0.3f)
                                    else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .background(backgroundColor, CircleShape)
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = screen.image),
                                        contentDescription = screen.label,
                                        // === THAY ĐỔI: Tăng kích thước, bỏ hiệu ứng scale ===
                                        modifier = Modifier
                                            .size(28.dp)
                                    )
                                }
                            }
                        },
                        label = {
                            if (screen.route != MainScreen.GiaoDichMoi.route) {
                                Text(
                                    text = screen.label,
                                    // === THAY ĐỔI: Tăng kích thước chữ ===
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFFF7C844) else Color.Gray
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Điều hướng giữa các màn hình con
        NavHost(
            navController,
            startDestination = MainScreen.SoGiaoDich.route,
            Modifier.padding(innerPadding)
        ) {
            composable(MainScreen.SoGiaoDich.route) {
                SoGiaoDichScreen(transactionViewModel, userViewModel)
            }
            composable(MainScreen.ViTien.route) {
                ViTienScreen(transactionViewModel = transactionViewModel)
            }
            composable(MainScreen.GiaoDichMoi.route) {
                GiaoDichMoiScreen(navController,transactionViewModel)
            }
            composable(MainScreen.ThongKe.route) {
                ThongKeScreen(transactionViewModel, userViewModel) // <- SỬA: Thêm userViewModel
            }
            composable(MainScreen.CaiDat.route) {
                // SỬA TẠI ĐÂY: Truyền themeViewModel xuống CaiDatScreen
                CaiDatScreen(
                    userViewModel = userViewModel,
                    themeViewModel = themeViewModel, // <-- TRUYỀN XUỐNG ĐÂY
                    onLogout = {
                        mainNavController.navigate("login") {
                            popUpTo(mainNavController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// ==================== HÀM ĐIỀU HƯỚNG ====================
fun navigateToScreen(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}