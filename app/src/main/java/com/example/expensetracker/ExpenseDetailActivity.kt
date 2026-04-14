package com.example.expensetracker

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.expensetracker.data.Expense
import com.google.android.material.button.MaterialButton

class ExpenseDetailActivity : AppCompatActivity() {

    private val viewModel: ExpenseViewModel by viewModels()

    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailAmount: TextView
    private lateinit var tvDetailCategory: TextView
    private lateinit var tvDetailDate: TextView
    private lateinit var tvDetailNote: TextView
    private lateinit var tvDetailCategoryIcon: TextView
    private lateinit var btnDetailDelete: MaterialButton

    companion object {
        const val EXTRA_EXPENSE_ID = "expense_id"
        const val EXTRA_EXPENSE_TITLE = "expense_title"
        const val EXTRA_EXPENSE_AMOUNT = "expense_amount"
        const val EXTRA_EXPENSE_CATEGORY = "expense_category"
        const val EXTRA_EXPENSE_DATE = "expense_date"
        const val EXTRA_EXPENSE_NOTE = "expense_note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_detail)

        window.statusBarColor = getColor(R.color.primary)

        setupToolbar()
        setupViews()
        loadExpenseData()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupViews() {
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailAmount = findViewById(R.id.tvDetailAmount)
        tvDetailCategory = findViewById(R.id.tvDetailCategory)
        tvDetailDate = findViewById(R.id.tvDetailDate)
        tvDetailNote = findViewById(R.id.tvDetailNote)
        tvDetailCategoryIcon = findViewById(R.id.tvDetailCategoryIcon)
        btnDetailDelete = findViewById(R.id.btnDetailDelete)
    }

    private fun loadExpenseData() {
        val id = intent.getIntExtra(EXTRA_EXPENSE_ID, 0)
        val title = intent.getStringExtra(EXTRA_EXPENSE_TITLE) ?: ""
        val amount = intent.getDoubleExtra(EXTRA_EXPENSE_AMOUNT, 0.0)
        val category = intent.getStringExtra(EXTRA_EXPENSE_CATEGORY) ?: ""
        val date = intent.getStringExtra(EXTRA_EXPENSE_DATE) ?: ""
        val note = intent.getStringExtra(EXTRA_EXPENSE_NOTE) ?: ""

        tvDetailTitle.text = title
        tvDetailAmount.text = "$${String.format("%.2f", amount)}"
        tvDetailCategory.text = category
        tvDetailDate.text = date
        tvDetailNote.text = note.ifEmpty { "-" }
        tvDetailCategoryIcon.text = getCategoryEmoji(category)

        val expense = Expense(id, title, amount, category, date, note)

        btnDetailDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete \"$title\"?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteExpense(expense)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun getCategoryEmoji(category: String): String {
        return when (category) {
            "Food" -> "🍔"
            "Transport" -> "🚌"
            "Entertainment" -> "🎬"
            "Health" -> "💊"
            else -> "💰"
        }
    }
}