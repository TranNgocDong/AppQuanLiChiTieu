package com.example.project_mbp.repository


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khởi tạo DataStore với tên "settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeDataStoreRepository(context: Context) {

    private val dataStore = context.dataStore

    // Đây là "chìa khóa" để lưu/đọc giá trị theme
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    /**
     * MỘT FLOW ĐỂ ĐỌC GIÁ TRỊ THEME (BẠN BỊ THIẾU CÁI NÀY)
     * Nếu không có gì được lưu, nó sẽ trả về "SYSTEM" làm mặc định.
     */
    val themeModeFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.THEME_MODE] ?: "SYSTEM"
        }

    /**
     * Hàm (tạm dừng) để lưu giá trị theme mới ("LIGHT", "DARK", "SYSTEM").
     */
    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode
        }
    }
}