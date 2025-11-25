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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_mbp.R
import com.example.project_mbp.animations.FadeDeleteAnimation
import com.example.project_mbp.model.Transaction
import com.example.project_mbp.ui.components.getIconForCategory
import com.example.project_mbp.ui.components.getLocalizedCategoryName // Hàm dịch Nhóm
import com.example.project_mbp.ui.components.getLocalizedScopeName    // Hàm dịch Phạm vi
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

    // === SỬA: ĐỊNH DẠNG TIỀN TỆ ĐA NGÔN NGỮ ===
    val isVietnamese = Locale.getDefault().language == "vi"
    val formatLocale = if (isVietnamese) Locale.GERMAN else Locale.US
    val currencySuffix = if (isVietnamese) " Đ" else " VND"

    val formatter = String.format(formatLocale, "%,.0f", currentBalance)
    val incomeStr = String.format(formatLocale, "+%,.0f", totalIncome)
    val expenseStr = String.format(formatLocale, "-%,.0f", totalExpense)
    // ==========================================

    val incomeColor = MaterialTheme.colorScheme.tertiary
    val expenseColor = MaterialTheme.colorScheme.error

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        item {
            AccountSummaryCard(
                totalBalance = "$formatter$currencySuffix",
                income = "$incomeStr$currencySuffix",
                incomeProgress = incomeProgress,
                expense = "$expenseStr$currencySuffix",
                expenseProgress = expenseProgress,
                incomeColor = incomeColor,
                expenseColor = expenseColor
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        items(transactions, key = { it.id }) { transaction ->
            var visible by remember { mutableStateOf(true) }

            FadeDeleteAnimation(
                visible = visible,
                onAnimationFinish = { transactionViewModel.deleteTransaction(transaction.id) }
            ){
                SimpleTransactionItemRow(
                    transaction = transaction,
                    incomeColor = incomeColor,
                    expenseColor = expenseColor,
                    onDeleteClick = { visible = false }
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
        Text(text = stringResource(R.string.my_account), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        Text(text = totalBalance, fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = stringResource(R.string.income), fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = income, fontSize = 18.sp, color = incomeColor)
        Spacer(modifier = Modifier.height(4.dp))
        CustomProgressBar(progress = incomeProgress, progressColor = incomeColor)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.expense), fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = expense,
                fontSize = 18.sp,
                color = expenseColor,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        CustomProgressBar(
            progress = expenseProgress,
            progressColor = expenseColor
        )
    }
}

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

@Composable
fun SimpleTransactionItemRow(
    transaction: Transaction,
    incomeColor: Color,
    expenseColor: Color,
    onDeleteClick: (String) -> Unit
) {
    val amountColor = if (transaction.loai == "thu") incomeColor else expenseColor
    val prefix = if (transaction.loai == "thu") "+" else "-"

    // Định dạng số tiền trong item
    val isVietnamese = Locale.getDefault().language == "vi"
    val formatLocale = if (isVietnamese) Locale.GERMAN else Locale.US
    val formattedAmount = String.format(formatLocale, "%,.0f", transaction.soTien)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_transaction_title)) },
            text = { Text(stringResource(R.string.delete_confirmation)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick(transaction.id)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(stringResource(R.string.delete), color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
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
            // SỬA: Tự động dịch Tên Nhóm (Ăn uống/Dining)
            Text(
                text = getLocalizedCategoryName(transaction.nhom),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // SỬA: Tự động dịch Phạm vi (Bản thân/Myself)
            Text(
                text = getLocalizedScopeName(transaction.tenGiaoDich),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

        Text(
            text = "$prefix$formattedAmount Đ",
            fontWeight = FontWeight.Bold,
            color = amountColor,
            fontSize = 18.sp
        )
        IconButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
        }
    }
}