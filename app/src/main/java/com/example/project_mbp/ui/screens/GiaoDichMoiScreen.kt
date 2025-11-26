package com.example.project_mbp.ui.screens

<<<<<<< HEAD
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
=======
import android.R.attr.fontStyle
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
>>>>>>> main
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
<<<<<<< HEAD
import androidx.compose.foundation.layout.*
=======
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
>>>>>>> main
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
<<<<<<< HEAD
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
=======
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme // Đã có
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
>>>>>>> main
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.project_mbp.R
<<<<<<< HEAD
import com.example.project_mbp.ui.theme.ExpenseRed
import com.example.project_mbp.ui.theme.IncomeGreen
=======
// Bỏ các import màu cố định
// import com.example.project_mbp.ui.theme.ButtonBlue
// import com.example.project_mbp.ui.theme.ButtonRed
// import com.example.project_mbp.ui.theme.SelectedYellow
// import com.example.project_mbp.ui.theme.TealBackground
// import com.example.project_mbp.ui.theme.TextLight
>>>>>>> main
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

<<<<<<< HEAD
=======
// Danh sách các nhóm (Giữ nguyên)
val transactionCategories = listOf(
    "Ăn uống", "Du lịch", "Tiền lương", "Thú cưng", "Hóa đơn nước",
    "Hóa đơn điện", "Sức khỏe", "Giải trí", "Mua sắm", "Di chuyển", "Khác"
)

// THÊM MỚI: Danh sách các nhóm THU NHẬP
val incomeCategories = listOf("Tiền lương", "Thưởng", "Thu nhập khác", "Được tặng")

val scopeCategories = listOf("Bản thân", "Gia đình", "Bạn bè")

>>>>>>> main
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiaoDichMoiScreen(
    navController: NavController,
    transactionViewModel: Transaction_ViewModel
) {
    val context = LocalContext.current

<<<<<<< HEAD
    // Lấy dữ liệu từ file Categories.kt (Không định nghĩa lại ở đây nữa)
    val expenseCats = getExpenseCategories()
    val incomeCats = getIncomeCategories()
    val scopeCats = getScopeCategories()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(R.string.expense), stringResource(R.string.income))
    val selectedType = if (selectedTabIndex == 0) "chi" else "thu"
    val selectedColor = if (selectedType == "chi") ExpenseRed else IncomeGreen
    val currentCategoryList = if (selectedType == "chi") expenseCats else incomeCats

    var nhomGiaoDich by remember(currentCategoryList) { mutableStateOf(currentCategoryList.firstOrNull() ?: "") }
    var chiCho by remember(scopeCats) { mutableStateOf(scopeCats.firstOrNull() ?: "") }
=======
    var nhomGiaoDich by remember { mutableStateOf(transactionCategories[0]) }
    var chiCho by remember { mutableStateOf(scopeCategories[0]) }
>>>>>>> main
    var soTien by remember { mutableStateOf("") }
    var ghiChu by remember { mutableStateOf("") }

    val formatterDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formatterTime = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    var selectedDate by remember { mutableStateOf(formatterDate.format(Date())) }
    var selectedTime by remember { mutableStateOf(formatterTime.format(Date())) }

    var isTimeLocked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val addStatus by transactionViewModel.addTransactionStatus.collectAsState()

    LaunchedEffect(addStatus) {
        val (success, message) = addStatus
        if (success != null) {
            isLoading = false
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (success) {
                soTien = ""
                ghiChu = ""
                navController.popBackStack()
            }
            transactionViewModel.clearAddTransactionStatus()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (!isTimeLocked) {
                val now = Date()
                selectedDate = formatterDate.format(now)
                selectedTime = formatterTime.format(now)
            }
            delay(1000L)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
<<<<<<< HEAD
                            text = stringResource(R.string.add_transaction_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 30.sp,
=======
                            "Thêm giao dịch mới",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 34.sp,
>>>>>>> main
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
<<<<<<< HEAD
                windowInsets = WindowInsets(10.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
=======
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )

>>>>>>> main
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
<<<<<<< HEAD
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = selectedColor,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = if (selectedTabIndex == index) selectedColor else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.category_group), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                CategoryDropdownMenu(
                    label = "",
                    options = currentCategoryList,
=======
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Dropdown nhóm giao dịch
                Text(text = "Nhóm giao dịch: ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                CategoryDropdownMenu(
                    label = "",
                    options = transactionCategories,
>>>>>>> main
                    selectedOption = nhomGiaoDich,
                    onOptionSelected = { nhomGiaoDich = it },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.iconnhomgiaodich),
                            contentDescription = "Nhóm",
                            modifier = Modifier.size(40.dp),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

<<<<<<< HEAD
                Text(text = stringResource(R.string.scope), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                CategoryDropdownMenu(
                    label = "",
                    options = scopeCats,
=======
                // Dropdown "Chi cho"
                Text(text = "Chi tiêu cho: ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                CategoryDropdownMenu(
                    label = "",
                    options = scopeCategories,
>>>>>>> main
                    selectedOption = chiCho,
                    onOptionSelected = { chiCho = it },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.iconloaichitieu),
<<<<<<< HEAD
                            contentDescription = "Phạm vi",
=======
                            contentDescription = "Chi cho",
>>>>>>> main
                            modifier = Modifier.size(40.dp),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

<<<<<<< HEAD
                val amountLabel = if (selectedType == "chi") stringResource(R.string.amount_expense_label) else stringResource(R.string.amount_income_label)
                val amountPlaceholder = if (selectedType == "chi") stringResource(R.string.placeholder_expense) else stringResource(R.string.placeholder_income)

                Text(text = amountLabel, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
=======
                // Số tiền
                Text(text = "Số tiền chi tiêu: ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
>>>>>>> main
                FormTextField(
                    value = soTien,
                    label = "",
                    onValueChange = { soTien = it },
<<<<<<< HEAD
                    placeholder = amountPlaceholder,
=======
                    placeholder = "Nhập số tiền chi tiêu",
>>>>>>> main
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.icontien),
                            contentDescription = "Số tiền",
                            modifier = Modifier.size(40.dp),
                        )
                    }
                )

<<<<<<< HEAD
                Text(text = stringResource(R.string.date_label), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
=======
                // DatePickerField
                Text(text = "Ngày, tháng, năm:", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
>>>>>>> main
                DatePickerField(
                    label = "",
                    selectedDate = selectedDate,
                    onDateSelected = { dateString ->
                        isTimeLocked = true
                        selectedDate = dateString
                    },
                    onNowClicked = { dateString ->
                        isTimeLocked = false
                        selectedDate = dateString
                    }
                )

<<<<<<< HEAD
                Text(text = stringResource(R.string.time_label), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
=======
                // TimePickerField
                Text(text = "Giờ:", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
>>>>>>> main
                TimePickerField(
                    label = "",
                    selectedTime = selectedTime,
                    onTimeSelected = { timeString ->
                        isTimeLocked = true
                        selectedTime = timeString
                    },
                    onNowClicked = { timeString ->
                        isTimeLocked = false
                        selectedTime = timeString
                    }
                )

<<<<<<< HEAD
                Text(text = stringResource(R.string.note_label), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                FormTextField(
                    value = ghiChu,
                    onValueChange = { ghiChu = it },
                    placeholder = stringResource(R.string.note_placeholder),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.note),
                                contentDescription = "Ghi chú",
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.height(100.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

=======
                // Ghi chú
                Text(text = "Ghi chú: ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                FormTextField(
                    value = ghiChu,
                    label = "",
                    onValueChange = { ghiChu = it },
                    placeholder = "Viết ghi chú vào đây",
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.note),
                            contentDescription = "Ghi chú",
                            modifier = Modifier.size(40.dp),
                        )
                    },
                    singleLine = false,
                    modifier = Modifier.height(100.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logic animation cho nút (Giữ nguyên)
>>>>>>> main
                val interactionSourceHuy = remember { MutableInteractionSource() }
                val isPressedHuy by interactionSourceHuy.collectIsPressedAsState()
                val scaleHuy by animateFloatAsState(
                    targetValue = if (isPressedHuy) 0.95f else 1f,
                    label = "scaleHuy",
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

                val interactionSourceLuu = remember { MutableInteractionSource() }
                val isPressedLuu by interactionSourceLuu.collectIsPressedAsState()
                val scaleLuu by animateFloatAsState(
                    targetValue = if (isPressedLuu) 0.95f else 1f,
                    label = "scaleLuu",
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

<<<<<<< HEAD
=======
                // Nút bấm
>>>>>>> main
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = scaleHuy
                                scaleY = scaleHuy
                            },
                        interactionSource = interactionSourceHuy
                    ) {
<<<<<<< HEAD
                        Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onError, fontSize = 16.sp)
=======
                        Text("HỦY", color = MaterialTheme.colorScheme.onError, fontSize = 16.sp)
>>>>>>> main
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (soTien.isBlank()) {
<<<<<<< HEAD
                                Toast.makeText(context, context.getString(R.string.error_empty_amount), Toast.LENGTH_SHORT).show()
                                return@Button
                            }

=======
                                Toast.makeText(context, "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // === Logic "thu"/"chi" (Giữ nguyên, vì nó ĐÃ ĐÚNG) ===
                            val transactionType = if (nhomGiaoDich in incomeCategories) {
                                "thu"
                            } else {
                                "chi"
                            }
                            // ========================

>>>>>>> main
                            isLoading = true
                            transactionViewModel.addTransaction(
                                ten = chiCho,
                                soTien = soTien,
                                nhom = nhomGiaoDich,
                                ghiChu = ghiChu,
<<<<<<< HEAD
                                loai = selectedType,
=======
                                loai = transactionType, // Truyền loại giao dịch đã xác định
>>>>>>> main
                                ngay = selectedDate,
                                gio = selectedTime
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = scaleLuu
                                scaleY = scaleLuu
                            },
                        interactionSource = interactionSourceLuu,
                        enabled = !isLoading
                    ) {
                        if (isLoading) CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
<<<<<<< HEAD
                        else Text(stringResource(R.string.save), color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
=======
                        else Text("LƯU", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
>>>>>>> main
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
<<<<<<< HEAD
=======
    // SỬA: Lấy màu từ theme
>>>>>>> main
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
<<<<<<< HEAD
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f), fontSize = 24.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
=======
            // SỬA: Dùng onBackgroundColor
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f), fontSize = 24.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            textStyle = TextStyle(fontSize = 18.sp, color = onBackgroundColor),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = onBackgroundColor,
                cursorColor = onBackgroundColor,
                unfocusedBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedBorderColor = onBackgroundColor,
                unfocusedLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedLeadingIconColor = onBackgroundColor,
                unfocusedTrailingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedTrailingIconColor = onBackgroundColor,
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
<<<<<<< HEAD
=======
            // SỬA: Dùng màu surface
>>>>>>> main
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
<<<<<<< HEAD
=======
                    // SỬA: Dùng màu onSurface
>>>>>>> main
                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
<<<<<<< HEAD
                    modifier = Modifier
=======
                    modifier = Modifier // Bỏ background cố định
>>>>>>> main
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    label: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    verticalCenter: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
<<<<<<< HEAD
=======
    // SỬA: Lấy màu từ theme
>>>>>>> main
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    Box(
        contentAlignment = if (verticalCenter) Alignment.Center else Alignment.TopStart
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            label = if (label.isNotEmpty()) { { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) } } else null,
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder, color = onBackgroundColor.copy(alpha = 0.5f), fontSize = 20.sp) } } else null,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            singleLine = singleLine,
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            textStyle = TextStyle(fontSize = 18.sp, color = onBackgroundColor),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = onBackgroundColor,
                cursorColor = onBackgroundColor,
                unfocusedBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedBorderColor = onBackgroundColor,
                unfocusedLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedLeadingIconColor = onBackgroundColor,
            ),
            modifier = modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
        )
    }
}

<<<<<<< HEAD
=======


>>>>>>> main
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
<<<<<<< HEAD
    onDateSelected: (String) -> Unit,
    onNowClicked: (String) -> Unit
=======
    onDateSelected: (String) -> Unit, // Khi nhấn OK
    onNowClicked: (String) -> Unit   // Khi nhấn "Bây giờ"
>>>>>>> main
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    val formatterDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
<<<<<<< HEAD
=======
    // SỬA: Lấy màu từ theme
>>>>>>> main
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            enabled = false,
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.date),
                    contentDescription = "Ngày",
                    modifier = Modifier.size(40.dp),
                )
            },
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            textStyle = TextStyle(fontSize = 20.sp, color = onBackgroundColor.copy(alpha = 0.7f)),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = onBackgroundColor,
                disabledBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                disabledLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedTextColor = onBackgroundColor,
                unfocusedTextColor = onBackgroundColor,
                unfocusedBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedBorderColor = onBackgroundColor,
                unfocusedLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedLeadingIconColor = onBackgroundColor,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDatePicker) {
<<<<<<< HEAD
=======
        // DatePickerDialog tự động dùng theme
>>>>>>> main
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Row {
<<<<<<< HEAD
                    TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel), fontSize = 16.sp) }
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateSelected(formatterDate.format(Date(it)))
                            }
                            showDatePicker = false
                        }) { Text(stringResource(R.string.ok), fontSize = 16.sp) }
                    TextButton(
                        onClick = {
                            val now = Calendar.getInstance()
                            onNowClicked(formatterDate.format(now.time))
                            showDatePicker = false
                        }) { Text(stringResource(R.string.now), color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp) }
=======
                    TextButton(onClick = { showDatePicker = false }) { Text("Hủy", fontSize = 16.sp) }
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateSelected(formatterDate.format(Date(it))) // Gọi lambda OK
                            }
                            showDatePicker = false
                        }) { Text("OK", fontSize = 16.sp) }
                    TextButton(
                        onClick = {
                            val now = Calendar.getInstance()
                            onNowClicked(formatterDate.format(now.time)) // Gọi lambda "Bây giờ"
                            showDatePicker = false
                        }) { Text("Bây giờ", color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp) } // SỬA: Dùng màu theme
>>>>>>> main
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    label: String,
    selectedTime: String,
<<<<<<< HEAD
    onTimeSelected: (String) -> Unit,
    onNowClicked: (String) -> Unit
=======
    onTimeSelected: (String) -> Unit, // Khi nhấn OK
    onNowClicked: (String) -> Unit   // Khi nhấn "Bây giờ"
>>>>>>> main
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    val formatterTime = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
<<<<<<< HEAD
=======
    // SỬA: Lấy màu từ theme
>>>>>>> main
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true },
    ) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            enabled = false,
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.gio),
                    contentDescription = "Giờ",
                    modifier = Modifier.size(40.dp),
                )
            },
<<<<<<< HEAD
=======
            // SỬA: Dùng onBackgroundColor
>>>>>>> main
            textStyle = TextStyle(fontSize = 20.sp, color = onBackgroundColor.copy(alpha = 0.7f)),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = onBackgroundColor,
                disabledBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                disabledLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedTextColor = onBackgroundColor,
                unfocusedTextColor = onBackgroundColor,
                unfocusedBorderColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedBorderColor = onBackgroundColor,
                unfocusedLeadingIconColor = onBackgroundColor.copy(alpha = 0.7f),
                focusedLeadingIconColor = onBackgroundColor,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Column(
                modifier = Modifier
<<<<<<< HEAD
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
=======
                    // SỬA: Dùng màu surface
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                // TimePicker tự động dùng theme
>>>>>>> main
                TimePicker(state = timePickerState)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
<<<<<<< HEAD
                    TextButton(onClick = { showTimePicker = false }) { Text(stringResource(R.string.cancel), fontSize = 16.sp) }
                    TextButton(onClick = {
                        onTimeSelected(String.format("%02d:%02d:00", timePickerState.hour, timePickerState.minute))
                        showTimePicker = false
                    }) { Text(stringResource(R.string.ok), fontSize = 16.sp) }
                    TextButton(onClick = {
                        val now = Calendar.getInstance()
                        onNowClicked(formatterTime.format(now.time))
                        showTimePicker = false
                    }) { Text(stringResource(R.string.now), color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp) }
=======
                    TextButton(onClick = { showTimePicker = false }) { Text("Hủy", fontSize = 16.sp) }
                    TextButton(onClick = {
                        onTimeSelected(String.format("%02d:%02d:00", timePickerState.hour, timePickerState.minute)) // Gọi lambda OK
                        showTimePicker = false
                    }) { Text("OK", fontSize = 16.sp) }
                    TextButton(onClick = {
                        val now = Calendar.getInstance()
                        onNowClicked(formatterTime.format(now.time)) // Gọi lambda "Bây giờ"
                        showTimePicker = false
                    }) { Text("Bây giờ", color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp) } // SỬA: Dùng màu theme
>>>>>>> main
                }
            }
        }
    }
}