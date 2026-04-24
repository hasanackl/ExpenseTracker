package com.example.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Expense
import com.example.expensetracker.data.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val repository = ExpenseRepository()

    val allExpenses: Flow<List<Expense>> = repository.allExpenses
    val totalAmount: Flow<Double> = repository.totalAmount

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
}