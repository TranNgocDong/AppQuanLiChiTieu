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
import androidx.compose.foundation.shape.RoundedCornerShape // <-- Thêm import
import androidx.compose.material3.Surface // <-- Thêm import
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

// === HÀM SỐ 1: TIÊU ĐỀ NGÀY (Không đổi) ===
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

// === HÀM SỐ 2: HÀNG GIAO DỊCH (Đã bọc trong "Box") ===
@Composable
fun TransactionItemRow(transaction: Transaction) {
    val amountColor = if (transaction.loai == "thu") IncomeGreen else ExpenseRed
    val prefix = if (transaction.loai == "thu") "+" else "-"
    val formattedAmount = String.format(Locale.GERMAN, "%,.0f", transaction.soTien)
    val timeStr = SimpleDateFormat("HH:mm:ss", Locale("vi", "VN")).format(transaction.ngayTao)

    // === THAY ĐỔI: BỌC TRONG SURFACE (Box) ===
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Thêm khoảng cách giữa các thẻ
        color = TextLight.copy(alpha = 0.1f), // Màu nền của thẻ (box)
        shape = RoundedCornerShape(12.dp) // Bo góc
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp), // Padding bên trong thẻ
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
                Text(text = transaction.nhom, fontSize = 20.sp,fontWeight = FontWeight.Bold, color = TextLight)
                Text(text = transaction.tenGiaoDich,color = TextLight.copy(alpha = 0.8f))
                if (transaction.ghiChu.isNotBlank()) {
                    Text(text = "Ghi chú: ${transaction.ghiChu}", fontSize = 16.sp, color = TextLight.copy(alpha = 0.7f))
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
                    text = "Giờ: $timeStr",
                    fontSize = 16.sp,
                    color = TextLight.copy(alpha = 0.8f)
                )
            }
        }
    }
    // ===================================
}

// === HÀM SỐ 3: LẤY ICON (Không đổi) ===
@Composable
fun getIconForCategory(category: String): Painter {
    return when (category) {
        "Ăn uống", "Thực phẩm" -> painterResource(R.drawable.eatdrink)
        "Du lịch" -> painterResource(R.drawable.dulich)
        "Tiền lương" -> painterResource(R.drawable.tienluong)
        "Thú cưng" -> painterResource(R.drawable.thucung)
        "Hóa đơn nước" -> painterResource(R.drawable.hoadonnuoc)
        "Hóa đơn điện" -> painterResource(R.drawable.hoadondien)
        "Sức khỏe" -> painterResource(R.drawable.suckhoe)
        "Giải trí" -> painterResource(R.drawable.giaitri)
        "Mua sắm" -> painterResource(R.drawable.muasam)
        "Di chuyển" -> painterResource(R.drawable.dichuyen)
        else -> painterResource(R.drawable.avt)
    }
}