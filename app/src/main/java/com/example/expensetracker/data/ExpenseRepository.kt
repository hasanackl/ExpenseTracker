package com.example.expensetracker.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
    val totalAmount: Flow<Double?> = dao.getTotalAmount()

    suspend fun insert(expense: Expense) {
        dao.insertExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        dao.deleteExpense(expense)
    }

    fun getByCategory(category: String): Flow<List<Expense>> {
        return dao.getExpensesByCategory(category)
    }
}