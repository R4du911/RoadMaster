package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.roadmaster.R

class OtherCountriesInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_countries_info)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        val toChooseInfoActivityButton: ImageButton = findViewById(R.id.backToChooseInfoCategoryFromOtherCountries)
        toChooseInfoActivityButton.setOnClickListener{
            val chooseInfoActivity = Intent(this, ChooseInfoActivity::class.java)
            chooseInfoActivity.putExtra("user", userData.toString())

            startActivity(chooseInfoActivity)
        }
    }
}