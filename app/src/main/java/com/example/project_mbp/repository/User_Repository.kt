package com.example.project_mbp.repository

import com.example.project_mbp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class User_Repository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Đăng nhập bằng Google
    suspend fun loginWithGoogle(idToken: String): User? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: return null

        val uid = firebaseUser.uid
        val userDoc = db.collection("users").document(uid).get().await()

        val user = if (userDoc.exists()) {
            userDoc.toObject(User::class.java)
        } else {
            val newUser = User(
                uid = uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: "",
                avatarUrl = firebaseUser.photoUrl?.toString()
            )
            db.collection("users").document(uid).set(newUser).await()
            newUser
        }

        return user
    }

    // Đăng nhập bằng Email + Password
    suspend fun loginWithEmail(email: String, password: String): User? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: return null

        // THÊM KIỂM TRA EMAIL ĐÃ XÁC MINH CHƯA
        if (!firebaseUser.isEmailVerified) {
            auth.signOut() // đăng xuất ngay để tránh login “ảo”
            throw Exception("Email chưa được xác minh. Vui lòng kiểm tra hộp thư Gmail của bạn.")
        }

        val uid = firebaseUser.uid
        val userDoc = db.collection("users").document(uid).get().await()

        return if (userDoc.exists()) {
            userDoc.toObject(User::class.java)
        } else {
            val newUser = User(
                uid = uid,
                email = email,
                name = firebaseUser.displayName ?: "",
                avatarUrl = firebaseUser.photoUrl?.toString()
            )
            db.collection("users").document(uid).set(newUser).await()
            newUser
        }
    }

    // Lấy user theo UID
    suspend fun getUserByUid(uid: String): User? {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Lấy current user từ FirebaseAuth
    fun getCurrentUser(): User? {
        val user = auth.currentUser
        return user?.let {
            User(
                uid = it.uid,
                email = it.email ?: "",
                name = it.displayName ?: "",
                avatarUrl = it.photoUrl?.toString()
            )
        }
    }

    // Đăng xuất
    suspend fun logout() {
        auth.signOut()
    }

    // Đăng ký tài khoản Email + Password
    suspend fun registerWithEmail(email: String, password: String, name: String): User? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return null

            // Gửi email xác minh
            user.sendEmailVerification().await()

            val newUser = User(
                uid = user.uid,
                email = email,
                name = name
            )
            db.collection("users").document(user.uid).set(newUser).await()
            newUser
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    //check mail xac minh
    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }
    // Cập nhật thông tin user
    suspend fun updateUserInfo(uid: String, updatedUser: User): Boolean {
        return try {
            db.collection("users").document(uid).set(updatedUser).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Lấy user từ Firestore
    suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
