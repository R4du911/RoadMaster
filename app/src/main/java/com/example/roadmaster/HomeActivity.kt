package com.example.roadmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toChangePasswordButton: ImageButton = findViewById(R.id.changePasswordHomeButton)
        toChangePasswordButton.setOnClickListener{
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
    }
}