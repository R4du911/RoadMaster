package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.roadmaster.R

class ChooseInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_info)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        val toHomeButton: ImageButton = findViewById(R.id.backToHomeFromInfoCategoryButton)
        toHomeButton.setOnClickListener{
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", userData.toString())

            startActivity(homeActivity)
        }

        val toOtherCountriesInfoButton: ImageButton = findViewById(R.id.otherCountriesButton)
        toOtherCountriesInfoButton.setOnClickListener{
            val otherCountriesInfoActivity = Intent(this, OtherCountriesInfoActivity::class.java)
            otherCountriesInfoActivity.putExtra("user", userData.toString())

            startActivity(otherCountriesInfoActivity)
        }

        val toDrivingTipsInfoButton: ImageButton = findViewById(R.id.practicButton)
        toDrivingTipsInfoButton.setOnClickListener{
            val drivingTipsInfoActivity = Intent(this, DrivingTipsInfoActivity::class.java)
            drivingTipsInfoActivity.putExtra("user", userData.toString())

            startActivity(drivingTipsInfoActivity)
        }
    }
}