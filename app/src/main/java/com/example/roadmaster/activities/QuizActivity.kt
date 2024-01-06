package com.example.roadmaster.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.CheckBox
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
import java.util.Vector

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
        val myIntent = intent;
        val category = intent.getStringExtra("category");

        nextQuestion.setOnClickListener {
            lifecycleScope.launch { getQuestion(category) }
        }
        nextQuestion.performClick();
    }

    private suspend fun getQuestion(category : String?) {
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/question")
            {
                contentType(ContentType.Application.Json)
                body = GetQuestionRequestDTO(category)
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")

                val question: TextView = findViewById(R.id.question)
                question.text = userData.getString("text")

                val answers = userData.getJSONArray("answers");
                val checkBoxes :  Array<CheckBox> = arrayOf(
                    findViewById(R.id.answer1),
                    findViewById(R.id.answer2),
                    findViewById(R.id.answer3),
                    findViewById(R.id.answer4))

                for (i in 0 until answers.length())
                {
                    val answer = answers.getJSONArray(i)
                    checkBoxes[i].text = answer.getString(0);
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}