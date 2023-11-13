package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.UserLoginRequestDTO
import com.example.roadmaster.model.UserResetPasswordRequest
import com.example.roadmaster.model.UserResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {

    private var editTextOldPassword: EditText? = null
    private var editTextNewPassword: EditText? = null
    private var editTextRepeatNewPassword: EditText? = null

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")
        val userDataJSON = userData?.let { JSONObject(it) }


        val linkBackToHomeArrow: ImageButton = findViewById(R.id.backToHomeButton)
        linkBackToHomeArrow.setOnClickListener{
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", userData.toString())

            startActivity(homeActivity)
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

                lifecycleScope.launch {
                    changePasswordPost(UserResetPasswordRequest(
                        userDataJSON?.getString("email"),
                        oldPasswordInput,
                        newPasswordInput,
                        repeatNewPasswordInput)
                    )
                }
            }
        }
    }

    private suspend fun changePasswordPost(userResetRequest: UserResetPasswordRequest){
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/reset-password") {
                contentType(ContentType.Application.Json)
                body = userResetRequest
            }
            if (response.status.isSuccess()) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                println("Login failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
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