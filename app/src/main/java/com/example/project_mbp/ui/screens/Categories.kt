package com.example.project_mbp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.project_mbp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Hàm lấy danh sách chi tiêu (Đa ngôn ngữ)
 */
@Composable
fun getExpenseCategories(): List<String> {
    return listOf(
        stringResource(R.string.cat_food),
        stringResource(R.string.cat_travel),
        stringResource(R.string.cat_pets),
        stringResource(R.string.cat_water),
        stringResource(R.string.cat_electric),
        stringResource(R.string.cat_health),
        stringResource(R.string.cat_entertainment),
        stringResource(R.string.cat_shopping),
        stringResource(R.string.cat_transport),
        stringResource(R.string.cat_other)
    )
}

/**
 * Hàm lấy danh sách thu nhập (Đa ngôn ngữ)
 */
@Composable
fun getIncomeCategories(): List<String> {
    return listOf(
        stringResource(R.string.cat_salary),
        stringResource(R.string.cat_bonus),
        stringResource(R.string.cat_invest),
        stringResource(R.string.cat_other_income)
    )
}

/**
 * Hàm lấy danh sách phạm vi (Đa ngôn ngữ)
 */
@Composable
fun getScopeCategories(): List<String> {
    return listOf(
        stringResource(R.string.scope_self),
        stringResource(R.string.scope_family),
        stringResource(R.string.scope_friends)
    )
}

// === DANH SÁCH CHO BỘ LỌC THỐNG KÊ ===

val currentCalendar: Calendar = Calendar.getInstance()

// Năm thì không cần dịch, giữ nguyên logic
val yearList = (currentCalendar.get(Calendar.YEAR) - 2..currentCalendar.get(Calendar.YEAR) + 1).map { it.toString() }

/**
 * Hàm lấy danh sách tháng tự động theo Locale
 * - Tiếng Anh: January, February...
 * - Tiếng Việt: Tháng 1, Tháng 2...
 */
@Composable
fun getMonthList(): List<String> {
    val list = mutableListOf<String>()
    val cal = Calendar.getInstance()
    // "MMMM" là định dạng tên tháng đầy đủ
    val fmt = SimpleDateFormat("MMMM", Locale.getDefault())

    for (i in 0..11) {
        cal.set(Calendar.MONTH, i)
        // fmt.format sẽ trả về "January" hoặc "tháng 1"
        // replaceFirstChar để viết hoa chữ cái đầu (quan trọng cho Tiếng Việt vì nó thường trả về chữ thường)
        val monthName = fmt.format(cal.time).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        list.add(monthName)
    }
    return list
}