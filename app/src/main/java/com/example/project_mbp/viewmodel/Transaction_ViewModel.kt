package com.example.project_mbp.viewmodel

// THÊM CÁC IMPORT NÀY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_mbp.model.Transaction
import com.example.project_mbp.repository.Transaction_Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Transaction_ViewModel : ViewModel() {

    private val repository = Transaction_Repository()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _addTransactionStatus = MutableStateFlow<Pair<Boolean?, String?>>(null to null)
    val addTransactionStatus = _addTransactionStatus.asStateFlow()

    //-_- _deleteTransactionStatus: lưu trạng thái xóa
    //    deleteTransactionStatus: cho UI quan sát
    private val _deleteTransactionStatus = MutableStateFlow<Pair<Boolean?, String?>>(null to null)
    val deleteTransactionStatus = _deleteTransactionStatus.asStateFlow()
    //reset lại trạng thái sau khi xóa
    fun clearDeleteTransactionStatus() {
        _deleteTransactionStatus.value = null to null
    }

    //Gọi hàm này để xóa giao dịch
    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            val success = repository.deleteTransaction(transactionId)
            if (success) {
                _deleteTransactionStatus.value = true to "Xóa giao dịch thành công"
            } else {
                _deleteTransactionStatus.value = false to "Xóa giao dịch thất bại"
            }
        }
    }


    // HÀM MỚI: Để ghép ngày và giờ
    private fun parseDateTime(dateStr: String, timeStr: String): Date {
        return try {
            val dateTimeStr = "$dateStr $timeStr"
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            format.parse(dateTimeStr) ?: Date() // Nếu lỗi, trả về ngày giờ hiện tại
        } catch (e: Exception) {
            Date() // Nếu lỗi, trả về ngày giờ hiện tại
        }
    }

    init {
        listenToTransactions()
    }

    private fun listenToTransactions() {
        viewModelScope.launch {
            repository.getTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }

    // THAY ĐỔI HÀM NÀY: Thêm 2 tham số 'ngay' và 'gio'
    fun addTransaction(
        ten: String,
        soTien: String,
        nhom: String,
        ghiChu: String,
        loai: String,
        ngay: String, // Tham số mới
        gio: String  // Tham số mới
    ) {
        viewModelScope.launch {
            val soTienDouble = soTien.toDoubleOrNull()
            if (ten.isBlank() || soTienDouble    == null || soTienDouble == 0.0) {
                _addTransactionStatus.value = false to "Vui lòng nhập đầy đủ tên và số tiền"
                return@launch
            }

            // 1. Ghép ngày và giờ từ UI
            val ngayGiaoDich = parseDateTime(ngay, gio)

            // 2. Tạo đối tượng mới
            val newTransaction = Transaction(
                tenGiaoDich = ten,
                soTien = soTienDouble,
                nhom = nhom,
                ghiChu = ghiChu,
                loai = loai,
                ngayTao = ngayGiaoDich // <-- Dùng ngày giờ đã ghép
            )

            // 3. Gọi repository để lưu
            val success = repository.addTransaction(newTransaction)
            if (success) {
                _addTransactionStatus.value = true to "Thêm giao dịch thành công!"
            } else {
                _addTransactionStatus.value = false to "Thêm giao dịch thất bại!"
            }
        }
    }

    fun clearAddTransactionStatus() {
        _addTransactionStatus.value = null to null
    }
}