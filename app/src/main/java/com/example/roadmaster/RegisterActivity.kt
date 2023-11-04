package com.example.roadmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val linkBackToLoginArrow: ImageButton = findViewById(R.id.BackToLoginButton)
        linkBackToLoginArrow.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}