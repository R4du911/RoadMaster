package com.example.roadmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val linkBackToHomeArrow: ImageButton = findViewById(R.id.backToHomeButton)
        linkBackToHomeArrow.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        val saveNewPasswordButton: Button = findViewById(R.id.saveButton)
        saveNewPasswordButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


        val editTextOldPassword: EditText = findViewById(R.id.oldPasswordInput)
        val oldPasswordInput: String = editTextOldPassword.text.toString()

        val editTextNewPassword: EditText = findViewById(R.id.newPasswordInput)
        val newPasswordInput: String = editTextNewPassword.text.toString()

        val editTextRepeatNewPassword: EditText = findViewById(R.id.repeatNewPasswordInput)
        val repeatNewPasswordInput: String = editTextRepeatNewPassword.text.toString()

    }
}