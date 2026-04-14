package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.adapter.ExpenseAdapter
import com.example.expensetracker.data.Expense
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: ExpenseViewModel by viewModels()
    private lateinit var adapter: ExpenseAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvExpenseCount: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var chipGroup: ChipGroup
    private lateinit var pieChart: PieChart

    private var currentFilter = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = getColor(R.color.primary)

        setupToolbar()
        setupViews()
        setupRecyclerView()
        observeData()

        fab.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerView)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvExpenseCount = findViewById(R.id.tvExpenseCount)
        tvEmpty = findViewById(R.id.tvEmpty)
        fab = findViewById(R.id.fab)
        chipGroup = findViewById(R.id.chipGroup)
        pieChart = findViewById(R.id.pieChart)
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(
            onDeleteClick = { expense -> showDeleteDialog(expense) },
            onItemClick = { expense -> openDetailScreen(expense) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.allExpenses.collectLatest { expenses ->
                val filtered = if (currentFilter == "All") expenses
                else expenses.filter { it.category == currentFilter }

                adapter.submitList(filtered)
                setupPieChart(expenses)

                if (filtered.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                val count = expenses.size
                tvExpenseCount.text = "$count transaction${if (count != 1) "s" else ""}"
            }
        }

        lifecycleScope.launch {
            viewModel.totalAmount.collectLatest { total ->
                val amount = total ?: 0.0
                tvTotalAmount.text = "$${String.format("%.2f", amount)}"
            }
        }

        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            currentFilter = when (checkedIds[0]) {
                R.id.chipFood -> "Food"
                R.id.chipTransport -> "Transport"
                R.id.chipEntertainment -> "Entertainment"
                R.id.chipHealth -> "Health"
                R.id.chipOther -> "Other"
                else -> "All"
            }
            lifecycleScope.launch {
                viewModel.allExpenses.collectLatest { expenses ->
                    val filtered = if (currentFilter == "All") expenses
                    else expenses.filter { it.category == currentFilter }

                    adapter.submitList(filtered)

                    if (filtered.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmpty.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupPieChart(expenses: List<Expense>) {
        val categoryTotals = expenses
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        if (categoryTotals.isEmpty()) {
            pieChart.visibility = View.GONE
            return
        }

        pieChart.visibility = View.VISIBLE

        val categoryColorMap = mapOf(
            "Food" to android.graphics.Color.parseColor("#FF9800"),
            "Transport" to android.graphics.Color.parseColor("#2196F3"),
            "Entertainment" to android.graphics.Color.parseColor("#9C27B0"),
            "Health" to android.graphics.Color.parseColor("#F44336"),
            "Other" to android.graphics.Color.parseColor("#607D8B")
        )

        val entries = categoryTotals.map { (category, total) ->
            PieEntry(total, category)
        }

        val colors = categoryTotals.keys.map { category ->
            categoryColorMap[category] ?: android.graphics.Color.parseColor("#607D8B")
        }

        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.WHITE
        }

        pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 40f
            setHoleColor(android.graphics.Color.TRANSPARENT)
            legend.isEnabled = true
            legend.textSize = 11f
            setEntryLabelColor(android.graphics.Color.WHITE)
            animateY(800)
            invalidate()
        }
    }

    private fun openDetailScreen(expense: Expense) {
        val intent = Intent(this, ExpenseDetailActivity::class.java).apply {
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_ID, expense.id)
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_TITLE, expense.title)
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_AMOUNT, expense.amount)
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_CATEGORY, expense.category)
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_DATE, expense.date)
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_NOTE, expense.note)
        }
        startActivity(intent)
    }

    private fun showDeleteDialog(expense: Expense) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete \"${expense.title}\"?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteExpense(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}