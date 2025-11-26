package com.example.project_mbp.ui.screens

<<<<<<< HEAD
import android.graphics.BitmapFactory // <-- Thêm
import android.util.Base64 // <-- Thêm
import androidx.compose.foundation.Image // <-- Dùng Image thay vì AsyncImage đơn thuần
=======
>>>>>>> main
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.*
<<<<<<< HEAD
import androidx.compose.runtime.*
=======
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
>>>>>>> main
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
import androidx.compose.ui.graphics.asImageBitmap // <-- Thêm
import androidx.compose.ui.graphics.painter.BitmapPainter // <-- Thêm
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter // <-- Dùng cái này
import com.example.project_mbp.R
=======
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_mbp.model.Transaction
>>>>>>> main
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import com.example.project_mbp.viewmodel.User_ViewModel
import java.text.SimpleDateFormat
import java.util.*
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.project_mbp.ui.components.TransactionGroupHeader
import com.example.project_mbp.ui.components.TransactionItemRow
<<<<<<< HEAD
import kotlin.math.max
=======

// === SỬA LỖI 1: CHỈ IMPORT 'max' VÀ 'ceil' ===
import kotlin.math.ceil
import kotlin.math.max // <-- Dùng 'max' thay vì 'maxOf'
>>>>>>> main

@Composable
fun SoGiaoDichScreen(
    transactionViewModel: Transaction_ViewModel,
    userViewModel: User_ViewModel
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    val incomeColor = MaterialTheme.colorScheme.tertiary
    val expenseColor = MaterialTheme.colorScheme.error
    val chartBackgroundColor = MaterialTheme.colorScheme.surface
    val onChartColor = MaterialTheme.colorScheme.onSurface

<<<<<<< HEAD
    val currentLocale = Locale.getDefault()
    val isVietnamese = currentLocale.language == "vi"

=======
>>>>>>> main
    val calendar = Calendar.getInstance()
    val monthlyData = transactions.groupBy {
        calendar.time = it.ngayTao
        calendar.get(Calendar.MONTH) + 1
    }

    val monthLabels = (1..12).map { "T$it" }
    val incomePoints = mutableListOf<Point>()
    val expensePoints = mutableListOf<Point>()
    var actualMax = 0f

    (1..12).forEachIndexed { index, month ->
        val monthData = monthlyData[month] ?: emptyList()
        val totalIncome = monthData.filter { it.loai == "thu" }.sumOf { it.soTien }.toFloat()
        val totalExpense = monthData.filter { it.loai == "chi" }.sumOf { it.soTien }.toFloat()

        incomePoints.add(Point(index.toFloat(), totalIncome))
        expensePoints.add(Point(index.toFloat(), totalExpense))

<<<<<<< HEAD
        actualMax = max(actualMax, max(totalIncome, totalExpense))
    }

    val steps = 4
    if (actualMax == 0f) actualMax = 1000f

    val yAxisLabels = (0..steps).map {
        val value = (actualMax / steps) * it
        formatLabel(value, isVietnamese)
=======
        // SỬA LỖI 1: Dùng hàm max() lồng nhau
        actualMax = max(actualMax, max(totalIncome, totalExpense))
    }

    // === SỬA LỖI 2: TÍNH TOÁN TRỤC Y TỰ ĐỘNG (KHÔNG DÙNG 20M) ===
    val steps = 4 // Số bậc (5 mốc)
    if (actualMax == 0f) actualMax = 1000f // Đặt giá trị nhỏ nhất nếu không có dữ liệu

    // Tính toán mốc dựa trên giá trị cao nhất thực tế
    val yAxisLabels = (0..steps).map {
        val value = (actualMax / steps) * it
        formatLabel(value)
>>>>>>> main
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps(monthLabels.size - 1)
        .labelData { index -> monthLabels.getOrNull(index) ?: "" }
        .axisLabelColor(onChartColor.copy(alpha = 0.7f))
        .axisLineColor(onChartColor.copy(alpha = 0.3f))
        .bottomPadding(0.dp)
        .build()

<<<<<<< HEAD
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelData { index -> yAxisLabels[index] }
        .axisLabelColor(onChartColor.copy(alpha = 0.7f))
        .axisLineColor(onChartColor.copy(alpha = 0.3f))
        .bottomPadding(0.dp)
        .build()
=======
    // === SỬA LỖI 2: BỎ TẤT CẢ CÁC THAM SỐ 'dataMaxValue' / 'axisMaxValue' ... ===
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelData { index -> yAxisLabels[index] } // Dùng mốc tự động
        .axisLabelColor(onChartColor.copy(alpha = 0.7f))
        .axisLineColor(onChartColor.copy(alpha = 0.3f))
        .bottomPadding(0.dp)
        .build() // Xóa .dataMaxValue(maxYValue)
>>>>>>> main

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
<<<<<<< HEAD
                Line(
                    dataPoints = incomePoints,
                    lineStyle = LineStyle(color = incomeColor, lineType = LineType.SmoothCurve()),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(listOf(incomeColor.copy(alpha = 0.5f), Color.Transparent)),
                        alpha = 1f
                    )
                ),
                Line(
                    dataPoints = expensePoints,
                    lineStyle = LineStyle(color = expenseColor, lineType = LineType.SmoothCurve()),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(listOf(expenseColor.copy(alpha = 0.5f), Color.Transparent)),
=======
                // Đường 1: Thu nhập
                Line(
                    dataPoints = incomePoints,
                    lineStyle = LineStyle(
                        color = incomeColor,
                        lineType = LineType.SmoothCurve()
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(
                            listOf(
                                incomeColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        alpha = 1f
                    )
                ),
                // Đường 2: Chi tiêu
                Line(
                    dataPoints = expensePoints,
                    lineStyle = LineStyle(
                        color = expenseColor,
                        lineType = LineType.SmoothCurve()
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(
                            listOf(
                                expenseColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
>>>>>>> main
                        alpha = 1f
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(Color.Transparent),
        backgroundColor = Color.Transparent,
        paddingRight = 0.dp,
        bottomPadding = 0.dp
<<<<<<< HEAD
    )

    val groupedTransactions = transactions.groupBy {
        val dateStr = SimpleDateFormat("dd/MM/yyyy, EEEE", currentLocale).format(it.ngayTao)
        dateStr.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(currentLocale) else char.toString() }
=======
        // Xóa 'maxYValue' ở đây
    )

    val groupedTransactions = transactions.groupBy {
        SimpleDateFormat("dd/MM/yyyy, EEEE", Locale("vi", "VN")).format(it.ngayTao)
>>>>>>> main
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
<<<<<<< HEAD
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {

                // ================== PHẦN HIỂN THỊ ẢNH (ĐÃ SỬA) ==================
                val avatarBitmap = rememberBase64(currentUser?.avatarUrl)

                Image(
                    painter = if (avatarBitmap != null) {
                        BitmapPainter(avatarBitmap)
                    } else {
                        rememberAsyncImagePainter(
                            model = currentUser?.avatarUrl ?: R.drawable.avt
                        )
                    },
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                // ===============================================================

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = stringResource(R.string.hi), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        text = currentUser?.name ?: stringResource(R.string.loading),
=======
        // ... (Phần còn lại của LazyColumn: Chào mừng, Biểu đồ, Danh sách) ...
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = currentUser?.avatarUrl ?: "",
                    contentDescription = "Avatar",
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Hi,", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        text = currentUser?.name ?: "Đang tải...",
>>>>>>> main
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(chartBackgroundColor, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Square, null, tint = incomeColor, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
<<<<<<< HEAD
                    Text(stringResource(R.string.income), color = onChartColor, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Square, null, tint = expenseColor, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.expense), color = onChartColor, fontSize = 12.sp)
=======
                    Text("Thu nhập", color = onChartColor, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Square, null, tint = expenseColor, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chi tiêu", color = onChartColor, fontSize = 12.sp)
>>>>>>> main
                }

                Spacer(modifier = Modifier.height(8.dp))

<<<<<<< HEAD
                if (actualMax > 0) {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        lineChartData = lineChartData
                    )
                }
=======
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    lineChartData = lineChartData
                )
>>>>>>> main
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        groupedTransactions.forEach { (dateStr, transactionsInGroup) ->
            item {
                val parts = dateStr.split(", ")
                val date = parts.getOrNull(0) ?: "..."
                val dayOfWeek = parts.getOrNull(1) ?: "..."
                TransactionGroupHeader(date = date, day = dayOfWeek)
            }
            items(transactionsInGroup) { transaction ->
                TransactionItemRow(transaction = transaction)
            }
        }
    }
}

<<<<<<< HEAD
// === HÀM FORMAT ĐA NGÔN NGỮ ===
private fun formatLabel(value: Float, isVietnamese: Boolean): String {
    val num = value.toDouble()
    val billionSuffix = if (isVietnamese) "T" else "B"
    val millionSuffix = if (isVietnamese) "Tr" else "M"
    val thousandSuffix = "K"

    return when {
        num >= 1_000_000_000 -> String.format(
            Locale.US,
            "%.1f%s",
            num / 1_000_000_000,
            billionSuffix
        )

        num >= 1_000_000 -> String.format(Locale.US, "%.1f%s", num / 1_000_000, millionSuffix)
        num >= 1_000 -> String.format(Locale.US, "%.0f%s", num / 1_000, thousandSuffix)
=======
// === HÀM FORMAT (Giữ nguyên) ===
private fun formatLabel(value: Float): String {
    val num = value.toDouble()
    return when {
        num >= 1_000_000_000 -> String.format(Locale.US, "%.1fB", num / 1_000_000_000)
        num >= 1_000_000 -> String.format(Locale.US, "%.1fM", num / 1_000_000)
        num >= 1_000 -> String.format(Locale.US, "%.0fK", num / 1_000)
>>>>>>> main
        else -> String.format(Locale.US, "%.0f", num)
    }.replace(".0", "")
}