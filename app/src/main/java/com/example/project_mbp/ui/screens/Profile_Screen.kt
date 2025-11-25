package com.example.project_mbp.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_mbp.R
import com.example.project_mbp.model.User
import com.example.project_mbp.viewmodel.User_ViewModel

@Composable
fun Profile_Screen(vm: User_ViewModel = viewModel(), navController: NavController) {
    val user by vm.currentUser.collectAsState()
    val context = LocalContext.current

    // --- LOGIC NHẬN DIỆN GIAO DIỆN (QUAN TRỌNG) ---
    // Thay vì dùng isSystemInDarkTheme(), ta kiểm tra màu nền thực tế của App.
    // Nếu bạn đổi theme trong Cài đặt -> MaterialTheme đổi -> Giá trị này tự động cập nhật.
    // luminance < 0.5 nghĩa là nền đang tối.
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // --- CẤU HÌNH MÀU "TRUE BLACK" (ĐEN TUYỆT ĐỐI) ---
    // 1. Nền màn hình: Nếu tối -> Đen (Black), Nếu sáng -> Xám nhạt
    val screenBackgroundColor = if (isDark) Color.Black else Color(0xFFF5F5F5)

    // 2. Nền Thẻ & Dialog: Nếu tối -> Đen (Black), Nếu sáng -> Trắng
    val surfaceColor = if (isDark) Color.Black else Color.White

    // 3. Màu chữ: Tối -> Trắng, Sáng -> Đen
    val primaryTextColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    // 4. Viền thẻ: Chỉ hiện khi ở chế độ tối (màu xám đậm) để tách biệt thẻ đen với nền đen
    val cardBorder = if (isDark) BorderStroke(1.dp, Color(0xFF333333)) else null

    // 5. Nền icon nhỏ bên trong: Tối -> Xám rất đậm, Sáng -> Xanh nhạt
    val iconBoxColor = if (isDark) Color(0xFF1A1A1A) else Color(0xFFE0F2F1)

    val messageResId by vm.message.collectAsState()
    LaunchedEffect(messageResId) {
        messageResId?.let { resId ->
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
            vm.clearMessage()
        }
    }

    var showEditDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            vm.saveAvatarToFirestore(context, it)
        }
    }

    // Gradient Header (Giữ màu xanh Teal thương hiệu)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF2A9D8F), Color(0xFF264653))
    )

    Box(modifier = Modifier.fillMaxSize().background(screenBackgroundColor)) {
        // Header Cong
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // --- AVATAR ---
            Box(contentAlignment = Alignment.Center) {
                val avatarBitmap = rememberBase64(user?.avatarUrl)
                Image(
                    painter = if (avatarBitmap != null) {
                        BitmapPainter(avatarBitmap)
                    } else {
                        rememberAsyncImagePainter(
                            model = if (!user?.avatarUrl.isNullOrEmpty() && avatarBitmap == null) user?.avatarUrl else R.drawable.avt
                        )
                    },
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(4.dp, surfaceColor, CircleShape) // Viền avatar ăn theo màu nền (Đen/Trắng)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                if (vm.isUploading) {
                    CircularProgressIndicator(
                        color = Color(0xFF2A9D8F),
                        modifier = Modifier.size(48.dp)
                    )
                }

                if (!vm.isUploading) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(surfaceColor)
                            .border(1.dp, if(isDark) Color.Gray else Color.LightGray, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Avatar",
                            tint = Color(0xFF2A9D8F),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- TÊN & EMAIL ---
            Text(
                text = user?.name ?: "Chưa cập nhật",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryTextColor
            )
            Text(
                text = user?.email ?: "",
                fontSize = 14.sp,
                color = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- THẺ THÔNG TIN ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor), // Nền thẻ Đen hoặc Trắng
                border = cardBorder, // Viền xám mỏng nếu nền đen
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoRow(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.name_label),
                        value = user?.name ?: "---",
                        textColor = primaryTextColor,
                        subTextColor = secondaryTextColor,
                        iconBgColor = iconBoxColor
                    )
                    HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.3f), thickness = 1.dp)

                    ProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = stringResource(R.string.email_label_profile),
                        value = user?.email ?: "---",
                        textColor = primaryTextColor,
                        subTextColor = secondaryTextColor,
                        iconBgColor = iconBoxColor
                    )
                    HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.3f), thickness = 1.dp)

                    ProfileInfoRow(
                        icon = Icons.Default.Phone,
                        label = stringResource(R.string.phone_label),
                        value = user?.phone ?: "Chưa có số điện thoại",
                        textColor = primaryTextColor,
                        subTextColor = secondaryTextColor,
                        iconBgColor = iconBoxColor
                    )
                    HorizontalDivider(color = secondaryTextColor.copy(alpha = 0.3f), thickness = 1.dp)

                    ProfileInfoRow(
                        icon = Icons.Default.Cake,
                        label = stringResource(R.string.birthday_label),
                        value = user?.birthday ?: "Chưa cập nhật ngày sinh",
                        textColor = primaryTextColor,
                        subTextColor = secondaryTextColor,
                        iconBgColor = iconBoxColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- NÚT CHỈNH SỬA ---
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9D8F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.edit_profile), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        // --- DIALOG CHỈNH SỬA ---
        if (showEditDialog) {
            EditProfileDialog(
                user = user,
                isDark = isDark,
                bgColor = surfaceColor,
                textColor = primaryTextColor,
                onDismiss = { showEditDialog = false },
                onSave = { newName, newPhone, newDob ->
                    val updatedUser = user?.copy(
                        name = newName,
                        phone = newPhone,
                        birthday = newDob
                    )
                    if (updatedUser != null) {
                        vm.updateUserInfo(updatedUser)
                    }
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    textColor: Color,
    subTextColor: Color,
    iconBgColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2A9D8F))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = subTextColor)
            Text(text = value, fontSize = 16.sp, color = textColor, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun EditProfileDialog(
    user: User?,
    isDark: Boolean,
    bgColor: Color,
    textColor: Color,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var birthday by remember { mutableStateOf(user?.birthday ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bgColor, // Nền Dialog (Đen/Trắng)
            border = if(isDark) BorderStroke(1.dp, Color.DarkGray) else null
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Cấu hình TextField để hiển thị tốt trên nền Đen
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = Color(0xFF2A9D8F),
                    focusedBorderColor = Color(0xFF2A9D8F),
                    unfocusedBorderColor = if(isDark) Color.Gray else Color.Black.copy(alpha = 0.5f)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.display_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(stringResource(R.string.phone_number_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = birthday,
                    onValueChange = { birthday = it },
                    label = { Text(stringResource(R.string.birthday_label_input)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel_changes), color = textColor.copy(alpha = 0.7f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(name, phone, birthday) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9D8F))
                    ) {
                        Text(stringResource(R.string.save_changes), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberBase64(base64String: String?): androidx.compose.ui.graphics.ImageBitmap? {
    if (base64String.isNullOrEmpty() || !base64String.startsWith("data:")) return null

    return remember(base64String) {
        try {
            val pureBase64 = base64String.substringAfter(",")
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}