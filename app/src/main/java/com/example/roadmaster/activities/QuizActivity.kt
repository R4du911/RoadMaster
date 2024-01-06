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


private const val maxQuestions: Int = 26;

class QuizActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var questionCounter: Int = 0;
    private var questionIds: MutableList<Int> = mutableListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timeText: TextView = findViewById(R.id.timer)

        val timer = object : CountDownTimer(1_800_000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeText.text =
                    buildString {
                        append("Timp ramas ")
                        append(((millisUntilFinished / 1000) / 60))
                        append(" : ")
                        append((millisUntilFinished / 1000) % 60)
                    }
            }

            override fun onFinish() {
                timeText.text = "Findish"
            }
        }
        timer.start()

        questionCounter = 0
        val nextQuestion: Button = findViewById(R.id.next_question)
        val category = intent.getStringExtra("category");

        nextQuestion.setOnClickListener {
            lifecycleScope.launch { getQuestion(category) }
            if (questionCounter == 0) {
                questionCounter++
                val q_counter: TextView = findViewById(R.id.question_counter)
                q_counter.text = buildString {
                    append(questionCounter)
                    append("/")
                    append(maxQuestions)
                }
            }
            else if (questionCounter == maxQuestions)
            {
                val q_counter: TextView = findViewById(R.id.question_counter)
                q_counter.text = buildString {
                    append("DOne")
                }
            }
            else
            {
                questionCounter++
                val q_counter: TextView = findViewById(R.id.question_counter)
                q_counter.text = buildString {
                    append(questionCounter)
                    append("/")
                    append(maxQuestions)
                }
            }
        }
        nextQuestion.performClick();
    }

    private suspend fun getQuestion(category: String?) {
        try {
            while (true) {
                val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/question")
                {
                    contentType(ContentType.Application.Json)
                    body = GetQuestionRequestDTO(category)
                }

                if  (response.status.isSuccess()) {

                    val json = JSONObject(response.readText())
                    val questionData = json.getJSONObject("data")

                    val questionId = questionData.getInt("id")
                    if (questionId in questionIds) {
                        continue
                    }

                    questionIds.add(questionId)

                    val question: TextView = findViewById(R.id.question)
                    question.text = questionData.getString("text")

                    val answers = questionData.getJSONArray("answers");
                    val checkBoxes: Array<CheckBox> = arrayOf(
                        findViewById(R.id.answer1),
                        findViewById(R.id.answer2),
                        findViewById(R.id.answer3),
                        findViewById(R.id.answer4)
                    )

                    for (i in 0 until answers.length()) {
                        val answer = answers.getJSONArray(i)
                        checkBoxes[i].text = answer.getString(0);
                    }

                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}