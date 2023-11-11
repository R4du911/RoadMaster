package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.roadmaster.R


class LoginActivity : AppCompatActivity() {

    private var editTextUser: EditText? = null
    private var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val linkToRegisterTextView: TextView = findViewById(R.id.LinkToRegister)
        linkToRegisterTextView.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        editTextUser = findViewById(R.id.LoginUserInput)
        editTextPassword = findViewById(R.id.LoginPasswordInput)

        val loginButton: Button = findViewById(R.id.LoginButton)
        loginButton.setOnClickListener {

            val areAllFieldsValid = checkAllFields()

            if(areAllFieldsValid){
                val userInput: String = editTextUser?.text.toString()
                val passwordInput: String = editTextPassword?.text.toString()

                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun checkAllFields() : Boolean {
        var areAllFieldsValid = true

        if(editTextUser!!.length() == 0){
            editTextUser!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        if(editTextPassword!!.length() == 0){
            editTextPassword!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        return areAllFieldsValid
    }
}