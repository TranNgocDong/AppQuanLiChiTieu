package com.example.project_mbp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_mbp.model.User
import com.example.project_mbp.repository.User_Repository
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


    // LOGIN GOOGLE
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
                } else {
                    _message.value = "Đăng nhập thất bại!"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi đăng nhập Google: ${e.message}"
            }
        }
    }


    // LOGIN EMAIL
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
                    // lấy thông tin Firestore để đảm bảo dữ liệu đầy đủ
                    if (!repository.isEmailVerified()) {
                        repository.logout()
                        _message.value = "Vui lòng xác minh email trước khi đăng nhập!"
                        return@launch
                    }

                    val fullUser = repository.getUserByUid(user.uid)
                    _currentUser.value = fullUser ?: user
                    _isLogined.value = true
                    _message.value = "Đăng nhập thành công!"
                } else {
                    _message.value = "Email hoặc mật khẩu không đúng!"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi đăng nhập: ${e.message}"
            }
        }
    }


    // KIỂM TRA TRẠNG THÁI LOGIN
    fun checkLoginStatus() {
        viewModelScope.launch {
            val user = repository.getCurrentUser()
            //  nếu có currentUser, tải thêm dữ liệu từ Firestore
            if (user != null) {
                val firestoreUser = repository.getUserByUid(user.uid)
                _currentUser.value = firestoreUser ?: user
                _isLogined.value = true
            } else {
                _isLogined.value = false
            }
        }
    }


    // LOGOUT
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _isLogined.value = false
            _currentUser.value = null
            _message.value = "Đã đăng xuất!"
        }
    }


    //  MESSAGE CONTROL
    fun setMessage(mess: String) {
        _message.value = mess
    }

    fun clearMessage() {
        _message.value = null
    }


    // REGISTER EMAIL
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

                else -> {
                    try {
                        val user = repository.registerWithEmail(email, password1, name)
                        if (user != null) {
                            _message.value =
                                "Đăng ký thành công! Vui lòng xác minh email trước khi đăng nhập."
                        } else {
                            _message.value = "Đăng ký thất bại!"
                        }
                    } catch (e: Exception) {
                        _message.value = "Lỗi đăng ký: ${e.message}"
                    }
                }
            }
        }

    }

    // CẬP NHẬT USER
    fun updateUserInfo(updatedUser: User) {
        viewModelScope.launch {
            val success = repository.updateUserInfo(updatedUser.uid, updatedUser)
            if (success) {
                _currentUser.value = updatedUser
                _message.value = "Cập nhật thông tin thành công!"
            } else {
                _message.value = "Lỗi cập nhật thông tin!"
            }
        }
    }

}