package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.primary)

        auth = FirebaseAuth.getInstance()

        // Zaten giriş yapmışsa direkt ana sayfaya git
        if (auth.currentUser != null) {
            goToMain()
            return
        }

        setContentView(R.layout.activity_login)
        setupViews()
    }

    private fun setupViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener { login() }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            tilEmail.error = "Please enter your email"
            return
        } else tilEmail.error = null

        if (password.isEmpty()) {
            tilPassword.error = "Please enter your password"
            return
        } else tilPassword.error = null

        btnLogin.isEnabled = false
        btnLogin.text = "Signing in..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                goToMain()
            }
            .addOnFailureListener { e ->
                btnLogin.isEnabled = true
                btnLogin.text = "Sign In"
                Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}