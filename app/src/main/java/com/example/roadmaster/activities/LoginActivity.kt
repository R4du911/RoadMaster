package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.UserLoginRequestDTO
import com.example.roadmaster.model.UserRegisterRequestDTO
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


class LoginActivity : AppCompatActivity() {

    private var editTextUser: EditText? = null
    private var editTextPassword: EditText? = null
    private var errorTextView: TextView? = null

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val linkToRegisterTextView: TextView = findViewById(R.id.LinkToRegister)
        linkToRegisterTextView.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        editTextUser = findViewById(R.id.LoginUserInput)
        editTextPassword = findViewById(R.id.LoginPasswordInput)
        errorTextView = findViewById(R.id.errorTextView)

        val loginButton: Button = findViewById(R.id.LoginButton)
        loginButton.setOnClickListener {

            val areAllFieldsValid = checkAllFields()

            if(areAllFieldsValid){
                val userInput: String = editTextUser?.text.toString()
                val passwordInput: String = editTextPassword?.text.toString()

                lifecycleScope.launch {
                    loginPost(UserLoginRequestDTO(userInput, userInput, passwordInput))
                }
            }
        }
    }

    private suspend fun loginPost(user: UserLoginRequestDTO){
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/login") {
                contentType(ContentType.Application.Json)
                body = user
            }
            if (response.status.isSuccess()) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else if (response.status.value == 401) {
                showErrorMessage("Incorrect credentials. Please try again.")
            } else {
                println("Login failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //TODO:Can t to show message incorrect credentials
    private fun showErrorMessage(message: String) {
        errorTextView?.text = message
        errorTextView?.visibility = View.VISIBLE
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