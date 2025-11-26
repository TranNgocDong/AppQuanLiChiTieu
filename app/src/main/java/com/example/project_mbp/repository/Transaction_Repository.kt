package com.example.project_mbp.repository

import android.util.Log
import com.example.project_mbp.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
class Transaction_Repository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Lấy UID của user hiện tại
    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    // Thêm giao dịch mới
    suspend fun addTransaction(transaction: Transaction): Boolean {
        val userId = getUserId() ?: return false
        return try {
            db.collection("users").document(userId)
                .collection("transactions")
                .add(transaction)
                .await()
            true
        } catch (e: Exception) {
            Log.w("TransactionRepo", "Error adding document", e)
            false
        }
    }

    // Lấy danh sách giao dịch (Flow real-time)
    fun getTransactions(): Flow<List<Transaction>> = callbackFlow {
        val userId = getUserId()
        if (userId == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val collection = db.collection("users").document(userId)
            .collection("transactions")
            .orderBy("ngayTao", Query.Direction.DESCENDING)

        val registration = collection.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("TransactionRepo", "Listen failed.", e)
                close(e)
                return@addSnapshotListener
            }

            val transactionList = snapshots?.map { doc ->
                doc.toObject(Transaction::class.java).apply { id = doc.id }
            } ?: emptyList()

            trySend(transactionList)
        }

        awaitClose { registration.remove() }
    }

    //Xóa giao dịch
    suspend fun deleteTransaction(transactionId: String): Boolean {
        val userId = getUserId() ?: return false
        return try {
            db.collection("users").document(userId)
                .collection("transactions")
                .document(transactionId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            Log.w("TransactionRepo", "Error deleting document", e)
            false
        }
    }
}
