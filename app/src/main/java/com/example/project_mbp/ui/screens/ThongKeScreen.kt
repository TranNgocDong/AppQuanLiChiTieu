package com.example.project_mbp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mbp.R
import com.example.project_mbp.ui.components.getCategoryNameResId
import com.example.project_mbp.ui.components.getLocalizedCategoryName
import com.example.project_mbp.ui.components.getLocalizedScopeName
import com.example.project_mbp.ui.components.getScopeNameResId
import com.example.project_mbp.ui.theme.ExpenseRed
import com.example.project_mbp.ui.theme.IncomeGreen
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import com.example.project_mbp.viewmodel.User_ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongKeScreen(
    transactionViewModel: Transaction_ViewModel,
    userViewModel: User_ViewModel
) {
    // === 1. LẤY DỮ LIỆU ĐA NGÔN NGỮ ===
    val context = LocalContext.current
    val monthListDynamic = getMonthList()
    val expenseCats = getExpenseCategories()
    val incomeCats = getIncomeCategories()
    val allString = stringResource(R.string.all)

    val allTransactions by transactionViewModel.transactions.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.expense), stringResource(R.string.income))
    val selectedType = if (selectedTabIndex == 0) "chi" else "thu"

    // === CẤU HÌNH UI ===
    val isDark = isSystemInDarkTheme()
    val screenBackgroundColor = MaterialTheme.colorScheme.background
    val mainTextColor = MaterialTheme.colorScheme.onBackground
    val cardBackgroundColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White.copy(alpha = 0.1f)
    val onCardTextColor = mainTextColor
    val selectedColor = if (selectedType == "chi") ExpenseRed else IncomeGreen
    val barChartColor = if (isDark) MaterialTheme.colorScheme.secondary else Color(0xFFF7C844)
    val pieChartStrokeColor = screenBackgroundColor.copy(alpha = 0.5f)
    val gridLineColor = mainTextColor.copy(alpha = 0.2f)

    // === CẤU HÌNH NGÀY THÁNG & BỘ LỌC ===
    val currentCalendar = remember { Calendar.getInstance() }
    val currentMonthIndex = currentCalendar.get(Calendar.MONTH)
    val currentYear = currentCalendar.get(Calendar.YEAR).toString()
    val yearList = remember {
        (currentCalendar.get(Calendar.YEAR) - 2..currentCalendar.get(Calendar.YEAR) + 1).map { it.toString() }
    }

    val monthFilterList_Pie = remember(monthListDynamic) { listOf(allString) + monthListDynamic }
    val monthFilterList_Bar = monthListDynamic

    val categoryList = (if (selectedType == "chi") expenseCats else incomeCats)
    val categoriesWithAll = remember(categoryList) { listOf(allString) + categoryList }

    var barSelectedCategory by remember { mutableStateOf(categoriesWithAll.firstOrNull() ?: allString) }
    var barSelectedMonthString by remember {
        mutableStateOf(monthFilterList_Bar.getOrElse(currentMonthIndex) { "" })
    }
    var barSelectedYear by remember { mutableStateOf(currentYear) }
    var pieSelectedMonthString by remember { mutableStateOf(monthFilterList_Pie[0]) }
    var pieSelectedYear by remember { mutableStateOf(currentYear) }

    LaunchedEffect(selectedTabIndex, categoriesWithAll) {
        if (categoriesWithAll.isNotEmpty()) {
            barSelectedCategory = categoriesWithAll[0]
        }
    }

    // === LOGIC LỌC DỮ LIỆU ===
    val pieMonthIndex = monthListDynamic.indexOf(pieSelectedMonthString)
    val isPieAllMonths = pieSelectedMonthString == allString

    val pieFilteredTransactions = allTransactions.filter { transaction ->
        val transactionCalendar = Calendar.getInstance().apply { time = transaction.ngayTao }
        val transactionYear = transactionCalendar.get(Calendar.YEAR).toString()
        val transactionMonth = transactionCalendar.get(Calendar.MONTH)
        val monthMatch = if (isPieAllMonths) true else (transactionMonth == pieMonthIndex)
        transaction.loai == selectedType && transactionYear == pieSelectedYear && monthMatch
    }

    // === LOGIC NHÓM DỮ LIỆU (PIE CHART) ===
    val categoryData = pieFilteredTransactions
        .groupBy { transaction ->
            if (selectedType == "chi") {
                // Nếu là Chi tiêu -> Nhóm theo "Phạm vi" (Tên giao dịch)
                // Chuẩn hóa tên trước khi nhóm để gộp "Myself" và "Bản thân"
                val resId = getScopeNameResId(transaction.tenGiaoDich)
                if (resId != 0) context.getString(resId) else transaction.tenGiaoDich
            } else {
                // Nếu là Thu nhập -> Nhóm theo "Danh mục"
                val resId = getCategoryNameResId(transaction.nhom)
                if (resId != 0) context.getString(resId) else transaction.nhom
            }
        }
        .mapValues { entry -> entry.value.sumOf { it.soTien } }

    val totalAmount = pieFilteredTransactions.sumOf { it.soTien }

    // === COMPOSABLES CON ===
    @Composable
    fun EmptyChart() {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_data), color = onCardTextColor.copy(alpha = 0.7f))
        }
    }

    @Composable
    fun StatisticsCard(title: String, content: @Composable () -> Unit) {
        Surface(
            color = cardBackgroundColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(text = title, color = onCardTextColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }

    @Composable
    fun LegendItem(category: String, color: Color) {
        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(color, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = category, color = onCardTextColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }

    @Composable
    fun FilterDropdown(
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }
        val dropdownBgColor = if (isDark)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        else
            Color.Black.copy(alpha = 0.05f)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            Surface(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = dropdownBgColor,
                border = if (!isDark) androidx.compose.foundation.BorderStroke(0.5.dp, Color.Gray.copy(alpha = 0.3f)) else null
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedOption,
                        color = onCardTextColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = onCardTextColor.copy(alpha = 0.7f)
                    )
                }
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp) },
                        onClick = { onOptionSelected(option); expanded = false }
                    )
                }
            }
        }
    }

    @Composable
    fun PieChart(data: Map<String, Double>, strokeWidth: Float = 70f) {
        val total = data.values.sum()
        if (total == 0.0) return
        val angles = data.values.map { (it / total * 360).toFloat() }
        val percentages = data.values.map { (it / total * 100).toFloat() }
        val colors = data.keys.map { getColorForCategory(it) }
        val density = LocalDensity.current
        val textPaint = remember(density) {
            Paint().asFrameworkPaint().apply {
                color = onCardTextColor.toArgb()
                textSize = with(density) { 14.sp.toPx() }
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
        }

        Canvas(modifier = Modifier.size(180.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.width / 2
            val innerRadius = outerRadius - strokeWidth
            val textRadius = innerRadius + (strokeWidth / 2) * 0.7f
            var startAngle = -90f
            val stroke = Stroke(width = strokeWidth)

            angles.forEachIndexed { index, angle ->
                drawArc(color = colors[index], startAngle = startAngle, sweepAngle = angle, useCenter = false, style = stroke)
                drawArc(color = pieChartStrokeColor, startAngle = startAngle, sweepAngle = angle, useCenter = false, style = Stroke(width = strokeWidth + 2f))

                if (percentages[index] > 0) {
                    val medianAngle = (startAngle + angle / 2) * (Math.PI / 180.0)
                    val x = center.x + (textRadius * cos(medianAngle)).toFloat()
                    val y = center.y + (textRadius * sin(medianAngle)).toFloat()
                    drawIntoCanvas { it.nativeCanvas.drawText("${percentages[index].roundToInt()}%", x, y - (textPaint.descent() + textPaint.ascent()) / 2, textPaint) }
                }
                startAngle += angle
            }
        }
    }

    @Composable
    fun YAxisLabels_Daily(modifier: Modifier = Modifier, maxAmount: Double) {
        val labels = listOf(formatAmountShort(maxAmount), formatAmountShort(maxAmount * 0.5))
        Column(modifier = modifier.width(50.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
            labels.forEach { Text(text = it, fontSize = 12.sp, color = onCardTextColor.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis) }
            Text(text = "0", fontSize = 12.sp, color = onCardTextColor.copy(alpha = 0.7f))
        }
    }

    @Composable
    fun RowScope.BarChartItem_Daily(value: Float, label: String, isVisible: Boolean, modifier: Modifier = Modifier) {
        Column(modifier = modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
            Box(modifier = Modifier.fillMaxWidth(0.8f).weight(1f), contentAlignment = Alignment.BottomCenter) {
                Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(fraction = value.coerceIn(0.0f, 1.0f))
                    .background(color = if (isVisible) barChartColor else Color.Transparent, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
            }
            Spacer(Modifier.height(4.dp))
            Text(text = label, color = if (isVisible) onCardTextColor else onCardTextColor.copy(alpha = 0.4f), fontSize = 11.sp)
        }
    }

    @Composable
    fun BarChart_Daily(data: List<Pair<Int, Double>>, selectedMonthIndex: Int, selectedYear: String, maxAmount: Double, chartHeight: Dp = 200.dp) {
        val monthAbbreviation = getMonthAbbreviation(selectedMonthIndex)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, selectedMonthIndex)
            set(Calendar.YEAR, selectedYear.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR))
        }
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dataMap = data.toMap()

        Column(modifier = Modifier.fillMaxWidth().height(chartHeight)) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.Bottom) {
                YAxisLabels_Daily(modifier = Modifier.fillMaxHeight().padding(end = 4.dp), maxAmount = maxAmount)
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(gridLineColor))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(gridLineColor))
                        Box(modifier = Modifier.fillMaxWidth().height(0.dp))
                    }
                    Row(modifier = Modifier.fillMaxHeight().horizontalScroll(rememberScrollState()), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..daysInMonth).forEach { day ->
                            val amount = dataMap[day] ?: 0.0
                            BarChartItem_Daily(value = (amount / maxAmount).toFloat().coerceIn(0f, 1f), label = "$day${monthAbbreviation}", isVisible = amount > 0.0, modifier = Modifier.width(35.dp))
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(onCardTextColor.copy(alpha = 0.3f)).padding(start = 50.dp))
        }
    }

    // === GIAO DIỆN CHÍNH ===
    Column(
        modifier = Modifier.fillMaxSize().background(screenBackgroundColor).verticalScroll(rememberScrollState())
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = screenBackgroundColor,
            contentColor = mainTextColor,
            indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]), color = selectedColor, height = 3.dp) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (selectedTabIndex == index) selectedColor else mainTextColor.copy(alpha = 0.7f))
                    }
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            val totalPrefix = if (selectedType == "chi") stringResource(R.string.total_prefix_expense) else stringResource(R.string.total_prefix_income)
            val amountPrefix = if (selectedType == "chi") "-" else "+"
            val yearString = stringResource(R.string.year)
            val totalLabel = if (isPieAllMonths) "$yearString $pieSelectedYear" else "$pieSelectedMonthString $pieSelectedYear"

            Text(text = "$totalPrefix ($totalLabel)", color = mainTextColor.copy(alpha = 0.8f), fontSize = 18.sp)

            // === SỬA DÒNG NÀY: HIỂN THỊ TỔNG TIỀN VỚI FORMAT CHUẨN ===
            val isVietnamese = Locale.getDefault().language == "vi"
            val currencySuffix = if (isVietnamese) " Đ" else " VND"
            Text(text = "${amountPrefix}${formatAmount(totalAmount)}$currencySuffix", color = selectedColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            // ==========================================================

            Spacer(modifier = Modifier.height(24.dp))

            StatisticsCard(title = stringResource(R.string.statistics_card_title, tabs[selectedTabIndex])) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterDropdown(
                        options = categoriesWithAll,
                        selectedOption = barSelectedCategory,
                        onOptionSelected = { barSelectedCategory = it },
                        modifier = Modifier.weight(1f)
                    )
                    FilterDropdown(
                        options = monthFilterList_Bar,
                        selectedOption = barSelectedMonthString,
                        onOptionSelected = { barSelectedMonthString = it },
                        modifier = Modifier.weight(1.3f)
                    )
                    FilterDropdown(
                        options = yearList,
                        selectedOption = barSelectedYear,
                        onOptionSelected = { barSelectedYear = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                val barMonthIndex = monthListDynamic.indexOf(barSelectedMonthString)

                val dailyData = allTransactions.filter { transaction ->
                    val calendar = Calendar.getInstance().apply { time = transaction.ngayTao }
                    val categoryMatch = if (barSelectedCategory == allString) true else (transaction.nhom == barSelectedCategory)
                    transaction.loai == selectedType && calendar.get(Calendar.YEAR).toString() == barSelectedYear && calendar.get(Calendar.MONTH) == barMonthIndex && categoryMatch
                }.groupBy {
                    Calendar.getInstance().apply { time = it.ngayTao }.get(Calendar.DAY_OF_MONTH)
                }.mapValues { entry -> entry.value.sumOf { it.soTien } }.toList().sortedBy { it.first }
                val dailyMaxAmount = dailyData.maxOfOrNull { it.second } ?: 1.0

                if (dailyData.isEmpty()) EmptyChart()
                else BarChart_Daily(data = dailyData, selectedMonthIndex = barMonthIndex, selectedYear = barSelectedYear, maxAmount = dailyMaxAmount)
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatisticsCard(title = stringResource(R.string.compare_card_title, tabs[selectedTabIndex])) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    FilterDropdown(options = monthFilterList_Pie, selectedOption = pieSelectedMonthString, onOptionSelected = { pieSelectedMonthString = it }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterDropdown(options = yearList, selectedOption = pieSelectedYear, onOptionSelected = { pieSelectedYear = it }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
                        if (categoryData.isEmpty()) Text(stringResource(R.string.no_data), color = onCardTextColor.copy(alpha = 0.7f))
                        else PieChart(data = categoryData)
                    }
                    Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                        if (totalAmount == 0.0) Text(stringResource(R.string.no_data), color = onCardTextColor.copy(alpha = 0.7f))
                        else categoryData.entries.toList().sortedByDescending { it.value }.forEach { (keyName, amount) ->
                            // Tên đã được dịch ở bước groupBy, ở đây chỉ cần hiển thị
                            LegendItem(category = keyName, color = getColorForCategory(keyName))
                        }
                    }
                }
            }
        }
    }
}

// === CÁC HÀM HELPER Ở CUỐI FILE ===

private fun getColorForCategory(category: String): Color {
    val hue = category.hashCode().toLong().absoluteValue % 360f
    return Color.hsv(hue, 0.75f, 0.85f)
}

// === SỬA: Format số tiền (Long) cho Tổng tiền ===
private fun formatAmount(amount: Double): String {
    val isVietnamese = Locale.getDefault().language == "vi"
    val formatLocale = if (isVietnamese) Locale.GERMAN else Locale.US
    return String.format(formatLocale, "%,.0f", amount)
}

// === SỬA: Format số tiền ngắn gọn (Trục Y) ===
private fun formatAmountShort(amount: Double): String {
    val isVietnamese = Locale.getDefault().language == "vi"

    // Định nghĩa hậu tố
    val billionSuffix = if (isVietnamese) "T" else "B"
    val millionSuffix = if (isVietnamese) "Tr" else "M"
    val thousandSuffix = "K"

    // Locale để định dạng số (1.5 vs 1,5)
    val formatLocale = if (isVietnamese) Locale.GERMAN else Locale.US

    return when {
        amount >= 1_000_000_000 -> String.format(formatLocale, "%.1f%s", amount / 1_000_000_000, billionSuffix)
        amount >= 1_000_000 -> String.format(formatLocale, "%.1f%s", amount / 1_000_000, millionSuffix)
        amount >= 1_000 -> String.format(formatLocale, "%.0f%s", amount / 1_000, thousandSuffix)
        else -> String.format(formatLocale, "%.0f", amount)
    }.replace(".0", "")
}

// === SỬA: Format tên tháng trục X (T1 hoặc Jan) ===
private fun getMonthAbbreviation(monthIndex: Int): String {
    val currentLocale = Locale.getDefault()
    return if (currentLocale.language == "vi") {
        // Tiếng Việt: T1, T2...
        " T${monthIndex + 1}"
    } else {
        // Tiếng Anh/Khác: Jan, Feb...
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthIndex)
        SimpleDateFormat("MMM", currentLocale).format(calendar.time)
    }
}