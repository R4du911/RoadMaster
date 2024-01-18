package com.example.roadmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class DrivingTipsInfoActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driving_tips_info)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        val textViewInfo: TextView = findViewById(R.id.tipsInfoText)

        lifecycleScope.launch {
            val infoList : List<String>? = tipsGet()
            val stringBuilder = StringBuilder()

            if (infoList != null) {
                for (info in infoList) {
                    stringBuilder.append("• ").append(info).append("\n").append("\n")
                }
            }

            textViewInfo.text = stringBuilder.toString()
        }

        //button to go back to choose info menu
        val toChooseInfoActivityButton: ImageButton = findViewById(R.id.backToChooseInfoCategoryFromDrivingTips)
        toChooseInfoActivityButton.setOnClickListener{
            val chooseInfoActivity = Intent(this, ChooseInfoActivity::class.java)
            chooseInfoActivity.putExtra("user", userData.toString())

            startActivity(chooseInfoActivity)
        }
    }

    //request function for retrieving tips from DB
    private suspend fun tipsGet(): List<String>? {
        try {
            val response: HttpResponse = httpClient.get("http://10.0.2.2:8000/api/tips") {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")
                val tipsJSON = userData.getString("tips")

                return Json.decodeFromString(tipsJSON)

            } else {
                println("Loading tips failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}