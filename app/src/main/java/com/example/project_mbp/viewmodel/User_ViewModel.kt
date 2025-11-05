package com.example.project_mbp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_mbp.model.User
import com.example.project_mbp.repository.User_Repository
import com.google.firebase.auth.FirebaseAuth // 🔹 (added)
import com.google.firebase.firestore.FirebaseFirestore // 🔹 (added)
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class User_ViewModel : ViewModel() {

    private val repository = User_Repository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isLogined = MutableStateFlow(false)
    val isLogined = _isLogined.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    // trạng thái chờ xác minh (sau khi nhấn Đăng Ký)
    private val _isAwaitingVerification = MutableStateFlow(false)
    val isAwaitingVerification = _isAwaitingVerification.asStateFlow()

    // giây còn lại trong countdown (60..0)
    private val _verificationSeconds = MutableStateFlow(0)
    val verificationSeconds = _verificationSeconds.asStateFlow()

    // internal
    private var pendingUser: User? = null
    private var verificationJob: Job? = null


    // ---------------- GOOGLE LOGIN ----------------
    fun loginWithGoogle(idToken: String) {
        if (idToken.isBlank()) {
            _message.value = "Token Google null!"
            return
        }

        viewModelScope.launch {
            try {
                val user = repository.loginWithGoogle(idToken)
                if (user != null) {
                    _currentUser.value = user
                    _isLogined.value = true
                    _message.value = "Đăng nhập thành công!"
                    loadCurrentUser() // 🔹 (added)
                } else {
                    _message.value = "Đăng nhập thất bại!"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi đăng nhập Google: ${e.message}"
            }
        }
    }


    // ---------------- EMAIL LOGIN ----------------
    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> {
                    _message.value = "Vui lòng nhập Email!"
                    return@launch
                }

                password.isBlank() -> {
                    _message.value = "Vui lòng nhập mật khẩu!"
                    return@launch
                }

                !email.contains("@") -> {
                    _message.value = "Email không hợp lệ!"
                    return@launch
                }
            }

            try {
                val user = repository.loginWithEmail(email, password)
                if (user != null) {
                    val fullUser = repository.getUserByUid(user.uid)
                    _currentUser.value = fullUser ?: user
                    _isLogined.value = true
                    _message.value = "Đăng nhập thành công!"
                    loadCurrentUser() // 🔹 (added)
                } else {
                    _message.value = "Email hoặc mật khẩu không đúng!"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi đăng nhập: ${e.message}"
            }
        }
    }


    // ---------------- CHECK LOGIN STATUS ----------------
    fun checkLoginStatus() {
        viewModelScope.launch {
            val user = repository.getCurrentUser()
            if (user != null) {
                val firestoreUser = repository.getUserByUid(user.uid)
                _currentUser.value = firestoreUser ?: user
                _isLogined.value = true
                loadCurrentUser() // 🔹 (added)
            } else {
                _isLogined.value = false
            }
        }
    }


    // ---------------- LOGOUT ----------------
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _isLogined.value = false
            _currentUser.value = null
            _message.value = "Đã đăng xuất!"
            stopVerificationWatcher()
        }
    }


    // ---------------- MESSAGE CONTROL ----------------
    fun setMessage(mess: String) {
        _message.value = mess
    }

    fun clearMessage() {
        _message.value = null
    }


    // ---------------- REGISTER + EMAIL VERIFY ----------------
    fun registerWithEmail(email: String, password1: String, password2: String, name: String) {
        viewModelScope.launch {
            when {
                email.isBlank() -> {
                    _message.value = "Vui lòng nhập email!"
                    return@launch
                }
                password1.isBlank() -> {
                    _message.value = "Vui lòng nhập mật khẩu!"
                    return@launch
                }
                password2.isBlank() -> {
                    _message.value = "Vui lòng xác nhận mật khẩu!"
                    return@launch
                }
                password1 != password2 -> {
                    _message.value = "Mật khẩu xác nhận không trùng khớp!"
                    return@launch
                }
                !email.contains("@gmail.com") -> {
                    _message.value = "Email không hợp lệ!"
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
                    _message.value = "Đã gửi email xác minh. Vui lòng mở mail để xác nhận."
                } else {
                    _message.value = "Đăng ký thất bại!"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi đăng ký: ${e.message}"
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
                        if (saved) {
                            _message.value = "Xác minh thành công! Bạn có thể đăng nhập."
                        } else {
                            _message.value = "Xác minh thành công nhưng lưu dữ liệu thất bại."
                        }
                    } else {
                        _message.value = "Xác minh thành công!"
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
            _message.value = "Chưa xác minh. Bạn có thể gửi lại email xác minh."
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
                    if (saved) {
                        _message.value = "Xác minh thành công!"
                    } else {
                        _message.value = "Xác minh thành công nhưng lưu dữ liệu thất bại."
                    }
                } else {
                    _message.value = "Xác minh thành công!"
                }
                repository.logout()
                _isAwaitingVerification.value = false
                _verificationSeconds.value = 0
                pendingUser = null
                stopVerificationWatcher()
            } else {
                _message.value = "Chưa xác minh. Vui lòng kiểm tra email."
            }
        }
    }


    fun resendVerificationEmail() {
        viewModelScope.launch {
            val ok = repository.resendVerificationEmail()
            if (ok) {
                _message.value = "Đã gửi lại email xác minh."
                _verificationSeconds.value = 60
                startVerificationWatcher()
            } else {
                _message.value = "Gửi lại email xác minh thất bại."
            }
        }
    }


    // ---------------- UPDATE USER ----------------
    fun updateUserInfo(updatedUser: User) {
        viewModelScope.launch {
            val success = repository.updateUserInfo(updatedUser.uid, updatedUser)
            if (success) {
                _currentUser.value = updatedUser
                _message.value = "Cập nhật thông tin thành công!"
                loadCurrentUser() // 🔹 (added)
            } else {
                _message.value = "Lỗi cập nhật thông tin!"
            }
        }
    }


    // ---------------- LOAD CURRENT USER (NEW) ----------------
    fun loadCurrentUser() { // 🔹 (added)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val userData = snapshot.toObject(User::class.java)
                if (userData != null) {
                    _currentUser.value = userData
                }
            }
    }
}
