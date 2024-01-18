package com.example.roadmaster.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.GetQuestionRequestDTO
import com.example.roadmaster.model.Question
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


const val maxQuestions: Int = 26

class QuizActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var questionCounter: Int = 0
    private var wrongQuestionsCounter: Int = 0
    private var answeredQuestions: MutableList<Question> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timeText: TextView = findViewById(R.id.timer)

        //timer
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

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                startActivity(getResultIntent())
            }
        }
        timer.start()

        questionCounter = 0
        wrongQuestionsCounter = 0

        val category = intent.getStringExtra("category")

        //logic for displaying the next question
        val nextQuestion: Button = findViewById(R.id.next_question)
        nextQuestion.setOnClickListener {
            when (questionCounter) {
                0 -> {
                    lifecycleScope.launch { getQuestion(category) }
                    questionCounter++
                    val q_counter: TextView = findViewById(R.id.question_counter)
                    q_counter.text = buildString {
                        append(questionCounter)
                        append("/")
                        append(maxQuestions)
                    }
                }

                maxQuestions -> {
                    registerResponse(questionCounter - 1)
                    startActivity(getResultIntent())
                }

                else -> {
                    registerResponse(questionCounter - 1)
                    lifecycleScope.launch { getQuestion(category) }
                    if (wrongQuestionsCounter == 6) {
                        startActivity(getResultIntent())
                    } else {
                        questionCounter++
                        val q_counter: TextView = findViewById(R.id.question_counter)
                        q_counter.text = buildString {
                            append(questionCounter)
                            append("/")
                            append(maxQuestions)
                        }
                    }
                }
            }
        }
        nextQuestion.performClick()
    }

    //function for going to the result section
    private fun getResultIntent(): Intent {
        val resultActivity = Intent(this@QuizActivity, ResultActivity::class.java)
        resultActivity.putExtra("user", intent.getStringExtra("user").toString())
        resultActivity.putExtra("category", intent.getStringExtra("category"))
        resultActivity.putExtra("answered_questions", questionCounter)
        resultActivity.putExtra("wrong_questions", wrongQuestionsCounter)
        resultActivity.putExtra("question_data", answeredQuestions.toTypedArray())

        return resultActivity
    }

    //request function for retrieving a question
    private suspend fun getQuestion(category: String?) {
        try {
            while (true) {
                val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/question")
                {
                    contentType(ContentType.Application.Json)
                    body = GetQuestionRequestDTO(category)
                }

                if (response.status.isSuccess()) {

                    val json = JSONObject(response.readText())
                    val questionData = json.getJSONObject("data")

                    val questionId = questionData.getInt("id")
                    if (answeredQuestions.filter { it.id == questionId }.size == 1) {
                        continue
                    }

                    val answeredQuestion = Question()
                    answeredQuestion.id = questionId
                    answeredQuestion.category = category.orEmpty()

                    val question: TextView = findViewById(R.id.question)
                    question.text = questionData.getString("text")
                    answeredQuestion.text = questionData.getString("text")

                    val answers = questionData.getJSONArray("answers")
                    val checkBoxes: Array<CheckBox> = arrayOf(
                        findViewById(R.id.answer1),
                        findViewById(R.id.answer2),
                        findViewById(R.id.answer3),
                        findViewById(R.id.answer4)
                    )

                    for (i in 0 until answers.length()) {
                        val answer = answers.getJSONArray(i)
                        checkBoxes[i].text = answer.getString(0)
                        answeredQuestion.answers.add(
                            Pair(
                                answer.getString(0),
                                answer.getBoolean(1)
                            )
                        )
                    }

                    answeredQuestions.add(
                        answeredQuestion
                    )

                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //logic for saving the response after each question
    private fun registerResponse(questionNumber: Int) {
        val answer1: CheckBox = findViewById(R.id.answer1)
        val answer2: CheckBox = findViewById(R.id.answer2)
        val answer3: CheckBox = findViewById(R.id.answer3)
        val answer4: CheckBox = findViewById(R.id.answer4)

        answeredQuestions[questionNumber].chosenAnswers.also {
            it.add(answer1.isChecked)
            it.add(answer2.isChecked)
            it.add(answer3.isChecked)
            it.add(answer4.isChecked)
        }

        if (answeredQuestions[questionNumber].answers.map { it.second } !=
            answeredQuestions[questionNumber].chosenAnswers) {
            wrongQuestionsCounter++
        }

        answer1.isChecked = false
        answer2.isChecked = false
        answer3.isChecked = false
        answer4.isChecked = false
    }
}