package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.roadmaster.R

class ChangePasswordActivity : AppCompatActivity() {

    private var editTextOldPassword: EditText? = null
    private var editTextNewPassword: EditText? = null
    private var editTextRepeatNewPassword: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val linkBackToHomeArrow: ImageButton = findViewById(R.id.backToHomeButton)
        linkBackToHomeArrow.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        editTextOldPassword = findViewById(R.id.oldPasswordInput)
        editTextNewPassword = findViewById(R.id.newPasswordInput)
        editTextRepeatNewPassword = findViewById(R.id.repeatNewPasswordInput)

        val saveNewPasswordButton: Button = findViewById(R.id.saveButton)
        saveNewPasswordButton.setOnClickListener {
            val areAllFieldsValid = checkAllFields()

            if(areAllFieldsValid){
                val oldPasswordInput: String = editTextOldPassword?.text.toString()
                val newPasswordInput: String = editTextNewPassword?.text.toString()
                val repeatNewPasswordInput: String = editTextRepeatNewPassword?.text.toString()

                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun checkAllFields() : Boolean {
        var allFieldsAreValid = true

        if(editTextOldPassword!!.length() == 0){
            editTextOldPassword!!.error = "Acest camp este obligatoriu"
            allFieldsAreValid = false
        }

        if(editTextNewPassword!!.length() == 0){
            editTextNewPassword!!.error = "Acest camp este obligatoriu"
            allFieldsAreValid = false
        }

        if(editTextRepeatNewPassword!!.length() == 0){
            editTextRepeatNewPassword!!.error = "Acest camp este obligatoriu"
            allFieldsAreValid = false
        }

        if(editTextOldPassword!!.text.toString() == editTextNewPassword!!.text.toString()){
            editTextNewPassword!!.error = "Parola noua nu poate fi aceeasi cu cea veche"
            allFieldsAreValid = false
        }

        if(editTextNewPassword!!.text.toString() != editTextRepeatNewPassword!!.text.toString()){
            editTextNewPassword!!.error = "Parola noua introdusa nu este la fel"
            editTextRepeatNewPassword!!.error = "Parola noua introdusa nu este la fel"
            allFieldsAreValid = false
        }

        return allFieldsAreValid
    }
}