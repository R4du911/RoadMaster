package com.example.roadmaster.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.GetQuestionRequestDTO
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

class QuizActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timeText: TextView = findViewById(R.id.timer)

        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeText.text =
                    buildString {
                        append("Timp ramas ")
                        append(((millisUntilFinished / 1000) / 60))
                        append(" : ")
                        append((millisUntilFinished / 1000))
                    }
            }

            override fun onFinish() {
                timeText.text = "Findish"
            }
        }
        timer.start()

        val nextQuestion: Button = findViewById(R.id.next_question)

        nextQuestion.setOnClickListener {
            lifecycleScope.launch { getQuestion() }
        }
        nextQuestion.performClick();
    }

    private suspend fun getQuestion() {
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/question")
            {
                contentType(ContentType.Application.Json)
                body = GetQuestionRequestDTO("A2")
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")

                val question: TextView = findViewById(R.id.question)
                question.text = userData.getString("text")

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}