package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.roadmaster.R
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")
        val userDataJSON = userData?.let { JSONObject(it) }

        val user_text_view: TextView = findViewById(R.id.user_name)

        user_text_view.text = userDataJSON?.getString("username")

        val time_text: TextView = findViewById(R.id.timer)
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                time_text.text = millisUntilFinished.toString();
            }

            override fun onFinish() {
                time_text.text = "Findish"
            }
        }
        timer.start()
    }
}