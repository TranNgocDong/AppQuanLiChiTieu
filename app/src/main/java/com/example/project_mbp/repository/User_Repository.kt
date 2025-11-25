package com.example.project_mbp.repository

import android.net.Uri
import android.util.Log
import com.example.project_mbp.model.User
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class User_Repository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun loginWithFacebook(accessToken: String): User? {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: return null

        val uid = firebaseUser.uid
        val userDoc = db.collection("users").document(uid).get().await()

        val user = if (userDoc.exists()) {
            // Lấy thông tin User đã lưu
            userDoc.toObject(User::class.java)
        } else {
            // Tạo User mới và lưu vào Firestore
            val newUser = User(
                uid = uid,
                email = firebaseUser.email ?: "facebook_${uid}@temp.com", // Facebook có thể không trả về email
                name = firebaseUser.displayName ?: "",
                avatarUrl = firebaseUser.photoUrl?.toString()
            )
            db.collection("users").document(uid).set(newUser).await()
            newUser
        }

        return user
    }
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

    suspend fun registerWithEmail_returnUid(email: String, password: String, name: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return null

            // Gửi email xác minh (FirebaseAuth giữ user đã đăng ký, chưa verified)
            user.sendEmailVerification().await()

            // Trả về uid cho ViewModel để bắt đầu luồng kiểm tra xác minh
            user.uid
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Lưu user vào Firestore
    suspend fun saveUserToFirestore(uid: String, email: String, name: String): Boolean {
        return try {
            val newUser = User(uid = uid, email = email, name = name)
            db.collection("users").document(uid).set(newUser).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Gửi lại email xác minh (cho currentUser đã create)
    suspend fun resendVerificationEmail(): Boolean {
        return try {
            val user = auth.currentUser ?: return false
            user.sendEmailVerification().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Reload current Firebase user (để cập nhật trạng thái isEmailVerified)
    suspend fun reloadCurrentUser() {
        try {
            auth.currentUser?.reload()?.await()
        } catch (_: Exception) {
            // ignore
        }
    }

    // check current user verified
    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }

    // Cập nhật thông tin user
    suspend fun updateUserInfo(uid: String, updatedUser: User): Boolean {
        return try {
            // SỬA: Dùng SetOptions.merge()
            // Tác dụng: Nếu User chưa có -> Tạo mới. Nếu có rồi -> Chỉ cập nhật trường thay đổi.
            db.collection("users").document(uid)
                .set(updatedUser, SetOptions.merge())
                .await()

            Log.d("Repository", "Update thành công cho UID: $uid")
            true
        } catch (e: Exception) {
            // QUAN TRỌNG: In lỗi ra Logcat để bạn biết tại sao sai
            Log.e("Repository", "Lỗi updateUserInfo: ${e.message}")
            e.printStackTrace()
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
    suspend fun updateFirebaseProfile(name: String?, photoUrl: String?): Boolean {
        val user = auth.currentUser ?: return false

        val profileUpdates = UserProfileChangeRequest.Builder()
            .apply {
                if (name != null) setDisplayName(name)
                if (photoUrl != null) setPhotoUri(Uri.parse(photoUrl))
            }
            .build()

        return try {
            user.updateProfile(profileUpdates).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    suspend fun updatePassword(newPassword: String): Boolean {
        val user = auth.currentUser ?: return false
        return try {
            user.updatePassword(newPassword).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
