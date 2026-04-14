package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.expensetracker.data.Expense
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private val viewModel: ExpenseViewModel by viewModels()

    private lateinit var etTitle: TextInputEditText
    private lateinit var etAmount: TextInputEditText
    private lateinit var etDate: TextInputEditText
    private lateinit var etNote: TextInputEditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSave: MaterialButton
    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilAmount: TextInputLayout

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        window.statusBarColor = getColor(R.color.primary)

        setupToolbar()
        setupViews()
        setupCategorySpinner()
        setupDatePicker()
        setupSaveButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupViews() {
        etTitle = findViewById(R.id.etTitle)
        etAmount = findViewById(R.id.etAmount)
        etDate = findViewById(R.id.etDate)
        etNote = findViewById(R.id.etNote)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSave = findViewById(R.id.btnSave)
        tilTitle = etTitle.parent.parent as TextInputLayout
        tilAmount = etAmount.parent.parent as TextInputLayout

        // Default date: today
        setDateText(calendar)
    }

    private fun setupCategorySpinner() {
        val categories = resources.getStringArray(R.array.categories)
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter
    }

    private fun setupDatePicker() {
        etDate.setOnClickListener { showDatePicker() }

        val tilDate = etDate.parent.parent
        if (tilDate is com.google.android.material.textfield.TextInputLayout) {
            tilDate.setEndIconOnClickListener { showDatePicker() }
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                setDateText(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setDateText(cal: Calendar) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        etDate.setText(sdf.format(cal.time))
    }

    private fun setupSaveButton() {
        btnSave.setOnClickListener {
            if (validateInputs()) {
                saveExpense()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            tilTitle.error = getString(R.string.error_empty_title)
            isValid = false
        } else {
            tilTitle.error = null
        }

        val amountStr = etAmount.text.toString().trim()
        if (amountStr.isEmpty()) {
            tilAmount.error = getString(R.string.error_empty_amount)
            isValid = false
        } else {
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                tilAmount.error = getString(R.string.error_invalid_amount)
                isValid = false
            } else {
                tilAmount.error = null
            }
        }

        return isValid
    }

    private fun saveExpense() {
        val expense = Expense(
            title = etTitle.text.toString().trim(),
            amount = etAmount.text.toString().trim().toDouble(),
            category = spinnerCategory.selectedItem.toString(),
            date = etDate.text.toString(),
            note = etNote.text.toString().trim()
        )

        viewModel.addExpense(expense)
        Toast.makeText(this, "Expense saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}