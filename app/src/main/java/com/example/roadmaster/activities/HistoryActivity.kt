package com.example.roadmaster.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.HistoryRequestDTO
import com.example.roadmaster.model.HistoryResponseDTO
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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class HistoryActivity : AppCompatActivity() {

    private var numberHistoriesView: TextView? = null

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")
        val userDataJSON = userData?.let { JSONObject(it) }

        val id = userDataJSON?.getString("id")

        lifecycleScope.launch {
            historyPost(HistoryRequestDTO(id))
        }

        val linkBackToHomeArrow: ImageButton = findViewById(R.id.backToHomeButtonFromHistory)
        linkBackToHomeArrow.setOnClickListener{
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", userData.toString())

            startActivity(homeActivity)
        }
    }


    private suspend fun historyPost(user: HistoryRequestDTO){
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/history") {
                contentType(ContentType.Application.Json)
                body = user
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")
                val historiesString = userData.getString("histories")

                val historiesList = Json.decodeFromString<List<HistoryResponseDTO>>(historiesString)

                val numberOfHistories = historiesList.size
                val numberHistoriesText = getString(R.string.number_histories, numberOfHistories)
                val spannableString = SpannableString(numberHistoriesText)
                val colorSpan = ForegroundColorSpan(Color.parseColor("#F18C42"))
                spannableString.setSpan(colorSpan, numberHistoriesText.indexOf(numberOfHistories.toString()), numberHistoriesText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                numberHistoriesView = findViewById(R.id.numberQuizesText)
                numberHistoriesView?.text = spannableString


            } else {
                println("History retrieval failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}