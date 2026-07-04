package com.example.chernyavskoy_v_4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PersonalData : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: MaterialButton
    private lateinit var btnStatus: MaterialButton
    private lateinit var btnLocation: MaterialButton
    private lateinit var btnSettings: ImageButton
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_data)

        dbHelper = DBHelper(this)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)
        btnStatus = findViewById(R.id.btnStatus)
        btnLocation = findViewById(R.id.btnLocation)
        btnSettings = findViewById(R.id.btnSettings)

        val username = intent.getStringExtra("username") ?: "Пользователь"
        tvWelcome.text = android.text.Html.fromHtml("Привет, <font color='#FFC222'>$username</font>!", android.text.Html.FROM_HTML_MODE_LEGACY)

        // Logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, SessionManager::class.java)
            startActivity(intent)
            finish()
        }

        // Online Status
        btnStatus.setOnClickListener {
            Toast.makeText(this, "Вы находитесь в режиме онлайн", Toast.LENGTH_SHORT).show()
        }

        // Implicit Intent to Map Application
        btnLocation.setOnClickListener {
            val lat = "55.751950"
            val lon = "37.618585"
            val address = "Моховая улица, 9с9"
            val mapUri = Uri.parse("geo:$lat,$lon?q=" + Uri.encode(address))
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            
            // Set package to Google Maps specifically, but fall back if not available
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            } catch (e: Exception) {
                // Fallback: launch browser or any map viewer
                val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lon"))
                try {
                    startActivity(webMapIntent)
                } catch (ex: Exception) {
                    Toast.makeText(this, "Картографическое приложение не найдено", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Open settings
        btnSettings.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Handle Settings preferences
        applySettings()
    }

    private fun applySettings() {
        val sharedPrefs = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val showNews = sharedPrefs.getBoolean("show_news", true)

        val newsItem1 = findViewById<LinearLayout>(R.id.newsItem1)
        val newsItem2 = findViewById<LinearLayout>(R.id.newsItem2)

        val visibility = if (showNews) View.VISIBLE else View.GONE
        newsItem1.visibility = visibility
        newsItem2.visibility = visibility
    }
}
