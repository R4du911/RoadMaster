package com.example.roadmaster.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.roadmaster.R
import com.example.roadmaster.model.Question

class ReviewQuizActivity : AppCompatActivity() {

    private var questions : Array<Question> = emptyArray<Question>()
    private var index = 0

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_quiz)

        val answeredQuestions = intent.getParcelableArrayExtra("question_data", Question::class.java)

        if (answeredQuestions != null) {
            questions = answeredQuestions
        }
        else
        {
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", intent.getStringExtra("user"))
            startActivity(homeActivity)
        }

        reviewQuestion(index)
        index++

        findViewById<Button>(R.id.review_next_question).setOnClickListener{
            when (index) {
                questions.size - 1  -> {
                    reviewQuestion(index)
                    index++
                    findViewById<Button>(R.id.review_next_question).text = "Du-ma acasa"
                }
                questions.size -> {
                    val homeActivity = Intent(this@ReviewQuizActivity, HomeActivity::class.java)
                    homeActivity.putExtra("user", intent.getStringExtra("user"))
                    startActivity(homeActivity)
                }
                else -> {
                    reviewQuestion(index)
                    index++
                }
            }
        }

    }

    fun reviewQuestion(index : Int)
    {
        findViewById<TextView>(R.id.review_question_label).text = questions[index].text
        findViewById<TextView>(R.id.review_answer1).text = questions[index].answers[0].first
        findViewById<TextView>(R.id.review_answer2).text = questions[index].answers[1].first
        findViewById<TextView>(R.id.review_answer3).text = questions[index].answers[2].first
        findViewById<TextView>(R.id.review_answer4).text = questions[index].answers[3].first
    }
}