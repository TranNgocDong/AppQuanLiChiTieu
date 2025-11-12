package com.example.project_mbp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project_mbp.R
import com.example.project_mbp.viewmodel.User_ViewModel
import com.example.project_mbp.ui.components.EditDialog
import com.example.project_mbp.ui.components.LogoutDialog
import com.google.firebase.storage.FirebaseStorage

@Composable
fun Profile_Screen(vm: User_ViewModel = viewModel(), navController: NavController) {
    val user by vm.currentUser.collectAsState()

    // Nếu user = null (sau khi logout) thì quay về login
    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F6FD))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(10.dp))

        // Avatar có thể chỉnh sửa
        EditableAvatar(
            avatarUrl = user?.avatarUrl,
            uid = user?.uid,
            onUpdate = { newUrl ->
                val updatedUser = user?.copy(avatarUrl = newUrl)
                if (updatedUser != null) vm.updateUserInfo(updatedUser)
            },
            vm = vm,
        )

        Spacer(Modifier.height(100.dp))

        // Thông tin có thể chỉnh sửa bằng dialog
        EditableUserInfoRow(
            label = "Họ tên",
            value = user?.name ?: "",
            onSave = { newName ->
                val updatedUser = user?.copy(name = newName)
                if (updatedUser != null) vm.updateUserInfo(updatedUser)
            }
        )

        EditableUserInfoRow(
            label = "Email",
            value = user?.email ?: "",
            editable = false
        )

        Spacer(Modifier.height(40.dp))

        // Nút đăng xuất
        Button(
            onClick = { showLogoutDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF36435)),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text("Đăng xuất", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Dialog xác nhận đăng xuất
        if (showLogoutDialog) {
            LogoutDialog(
                onDismiss = { showLogoutDialog = false },
                onConfirm = {
                    showLogoutDialog = false
                    vm.logout()
                }
            )
        }
    }
}

// ------------------ COMPONENT: AVATAR -------------------
@Composable
fun EditableAvatar(
    avatarUrl: String?,
    uid: String?,
    onUpdate: (String) -> Unit,
    vm: User_ViewModel,
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempAvatar by remember { mutableStateOf(avatarUrl ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        if (uri != null) tempAvatar = uri.toString()
    }

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = rememberAsyncImagePainter(tempAvatar.ifEmpty { R.drawable.avt }),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        if (isEditing) {
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .offset(y = 80.dp)
                    .background(Color(0xFFEFEFF5), RoundedCornerShape(20.dp)),
            ) {
                IconButton(onClick = { imagePicker.launch("image/*") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Chọn ảnh",
                        tint = Color.Blue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = {
                    if (imageUri != null && uid != null) {
                        uploading = true
                        val storageRef = FirebaseStorage.getInstance().reference
                            .child("avatars/$uid.jpg")

                        val uploadTask = storageRef.putFile(imageUri!!)
                        uploadTask.addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                val downloadLink = downloadUrl.toString()
                                onUpdate(downloadLink)

                                val currentUser = vm.currentUser.value
                                val updatedUser = currentUser?.copy(avatarUrl = downloadLink)
                                if (updatedUser != null) vm.updateUserInfo(updatedUser)

                                uploading = false
                                isEditing = false
                            }
                        }.addOnFailureListener {
                            uploading = false
                            isEditing = false
                        }
                    } else {
                        onUpdate(tempAvatar)
                        isEditing = false
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_save),
                        contentDescription = "Save",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = { isEditing = false }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = "Cancel",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit Avatar",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, start = 4.dp)
                    .clickable { isEditing = true },
                tint = Color.Black
            )
        }

        if (uploading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0x66000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

// ------------------ COMPONENT: USER INFO ROW -------------------
@Composable
fun EditableUserInfoRow(
    label: String,
    value: String,
    editable: Boolean = true,
    onSave: (String) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .heightIn(min = 46.dp)
        ) {
            Text(
                text = value.ifEmpty { "Chưa có thông tin..." },
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )

            if (editable) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (showDialog) {
            EditDialog(
                title = "Chỉnh sửa $label",
                currentValue = value,
                onConfirm = { onSave(it) },
                onDismiss = { showDialog = false }
            )
        }
    }
}
