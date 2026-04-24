package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.statusBarColor = getColor(R.color.primary)

        auth = FirebaseAuth.getInstance()
        setupViews()
    }

    private fun setupViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener { register() }
        tvLogin.setOnClickListener { finish() }
    }

    private fun register() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (email.isEmpty()) {
            tilEmail.error = "Please enter your email"
            return
        } else tilEmail.error = null

        if (password.isEmpty()) {
            tilPassword.error = "Please enter a password"
            return
        } else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            return
        } else tilPassword.error = null

        if (confirmPassword != password) {
            tilConfirmPassword.error = "Passwords do not match"
            return
        } else tilConfirmPassword.error = null

        btnRegister.isEnabled = false
        btnRegister.text = "Creating account..."

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                btnRegister.isEnabled = true
                btnRegister.text = "Create Account"
                Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}