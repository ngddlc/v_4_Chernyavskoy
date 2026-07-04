package com.example.chernyavskoy_v_4

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PersonalArea : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnTakeSnapshot: MaterialButton
    private lateinit var btnLogout: MaterialButton
    private lateinit var btnSettings: ImageButton
    private lateinit var rvUsers: RecyclerView
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_area)

        dbHelper = DBHelper(this)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnTakeSnapshot = findViewById(R.id.btnTakeSnapshot)
        btnLogout = findViewById(R.id.btnLogout)
        btnSettings = findViewById(R.id.btnSettings)
        rvUsers = findViewById(R.id.rvUsers)

        val username = intent.getStringExtra("username") ?: "Андрей"
        tvWelcome.text = android.text.Html.fromHtml("Привет, <font color='#FF5630'>$username</font>!", android.text.Html.FROM_HTML_MODE_LEGACY)

        // Control buttons logic
        btnLogout.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        btnTakeSnapshot.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivity(cameraIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Приложение камеры не найдено", Toast.LENGTH_SHORT).show()
            }
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        // Setup users RecyclerView
        setupUsersList()
    }

    override fun onResume() {
        super.onResume()
        // Refresh users list (in case a new user registered)
        setupUsersList()
        // Handle Settings preferences
        applySettings()
    }

    private fun setupUsersList() {
        val users = dbHelper.getAllUsers()
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = UserAdapter(users)
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

    // Inner class for RecyclerView Adapter
    private class UserAdapter(private val users: List<String>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.tvUsername.text = users[position]
        }

        override fun getItemCount(): Int {
            return users.size
        }
    }
}
