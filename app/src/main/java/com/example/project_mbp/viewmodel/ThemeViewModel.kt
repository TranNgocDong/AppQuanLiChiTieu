package com.example.project_mbp.viewmodel

import android.app.Application
import androidx.lifecycle.*
// Import Repository mà chúng ta vừa sửa ở Bước 1
import com.example.project_mbp.repository.ThemeDataStoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// LỚP NÀY QUẢN LÝ VIỆC ĐỌC/GHI THEME
class ThemeViewModel(private val repository: ThemeDataStoreRepository) : ViewModel() {

    // Đọc theme preference dưới dạng StateFlow
    val themeMode: StateFlow<String> = repository.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "SYSTEM"
    )

    // Hàm để cập nhật theme
    fun setThemeMode(themeMode: String) {
        viewModelScope.launch {
            repository.setThemeMode(themeMode)
        }
    }
}

// LỚP NÀY DÙNG ĐỂ KHỞI TẠO VIEWMODEL (MÀ MainActivity đang gọi)
class ThemeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(ThemeDataStoreRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}