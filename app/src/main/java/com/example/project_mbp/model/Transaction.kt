package com.example.project_mbp.model

import java.util.Date

data class Transaction(
    var id: String = "",
    val tenGiaoDich: String = "",
    val soTien: Double = 0.0,
    val nhom: String = "Khác",
    val ghiChu: String = "",
    val loai: String = "chi",

    // Ngày giờ do người dùng chọn
    val ngay: String = "",  // dd/MM/yyyy
    val gio: String = "",   // HH:mm:ss

    // Thời gian tạo tự động của Firestore
    val ngayTao: Date = Date()
)
