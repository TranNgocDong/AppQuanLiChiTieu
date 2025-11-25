package com.example.project_mbp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mbp.R
import com.example.project_mbp.model.Transaction
import com.example.project_mbp.ui.theme.ExpenseRed
import com.example.project_mbp.ui.theme.IncomeGreen
import com.example.project_mbp.ui.theme.TextLight
import java.text.SimpleDateFormat
import java.util.Locale

// === HÀM SỐ 1: TIÊU ĐỀ NGÀY ===
@Composable
fun TransactionGroupHeader(date: String, day: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TextLight.copy(alpha = 0.1f))
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = date, color = TextLight, fontWeight = FontWeight.Bold)
        Text(text = day, color = TextLight.copy(alpha = 0.8f))
    }
}

// === HÀM SỐ 2: HÀNG GIAO DỊCH ===
@Composable
fun TransactionItemRow(transaction: Transaction) {
    val amountColor = if (transaction.loai == "thu") IncomeGreen else ExpenseRed
    val prefix = if (transaction.loai == "thu") "+" else "-"
    val formattedAmount = String.format(Locale.GERMAN, "%,.0f", transaction.soTien)
    val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(transaction.ngayTao)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = TextLight.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = getIconForCategory(transaction.nhom),
                contentDescription = transaction.nhom,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
                    .padding(8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                // 1. Tên Nhóm (Đa ngôn ngữ)
                Text(
                    text = getLocalizedCategoryName(transaction.nhom),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )

                // 2. Phạm vi / Tên giao dịch (Đa ngôn ngữ)
                // SỬA: Dùng hàm getLocalizedScopeName để dịch "Bản thân", "Gia đình"...
                Text(
                    text = getLocalizedScopeName(transaction.tenGiaoDich),
                    color = TextLight.copy(alpha = 0.8f)
                )

                if (transaction.ghiChu.isNotBlank()) {
                    Text(
                        text = "${stringResource(R.string.note_label)} ${transaction.ghiChu}",
                        fontSize = 16.sp,
                        color = TextLight.copy(alpha = 0.7f)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$prefix$formattedAmount Đ",
                    fontWeight = FontWeight.Bold,
                    color = amountColor,
                    fontSize = 16.sp
                )
                Text(
                    text = "${stringResource(R.string.time_label)} $timeStr",
                    fontSize = 16.sp,
                    color = TextLight.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// === HÀM SỐ 3: LẤY ICON ===
@Composable
fun getIconForCategory(category: String): Painter {
    val resId = getCategoryNameResId(category)
    return when (resId) {
        R.string.cat_food -> painterResource(R.drawable.eatdrink)
        R.string.cat_travel -> painterResource(R.drawable.dulich)
        R.string.cat_pets -> painterResource(R.drawable.thucung)
        R.string.cat_water -> painterResource(R.drawable.hoadonnuoc)
        R.string.cat_electric -> painterResource(R.drawable.hoadondien)
        R.string.cat_health -> painterResource(R.drawable.suckhoe)
        R.string.cat_entertainment -> painterResource(R.drawable.giaitri)
        R.string.cat_shopping -> painterResource(R.drawable.muasam)
        R.string.cat_transport -> painterResource(R.drawable.dichuyen)

        R.string.cat_salary -> painterResource(R.drawable.tienluong)
        R.string.cat_bonus -> painterResource(R.drawable.tienthuong)
        R.string.cat_invest -> painterResource(R.drawable.tiendautu)
        R.string.cat_other_income -> painterResource(R.drawable.tienkhac)

        else -> painterResource(R.drawable.avt)
    }
}

// ===================================================================
// === CÁC HÀM HELPER: ÁNH XẠ NGÔN NGỮ (REVERSE MAPPING) ===
// ===================================================================

/**
 * Dịch tên nhóm (Category)
 */
@Composable
fun getLocalizedCategoryName(rawName: String): String {
    val resId = getCategoryNameResId(rawName)
    return if (resId != 0) stringResource(resId) else rawName
}

/**
 * MỚI: Dịch tên phạm vi (Scope)
 * Chuyển "Bản thân" -> "Myself"
 */
@Composable
fun getLocalizedScopeName(rawName: String): String {
    val resId = getScopeNameResId(rawName)
    return if (resId != 0) stringResource(resId) else rawName
}

// --- TỪ ĐIỂN NHÓM (CATEGORY) ---
fun getCategoryNameResId(rawName: String): Int {
    return when (rawName) {
        "Ăn uống", "Food & Dining" -> R.string.cat_food
        "Du lịch", "Travel" -> R.string.cat_travel
        "Thú cưng", "Pets" -> R.string.cat_pets
        "Hóa đơn nước", "Water Bill" -> R.string.cat_water
        "Hóa đơn điện", "Electric Bill" -> R.string.cat_electric
        "Sức khỏe", "Health" -> R.string.cat_health
        "Giải trí", "Entertainment" -> R.string.cat_entertainment
        "Mua sắm", "Shopping" -> R.string.cat_shopping
        "Di chuyển", "Transport", "Transportation" -> R.string.cat_transport
        "Khác", "Other" -> R.string.cat_other

        "Tiền lương", "Salary" -> R.string.cat_salary
        "Tiền thưởng", "Bonus" -> R.string.cat_bonus
        "Tiền đầu tư", "Investment" -> R.string.cat_invest
        "Thu nhập khác", "Other Income" -> R.string.cat_other_income

        else -> 0
    }
}

// --- MỚI: TỪ ĐIỂN PHẠM VI (SCOPE) ---
fun getScopeNameResId(rawName: String): Int {
    return when (rawName) {
        "Bản thân", "Myself" -> R.string.scope_self
        "Gia đình", "Family" -> R.string.scope_family
        "Bạn bè", "Friends" -> R.string.scope_friends
        else -> 0
    }
}