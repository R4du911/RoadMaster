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
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private var editTextEmail: EditText? = null
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

        editTextEmail = findViewById(R.id.LoginEmailInput)
        editTextPassword = findViewById(R.id.LoginPasswordInput)
        errorTextView = findViewById(R.id.errorTextView)

        val loginButton: Button = findViewById(R.id.LoginButton)
        loginButton.setOnClickListener {

            val areAllFieldsValid = checkAllFields()

            if(areAllFieldsValid){
                val emailInput: String = editTextEmail?.text.toString()
                val passwordInput: String = editTextPassword?.text.toString()

                lifecycleScope.launch {
                    loginPost(UserLoginRequestDTO(emailInput, emailInput, passwordInput))
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

                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")

                val homeActivity = Intent(this, HomeActivity::class.java)
                homeActivity.putExtra("user", userData.toString())

                startActivity(homeActivity)
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

        if(editTextEmail!!.length() == 0){
            editTextEmail!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        if(editTextPassword!!.length() == 0){
            editTextPassword!!.error = "Acest camp este obligatoriu"
            areAllFieldsValid = false
        }

        return areAllFieldsValid
    }
}