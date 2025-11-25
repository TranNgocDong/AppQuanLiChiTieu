package com.example.project_mbp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_mbp.R
import com.example.project_mbp.model.User
import com.example.project_mbp.repository.User_Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class User_ViewModel : ViewModel() {

    private val repository = User_Repository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isLogined = MutableStateFlow(false)
    val isLogined = _isLogined.asStateFlow()

    private val _message = MutableStateFlow<Int?>(null)
    val message = _message.asStateFlow()

    private val _isAwaitingVerification = MutableStateFlow(false)
    val isAwaitingVerification = _isAwaitingVerification.asStateFlow()

    private val _verificationSeconds = MutableStateFlow(0)
    val verificationSeconds = _verificationSeconds.asStateFlow()

    private var pendingUser: User? = null
    private var verificationJob: Job? = null

    // Đã xóa biến storageRef vì không dùng nữa

    var isUploading by mutableStateOf(false)

    fun setMessage(resId: Int) {
        _message.value = resId
    }

    fun clearMessage() {
        _message.value = null
    }

    // ---------------- CÁC HÀM LOGIN/REGISTER GIỮ NGUYÊN ----------------
    fun loginWithFacebook(accessToken: String) {
        if (accessToken.isBlank()) {
            setMessage(R.string.error_facebook_token_null)
            return
        }
        viewModelScope.launch {
            try {
                val user = repository.loginWithFacebook(accessToken)
                if (user != null) {
                    val fullUser = repository.getUserByUid(user.uid)
                    _currentUser.value = fullUser ?: user
                    _isLogined.value = true
                    setMessage(R.string.login_success)
                } else {
                    setMessage(R.string.error_facebook_login_failed)
                }
            } catch (e: Exception) {
                setMessage(R.string.error_facebook_unknown)
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        if (idToken.isBlank()) {
            setMessage(R.string.error_google_token_null)
            return
        }
        viewModelScope.launch {
            try {
                val user = repository.loginWithGoogle(idToken)
                if (user != null) {
                    _currentUser.value = user
                    _isLogined.value = true
                    setMessage(R.string.login_success)
                } else {
                    setMessage(R.string.error_login_generic)
                }
            } catch (e: Exception) {
                setMessage(R.string.error_google_unknown)
            }
        }
    }

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> {
                    setMessage(R.string.error_empty_email)
                    return@launch
                }
                password.isBlank() -> {
                    setMessage(R.string.error_empty_password)
                    return@launch
                }
                !email.contains("@") -> {
                    setMessage(R.string.invalid_email)
                    return@launch
                }
            }
            try {
                val user = repository.loginWithEmail(email, password)
                if (user != null) {
                    val fullUser = repository.getUserByUid(user.uid)
                    _currentUser.value = fullUser ?: user
                    _isLogined.value = true
                    setMessage(R.string.login_success)
                } else {
                    setMessage(R.string.error_wrong_email_or_password)
                }
            } catch (e: Exception) {
                setMessage(R.string.error_wrong_email_or_password)
            }
        }
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            val user = repository.getCurrentUser()
            if (user != null) {
                val firestoreUser = repository.getUserByUid(user.uid)
                _currentUser.value = firestoreUser ?: user
                _isLogined.value = true
            } else {
                _isLogined.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _isLogined.value = false
            _currentUser.value = null
            setMessage(R.string.logout_success)
            stopVerificationWatcher()
        }
    }

    fun registerWithEmail(email: String, password1: String, password2: String, name: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> {
                    setMessage(R.string.enter_email)
                    return@launch
                }
                password1.isBlank() -> {
                    setMessage(R.string.enter_password)
                    return@launch
                }
                password2.isBlank() -> {
                    setMessage(R.string.error_confirm_password)
                    return@launch
                }
                password1 != password2 -> {
                    setMessage(R.string.error_password_not_match)
                    return@launch
                }
                !email.contains("@") -> {
                    setMessage(R.string.invalid_email)
                    return@launch
                }
            }
            try {
                val uid = repository.registerWithEmail_returnUid(email, password1, name)
                if (uid != null) {
                    pendingUser = User(uid = uid, email = email, name = name)
                    _isAwaitingVerification.value = true
                    _verificationSeconds.value = 60
                    startVerificationWatcher()
                    setMessage(R.string.verify_email_sent)
                } else {
                    setMessage(R.string.register_failed)
                }
            } catch (e: Exception) {
                setMessage(R.string.error_register_generic)
            }
        }
    }

    private fun startVerificationWatcher() {
        stopVerificationWatcher()
        verificationJob = viewModelScope.launch {
            while (_verificationSeconds.value > 0) {
                repository.reloadCurrentUser()
                if (repository.isEmailVerified()) {
                    val user = pendingUser
                    if (user != null) {
                        val saved = repository.saveUserToFirestore(user.uid, user.email, user.name)
                        if (saved) setMessage(R.string.verify_success)
                        else setMessage(R.string.verify_save_failed)
                    } else {
                        setMessage(R.string.verify_success)
                    }
                    repository.logout()
                    _isAwaitingVerification.value = false
                    _verificationSeconds.value = 0
                    pendingUser = null
                    return@launch
                }
                delay(1000L)
                _verificationSeconds.value = (_verificationSeconds.value - 1).coerceAtLeast(0)
            }
            _isAwaitingVerification.value = true
            setMessage(R.string.verify_not_completed)
        }
    }

    private fun stopVerificationWatcher() {
        verificationJob?.cancel()
        verificationJob = null
    }

    fun checkEmailVerificationNow() {
        viewModelScope.launch {
            repository.reloadCurrentUser()
            if (repository.isEmailVerified()) {
                val user = pendingUser
                if (user != null) {
                    val saved = repository.saveUserToFirestore(user.uid, user.email, user.name)
                    if (saved) setMessage(R.string.verify_success)
                    else setMessage(R.string.verify_save_failed)
                } else {
                    setMessage(R.string.verify_success)
                }
                repository.logout()
                _isAwaitingVerification.value = false
                _verificationSeconds.value = 0
                pendingUser = null
                stopVerificationWatcher()
            } else {
                setMessage(R.string.verify_not_completed)
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            val ok = repository.resendVerificationEmail()
            if (ok) {
                setMessage(R.string.verify_sent_again)
                _verificationSeconds.value = 60
                startVerificationWatcher()
            } else {
                setMessage(R.string.verify_resend_failed)
            }
        }
    }

    fun updateUserInfo(updatedUser: User) {
        viewModelScope.launch {
            val success = repository.updateUserInfo(updatedUser.uid, updatedUser)
            if (success) {
                _currentUser.value = updatedUser
                setMessage(R.string.update_success)
            } else {
                setMessage(R.string.update_failed)
            }
        }
    }

    // ================== PHẦN THAY ĐỔI QUAN TRỌNG ==================

    // 1. Hàm chuyển ảnh URI thành Base64 String (Đã nén nhỏ)
    // Trong User_ViewModel.kt

    // Trong User_ViewModel.kt

    private fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null

            // 1. GIẢM XUỐNG 512x512
            // 1024 quá rủi ro cho Firestore (dễ bị vượt quá 1MB). 512 là quá đủ nét cho Avatar điện thoại.
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true)

            val outputStream = ByteArrayOutputStream()

            // Nén 70% cho nét
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            val byteArray = outputStream.toByteArray()

            // 2. QUAN TRỌNG NHẤT: Đổi Base64.DEFAULT thành Base64.NO_WRAP
            // DEFAULT sẽ thêm dấu xuống dòng làm hỏng đường dẫn ảnh
            "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 2. Hàm lưu ảnh (Base64) thẳng vào Firestore (KHÔNG DÙNG STORAGE)
    fun saveAvatarToFirestore(context: Context, uri: Uri) {
        val user = currentUser.value ?: return
        isUploading = true

        // Chạy trên luồng IO để xử lý ảnh nặng
        viewModelScope.launch(Dispatchers.IO) {
            val base64Image = uriToBase64(context, uri)

            if (base64Image == null) {
                isUploading = false
                launch(Dispatchers.Main) { setMessage(R.string.update_failed) }
                return@launch
            }

            // Tạo user mới với chuỗi ảnh base64
            val updatedUser = user.copy(avatarUrl = base64Image)

            // Lưu vào Firestore
            val success = repository.updateUserInfo(updatedUser.uid, updatedUser)

            isUploading = false

            // Cập nhật UI trên luồng chính
            launch(Dispatchers.Main) {
                if (success) {
                    _currentUser.value = updatedUser
                    setMessage(R.string.upload_success)
                } else {
                    setMessage(R.string.update_failed)
                }
            }
        }
    }
}