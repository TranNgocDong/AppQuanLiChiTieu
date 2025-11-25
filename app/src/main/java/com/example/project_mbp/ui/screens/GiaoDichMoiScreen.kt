package com.example.project_mbp.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.project_mbp.R
import com.example.project_mbp.ui.theme.ExpenseRed
import com.example.project_mbp.ui.theme.IncomeGreen
import com.example.project_mbp.viewmodel.Transaction_ViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiaoDichMoiScreen(
    navController: NavController,
    transactionViewModel: Transaction_ViewModel
) {
    val context = LocalContext.current

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
                            text = stringResource(R.string.add_transaction_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                windowInsets = WindowInsets(10.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                Text(text = stringResource(R.string.scope), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                CategoryDropdownMenu(
                    label = "",
                    options = scopeCats,
                    selectedOption = chiCho,
                    onOptionSelected = { chiCho = it },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.iconloaichitieu),
                            contentDescription = "Phạm vi",
                            modifier = Modifier.size(40.dp),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

                val amountLabel = if (selectedType == "chi") stringResource(R.string.amount_expense_label) else stringResource(R.string.amount_income_label)
                val amountPlaceholder = if (selectedType == "chi") stringResource(R.string.placeholder_expense) else stringResource(R.string.placeholder_income)

                Text(text = amountLabel, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                FormTextField(
                    value = soTien,
                    label = "",
                    onValueChange = { soTien = it },
                    placeholder = amountPlaceholder,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.icontien),
                            contentDescription = "Số tiền",
                            modifier = Modifier.size(40.dp),
                        )
                    }
                )

                Text(text = stringResource(R.string.date_label), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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

                Text(text = stringResource(R.string.time_label), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                        Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onError, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (soTien.isBlank()) {
                                Toast.makeText(context, context.getString(R.string.error_empty_amount), Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            transactionViewModel.addTransaction(
                                ten = chiCho,
                                soTien = soTien,
                                nhom = nhomGiaoDich,
                                ghiChu = ghiChu,
                                loai = selectedType,
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
                        else Text(stringResource(R.string.save), color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
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
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f), fontSize = 24.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
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
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier = Modifier
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
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    Box(
        contentAlignment = if (verticalCenter) Alignment.Center else Alignment.TopStart
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = if (label.isNotEmpty()) { { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) } } else null,
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder, color = onBackgroundColor.copy(alpha = 0.5f), fontSize = 20.sp) } } else null,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            singleLine = singleLine,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onNowClicked: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    val formatterDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
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
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.date),
                    contentDescription = "Ngày",
                    modifier = Modifier.size(40.dp),
                )
            },
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
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Row {
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
    onTimeSelected: (String) -> Unit,
    onNowClicked: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    val formatterTime = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
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
            label = { Text(label, color = onBackgroundColor.copy(alpha = 0.7f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.gio),
                    contentDescription = "Giờ",
                    modifier = Modifier.size(40.dp),
                )
            },
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
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                TimePicker(state = timePickerState)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
                }
            }
        }
    }
}