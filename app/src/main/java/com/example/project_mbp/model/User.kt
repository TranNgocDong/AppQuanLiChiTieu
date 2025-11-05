package com.example.project_mbp.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val provider: String = "",
    val birthday: String? = null,
    val phone: String? = null

)
