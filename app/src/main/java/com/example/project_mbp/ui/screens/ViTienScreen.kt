package com.example.project_mbp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.project_mbp.animations.FadeDeleteAnimation
import com.example.project_mbp.model.Transaction
import com.example.project_mbp.ui.components.getIconForCategory
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import java.util.Locale

@Composable
fun ViTienScreen(
    transactionViewModel: Transaction_ViewModel
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    val totalIncome = transactions.filter { it.loai == "thu" }.sumOf { it.soTien }
    val totalExpense = transactions.filter { it.loai == "chi" }.sumOf { it.soTien }
    val currentBalance = totalIncome - totalExpense

    val incomeGoal = 60_000_000.0
    val expenseGoal = 10_000_000.0
    val incomeProgress = (totalIncome / incomeGoal).toFloat().coerceIn(0f, 1f)
    val expenseProgress = (totalExpense / expenseGoal).toFloat().coerceIn(0f, 1f)

    val formatter = String.format(Locale.GERMAN, "%,.0f", currentBalance)
    val incomeStr = String.format(Locale.GERMAN, "+%,.0f", totalIncome)
    val expenseStr = String.format(Locale.GERMAN, "-%,.0f", totalExpense)

    val incomeColor = MaterialTheme.colorScheme.tertiary
    val expenseColor = MaterialTheme.colorScheme.error

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // --- Phần 1: Thẻ tóm tắt tài khoản ---
        item {
            AccountSummaryCard(
                totalBalance = "$formatter VND",
                income = "$incomeStr VND",
                incomeProgress = incomeProgress,
                expense = "$expenseStr VND",
                expenseProgress = expenseProgress,
                incomeColor = incomeColor,
                expenseColor = expenseColor
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ---Phần 2: Danh sách giao dịch ---
        //thêm key, thêm animation tan biến khi xóa xong
        //key = { it.id } dùng key để theo dõi item duy nhất, tránh compose nhầm item
        items(transactions, key = { it.id }) { transaction ->
            var visible by remember { mutableStateOf(true) } // ban đầu cho iteam = true, item hiển thị bình thường

            //khi visible = false, animation fade out
            FadeDeleteAnimation(
                visible = visible,
                onAnimationFinish = { transactionViewModel.deleteTransaction(transaction.id) }
            ){
                SimpleTransactionItemRow(
                    transaction = transaction,
                    incomeColor = incomeColor,
                    expenseColor = expenseColor,
                    //khi visible = false thì nó sẽ không xóa liền mà nó sẽ tan biến từ từ
                    //tiếp đó onAnimationFinish mới xóa hoàn toàn
                    onDeleteClick = { visible = false } // Chỉ cần set visible = false
                )
            }
        }
    }
}


        @Composable
fun AccountSummaryCard(
    totalBalance: String,
    income: String,
    incomeProgress: Float,
    expense: String,
    expenseProgress: Float,
    incomeColor: Color,
    expenseColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = "Tài khoản của tôi", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        Text(text = totalBalance, fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(20.dp))

        // --- Thu nhập ---
        Text(text = "Thu nhập", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = income, fontSize = 18.sp, color = incomeColor)
        Spacer(modifier = Modifier.height(4.dp))
        //=== SỬA: DÙNG BOX THAY VÌ LinearProgressIndicator ===
        CustomProgressBar(progress = incomeProgress, progressColor = incomeColor)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Chi tiêu ---
        Text(text = "Chi tiêu", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = expense,
                fontSize = 18.sp,
                color = expenseColor,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // === SỬA: DÙNG BOX THAY VÌ LinearProgressIndicator ===
        CustomProgressBar(
            progress = expenseProgress,
            progressColor = expenseColor
        )
    }
}

/** Thanh tiến độ tùy chỉnh bằng Box, liền mạch, không "đứt" **/
@Composable
private fun CustomProgressBar(
    progress: Float,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val cornerRadius = 8.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(trackColor, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(
                    color = progressColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        )
    }
}

/** --- SimpleTransactionItemRow có thêm nút Xóa --- **/
@Composable
fun SimpleTransactionItemRow(
    transaction: Transaction,
    incomeColor: Color,
    expenseColor: Color,
    //-_-
    onDeleteClick: (String) -> Unit
) {
    val amountColor = if (transaction.loai == "thu") incomeColor else expenseColor
    val prefix = if (transaction.loai == "thu") "+" else "-"
    val formattedAmount = String.format(Locale.GERMAN, "%,.0f", transaction.soTien)

    var showDialog by remember { mutableStateOf(false) }

    //Thêm dialog để xác nhận lần nữa có muốn xóa không
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xóa giao dịch") },
            text = { Text("Bạn có chắc muốn xóa giao dịch này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick(transaction.id)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = getIconForCategory(transaction.nhom),
            contentDescription = transaction.nhom,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(8.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.nhom,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = transaction.tenGiaoDich,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

        Text(
            text = "$prefix$formattedAmount Đ",
            fontWeight = FontWeight.Bold,
            color = amountColor,
            fontSize = 18.sp
        )
        //thêm thêm nút xóa -_-
        IconButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
        }
    }
}
