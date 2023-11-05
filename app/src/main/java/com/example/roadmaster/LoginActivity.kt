package com.example.roadmaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val linkToRegisterTextView: TextView = findViewById(R.id.LinkToRegister)
        linkToRegisterTextView.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val loginButton: Button = findViewById(R.id.LoginButton)
        loginButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}