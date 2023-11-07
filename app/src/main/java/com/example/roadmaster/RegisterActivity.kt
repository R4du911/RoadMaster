package com.example.roadmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class RegisterActivity : AppCompatActivity() {

    private var editTextUser: EditText? = null;
    private var editTextEmail: EditText? = null;
    private var editTextPassword: EditText? = null;
    private var editTextRepeatPassword: EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val linkBackToLoginArrow: ImageButton = findViewById(R.id.BackToLoginButton)
        linkBackToLoginArrow.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        editTextUser = findViewById(R.id.RegisterUserInput)
        editTextEmail = findViewById(R.id.RegisterEmailInput)
        editTextPassword = findViewById(R.id.RegisterPasswordInput)
        editTextRepeatPassword = findViewById(R.id.RegisterRepeatPasswordInput)

        val registerButton: Button = findViewById(R.id.RegisterButton)
        registerButton.setOnClickListener {

            val areAllFieldsValid = checkAllFields()

            if(areAllFieldsValid){
                val userInput: String = editTextUser?.text.toString()
                val emailInput: String = editTextEmail?.text.toString()
                val passwordInput: String = editTextPassword?.text.toString()
                val repeatPasswordInput: String = editTextRepeatPassword?.text.toString()

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

        if(editTextEmail!!.length() == 0){
            editTextEmail!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        if(editTextPassword!!.length() == 0){
            editTextPassword!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        if(editTextRepeatPassword!!.length() == 0){
            editTextRepeatPassword!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        return areAllFieldsValid
    }
}