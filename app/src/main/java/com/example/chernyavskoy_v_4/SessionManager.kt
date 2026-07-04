package com.example.chernyavskoy_v_4

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SessionManager : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAuthorize: MaterialButton
    private lateinit var btnRegister: TextView
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_manager)

        dbHelper = DBHelper(this)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnAuthorize = findViewById(R.id.btnAuthorize)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            handleRegistration()
        }

        btnAuthorize.setOnClickListener {
            handleAuthorization()
        }

        // Password visibility toggle
        val btnPasswordToggle = findViewById<android.widget.ImageButton>(R.id.btnPasswordToggle)
        var isPasswordVisible = false
        btnPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance()
            } else {
                etPassword.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }

    private fun handleRegistration() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(this, "Имя пользователя не должно быть пустым", Toast.LENGTH_SHORT).show()
            return
        }

        if (username.length > 30) {
            Toast.makeText(this, "Имя пользователя должно быть не более 30 символов", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Пароль должен быть не менее 8 символов", Toast.LENGTH_SHORT).show()
            return
        }

        val hasUppercase = password.any { it.isUpperCase() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        if (!hasUppercase) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну заглавную букву (A-Z)", Toast.LENGTH_SHORT).show()
            return
        }

        if (!hasSpecial) {
            Toast.makeText(this, "Пароль должен содержать хотя бы один спецсимвол", Toast.LENGTH_SHORT).show()
            return
        }

        val result = dbHelper.addUser(username, password)
        if (result != -1L) {
            Toast.makeText(this, "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ошибка регистрации пользователя", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAuthorization() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show()
            return
        }

        val isValidUser = dbHelper.checkUser(username, password)
        if (isValidUser) {
            val intent = Intent(this, PersonalArea::class.java).apply {
                putExtra("username", username)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT).show()
        }
    }
}
