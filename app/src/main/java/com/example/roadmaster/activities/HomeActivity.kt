package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.roadmaster.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        println(userData)


        val toChangePasswordButton: ImageButton = findViewById(R.id.changePasswordHomeButton)
        toChangePasswordButton.setOnClickListener{
            val changePasswordActivity = Intent(this, ChangePasswordActivity::class.java)
            changePasswordActivity.putExtra("user", userData.toString())

            startActivity(changePasswordActivity)
        }

        val logoutButton: ImageButton = findViewById(R.id.logoutHomeButton)
        logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}