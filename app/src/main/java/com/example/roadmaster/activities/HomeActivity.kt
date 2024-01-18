package com.example.roadmaster.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.roadmaster.R
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private var greetingTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")
        val userDataJSON = userData?.let { JSONObject(it) }

        val username = userDataJSON?.getString("username")
        val greetingText = getString(R.string.greeting_text, username)
        greetingTextView = findViewById(R.id.helloUserText)
        greetingTextView?.text = greetingText


        //button to choose category section for starting quiz
        val toChooseCategoryButton: ImageButton = findViewById(R.id.startQuizButton)
        toChooseCategoryButton.setOnClickListener{
            val chooseCategoryActivity = Intent(this, ChooseCategoryActivity::class.java)
            chooseCategoryActivity.putExtra("user", userData.toString())

            startActivity(chooseCategoryActivity)
        }

        //button to choose info section
        val toChooseInfoButton: ImageButton = findViewById(R.id.infoButton)
        toChooseInfoButton.setOnClickListener{
            val chooseInfoActivity = Intent(this, ChooseInfoActivity::class.java)
            chooseInfoActivity.putExtra("user",userData.toString())

            startActivity(chooseInfoActivity)
        }

        //button to history section
        val toHistoryButton: ImageButton = findViewById(R.id.historyButton)
        toHistoryButton.setOnClickListener{
            val historyActivity = Intent(this, HistoryActivity::class.java)
            historyActivity.putExtra("user", userData.toString())

            startActivity(historyActivity)
        }

        //button to change password section
        val toChangePasswordButton: ImageButton = findViewById(R.id.changePasswordButton)
        toChangePasswordButton.setOnClickListener{
            val changePasswordActivity = Intent(this, ChangePasswordActivity::class.java)
            changePasswordActivity.putExtra("user", userData.toString())

            startActivity(changePasswordActivity)
        }

        //logout button
        val logoutButton: ImageButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}