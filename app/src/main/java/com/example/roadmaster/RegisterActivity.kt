package com.example.roadmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val linkBackToLoginArrow: ImageButton = findViewById(R.id.BackToLoginButton)
        linkBackToLoginArrow.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val registerButton: Button = findViewById(R.id.RegisterButton)
        registerButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


        val editTextUser: EditText = findViewById(R.id.RegisterUserInput)
        val userInput: String = editTextUser.text.toString()

        val editTextEmail: EditText = findViewById(R.id.RegisterEmailInput)
        val emailInput: String = editTextEmail.text.toString()

        val editTextPassword: EditText = findViewById(R.id.RegisterPasswordInput)
        val passwordInput: String = editTextPassword.text.toString()

        val editTextRepeatPassword: EditText = findViewById(R.id.RegisterRepeatPasswordInput)
        val repeatPasswordInput: String = editTextRepeatPassword.text.toString()
    }
}