package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.roadmaster.R

class ChooseCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_category)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        val toHomeButton: ImageButton = findViewById(R.id.backToHomeFromCategoryButton)
        toHomeButton.setOnClickListener{
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", userData.toString())

            startActivity(homeActivity)
        }

        val categoryA: ImageButton = findViewById(R.id.categoryAButton)
        categoryA.setOnClickListener{
            val quizActivity = Intent(this, QuizActivity::class.java)
            quizActivity.putExtra("user", userData.toString())
            quizActivity.putExtra("category", "A");
            startActivity(quizActivity)
        }

        val categoryB: ImageButton = findViewById(R.id.categoryBButton)
        categoryB.setOnClickListener{
            val quizActivity = Intent(this, QuizActivity::class.java)
            quizActivity.putExtra("user", userData.toString())
            quizActivity.putExtra("category", "B");
            startActivity(quizActivity)
        }

        val categoryC: ImageButton = findViewById(R.id.categoryCButton)
        categoryC.setOnClickListener{
            val quizActivity = Intent(this, QuizActivity::class.java)
            quizActivity.putExtra("user", userData.toString())
            quizActivity.putExtra("category", "C");
            startActivity(quizActivity)
        }

        val categoryD: ImageButton = findViewById(R.id.categoryDButton)
        categoryD.setOnClickListener{
            val quizActivity = Intent(this, QuizActivity::class.java)
            quizActivity.putExtra("user", userData.toString())
            quizActivity.putExtra("category", "D");
            startActivity(quizActivity)
        }
    }
}