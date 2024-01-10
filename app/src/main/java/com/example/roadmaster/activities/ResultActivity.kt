package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.ResultDTO
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val category = intent.getStringExtra("category").orEmpty()
        val answeredQuestions = intent.getIntExtra("answered_questions", 0)
        val nrWrong = intent.getIntExtra("wrong_questions", 0)
        val nrCorrect = answeredQuestions - nrWrong
        val percentageCorrect = (nrCorrect / maxQuestions) * 100
        val currentDate = LocalDateTime.now().format(formatter)
        val resultString = if (nrCorrect > 21) "Admis" else "Respins"

        findViewById<TextView>(R.id.category_value).text = category
        findViewById<TextView>(R.id.right_ans_value).text = nrCorrect.toString()
        findViewById<TextView>(R.id.wrong_ans_value).text = nrWrong.toString()
        findViewById<TextView>(R.id.percentage_value).text = percentageCorrect.toString()
        findViewById<TextView>(R.id.date_value).text = currentDate
        findViewById<TextView>(R.id.result_value).text = resultString

        val userId = intent.getStringExtra("user")?.let { JSONObject(it) }?.getString("id")

        findViewById<Button>(R.id.back_to_menu).setOnClickListener {
            lifecycleScope.launch {
                sendResult(
                    ResultDTO(
                        userId.orEmpty(),
                        category,
                        nrCorrect,
                        nrWrong,
                        percentageCorrect,
                        resultString
                    )
                )
            }
        }
    }

    private suspend fun sendResult(result: ResultDTO) {
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/result")
            {
                contentType(ContentType.Application.Json)
                body = result
            }
            if (response.status.isSuccess()) {

                val homeActivity = Intent(this, HomeActivity::class.java)
                homeActivity.putExtra("user", intent.getStringExtra("user"))
                startActivity(homeActivity)

            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}