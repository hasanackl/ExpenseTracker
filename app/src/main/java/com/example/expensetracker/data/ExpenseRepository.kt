package com.example.expensetracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ExpenseRepository {

    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val collection = db.collection("expenses")

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    val allExpenses: Flow<List<Expense>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val expenses = snapshot?.documents?.map { doc ->
                    Expense(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        amount = doc.getDouble("amount") ?: 0.0,
                        category = doc.getString("category") ?: "",
                        date = doc.getString("date") ?: "",
                        note = doc.getString("note") ?: ""
                    )
                } ?: emptyList()
                trySend(expenses)
            }
        awaitClose { listener.remove() }
    }

    val totalAmount: Flow<Double> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val total = snapshot?.documents?.sumOf { it.getDouble("amount") ?: 0.0 } ?: 0.0
                trySend(total)
            }
        awaitClose { listener.remove() }
    }

    suspend fun insert(expense: Expense) {
        val data = hashMapOf(
            "userId" to userId,
            "title" to expense.title,
            "amount" to expense.amount,
            "category" to expense.category,
            "date" to expense.date,
            "note" to expense.note,
            "timestamp" to com.google.firebase.Timestamp.now()
        )
        collection.add(data).await()
    }

    suspend fun delete(expense: Expense) {
        collection.document(expense.id).delete().await()
    }
}