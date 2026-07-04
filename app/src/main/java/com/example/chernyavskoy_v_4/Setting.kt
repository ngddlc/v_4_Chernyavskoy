package com.example.chernyavskoy_v_4

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class Setting : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSettingsExit: ImageButton
    private lateinit var switchLocation: SwitchMaterial
    private lateinit var switchNotify: SwitchMaterial
    private lateinit var switchNews: SwitchMaterial
    private lateinit var btnReset: MaterialButton
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        sharedPrefs = getSharedPreferences("AppSettings", MODE_PRIVATE)

        btnBack = findViewById(R.id.btnBack)
        btnSettingsExit = findViewById(R.id.btnSettingsExit)
        switchLocation = findViewById(R.id.switchLocation)
        switchNotify = findViewById(R.id.switchNotify)
        switchNews = findViewById(R.id.switchNews)
        btnReset = findViewById(R.id.btnReset)

        btnBack.setOnClickListener {
            finish()
        }

        btnSettingsExit.setOnClickListener {
            val intent = Intent(this, SessionManager::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Set initial values without triggering listeners
        loadSettings()

        // Setup check listeners
        setupListeners()

        btnReset.setOnClickListener {
            resetSettings()
        }
    }

    private fun loadSettings() {
        switchLocation.isChecked = sharedPrefs.getBoolean("show_location", false)
        switchNotify.isChecked = sharedPrefs.getBoolean("show_notify", false)
        switchNews.isChecked = sharedPrefs.getBoolean("show_news", true)
    }

    private fun setupListeners() {
        switchLocation.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) {
                "Включено отображение моего местороложения"
            } else {
                "Отключено отображение моего местороложения"
            }
            showCustomToast(text)
            sharedPrefs.edit().putBoolean("show_location", isChecked).apply()
        }

        switchNotify.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) {
                "Включены уведомления о новом заказе"
            } else {
                "Отключены уведомления о новом заказе"
            }
            showCustomToast(text)
            sharedPrefs.edit().putBoolean("show_notify", isChecked).apply()
        }

        switchNews.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) {
                "Включена статистика моих заказов"
            } else {
                "Отключена статистика моих заказов"
            }
            showCustomToast(text)
            sharedPrefs.edit().putBoolean("show_news", isChecked).apply()
        }
    }

    private fun resetSettings() {
        // Clear listeners to avoid showing multiple Toasts when resetting
        switchLocation.setOnCheckedChangeListener(null)
        switchNotify.setOnCheckedChangeListener(null)
        switchNews.setOnCheckedChangeListener(null)

        // Reset UI state to false (unchecked)
        switchLocation.isChecked = false
        switchNotify.isChecked = false
        switchNews.isChecked = false

        // Update SharedPreferences
        sharedPrefs.edit().apply {
            putBoolean("show_location", false)
            putBoolean("show_notify", false)
            putBoolean("show_news", false)
            apply()
        }

        showCustomToast("Настройки сброшены")

        // Restore listeners
        setupListeners()
    }

    private fun showCustomToast(message: String) {
        try {
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, null)
            
            val tvToastMessage: TextView = layout.findViewById(R.id.tvToastMessage)
            tvToastMessage.text = message
            
            val toast = Toast(applicationContext)
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 120)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            // Fallback to standard Toast if inflation fails for any reason
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
