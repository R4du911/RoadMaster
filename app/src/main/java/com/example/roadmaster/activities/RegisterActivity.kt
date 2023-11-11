package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.roadmaster.R
import com.example.roadmaster.model.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private var editTextUser: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var editTextRepeatPassword: EditText? = null

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

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

            if(checkAllFields()){
                val userInput: String = editTextUser?.text.toString()
                val emailInput: String = editTextEmail?.text.toString()
                val passwordInput: String = editTextPassword?.text.toString()

                lifecycleScope.launch {
                    registerPost(User(userInput, emailInput, passwordInput))
                }

                startActivity(Intent(this, HomeActivity::class.java))
            }

        }
    }

    private suspend fun registerPost(user: User){
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/register") {
                contentType(ContentType.Application.Json)
                body = user
            }
            println("Response status: ${response.status}")
        } catch (e: Exception) {
            e.printStackTrace()
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

        if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail!!.text.toString()).matches()){
            editTextEmail!!.error = "Email-ul nu este valid"
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

        if(editTextPassword!!.text.toString() != editTextRepeatPassword!!.text.toString()){
            editTextPassword!!.error = "Parola introdusa nu este la fel"
            editTextRepeatPassword!!.error = "Parola introdusa nu este la fel"
            areAllFieldsValid = false
        }

        return areAllFieldsValid
    }
}