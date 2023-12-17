package com.example.roadmaster.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.HistoryRequestDTO
import com.example.roadmaster.model.HistoryResponseDTO
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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class HistoryActivity : AppCompatActivity() {

    private var numberHistoriesView: TextView? = null
    private var previousChildRelativeLayoutId = R.id.numberQuizesText

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")
        val userDataJSON = userData?.let { JSONObject(it) }

        val id = userDataJSON?.getString("id")

        lifecycleScope.launch {
            historyPost(HistoryRequestDTO(id))
        }

        val linkBackToHomeArrow: ImageButton = findViewById(R.id.backToHomeButtonFromHistory)
        linkBackToHomeArrow.setOnClickListener{
            val homeActivity = Intent(this, HomeActivity::class.java)
            homeActivity.putExtra("user", userData.toString())

            startActivity(homeActivity)
        }
    }


    private suspend fun historyPost(user: HistoryRequestDTO){
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/history") {
                contentType(ContentType.Application.Json)
                body = user
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")
                val historiesString = userData.getString("histories")

                val historiesList = Json.decodeFromString<List<HistoryResponseDTO>>(historiesString)

                val numberOfHistories = historiesList.size
                val numberHistoriesText = getString(R.string.number_histories, numberOfHistories)
                var spannableString = SpannableString(numberHistoriesText)
                val colorSpan = ForegroundColorSpan(Color.parseColor("#F18C42"))
                spannableString.setSpan(colorSpan, numberHistoriesText.indexOf(numberOfHistories.toString()), numberHistoriesText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                numberHistoriesView = findViewById(R.id.numberQuizesText)
                numberHistoriesView?.text = spannableString

                val parentRelativeLayout = findViewById<RelativeLayout>(R.id.historiesContainer)
                for(index in 0 until numberOfHistories){
                    val childRelativeLayout = RelativeLayout(this)

                    val childLayoutParams = RelativeLayout.LayoutParams(
                        850,
                        320
                    )

                    childLayoutParams.addRule(RelativeLayout.BELOW, previousChildRelativeLayoutId)
                    childLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                    childLayoutParams.setMargins(16, 25, 16, 25)
                    childRelativeLayout.layoutParams = childLayoutParams
                    childRelativeLayout.setBackgroundColor(Color.parseColor("#D9D9D9"))
                    childRelativeLayout.id = View.generateViewId()

                    parentRelativeLayout.addView(childRelativeLayout)
                    previousChildRelativeLayoutId = childRelativeLayout.id


                    //category TextView
                    val categoryTextView = TextView(this)
                    val categoryText = getString(R.string.history_category, historiesList[index].category)
                    spannableString = SpannableString(categoryText)
                    spannableString.setSpan(colorSpan, categoryText.indexOf(" "), categoryText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    categoryTextView.text = spannableString
                    categoryTextView.textSize = 16F

                    val categoryLayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    categoryLayoutParams.leftMargin = 40
                    categoryLayoutParams.topMargin = 40
                    categoryTextView.layoutParams = categoryLayoutParams
                    categoryTextView.id = View.generateViewId()
                    childRelativeLayout.addView(categoryTextView)


                    //answers TextView
                    val answersTextView = TextView(this)
                    val answersText = getString(R.string.history_correct_answer_nr, historiesList[index].nr_correct)
                    spannableString = SpannableString(answersText)
                    spannableString.setSpan(colorSpan, answersText.indexOf(historiesList[index].nr_correct.toString()), answersText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    answersTextView.text = spannableString
                    answersTextView.textSize = 16F

                    val answersLayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    answersLayoutParams.leftMargin = 40
                    answersLayoutParams.addRule(RelativeLayout.BELOW, categoryTextView.id)
                    answersTextView.layoutParams = answersLayoutParams
                    answersTextView.id = View.generateViewId()
                    childRelativeLayout.addView(answersTextView)


                    //result TextView
                    val resultTextView = TextView(this)
                    val resultText = getString(R.string.history_result, historiesList[index].result_test)
                    spannableString = SpannableString(resultText)
                    spannableString.setSpan(colorSpan, resultText.indexOf(historiesList[index].result_test), resultText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    resultTextView.text = spannableString
                    resultTextView.textSize = 16F

                    val resultLayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    resultLayoutParams.leftMargin = 40
                    resultLayoutParams.addRule(RelativeLayout.BELOW, answersTextView.id)
                    resultTextView.layoutParams = resultLayoutParams
                    resultTextView.id = View.generateViewId()
                    childRelativeLayout.addView(resultTextView)


                    //date TextView
                    val dateTextView = TextView(this)
                    val dateTimeArray: List<String> = historiesList[index].date_test.toString().split(" ")
                    val dateText = getString(R.string.history_date, dateTimeArray[0])

                    spannableString = SpannableString(dateText)
                    spannableString.setSpan(colorSpan, dateText.indexOf(dateTimeArray[0]), dateText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    dateTextView.text = spannableString
                    dateTextView.textSize = 16F

                    val dateLayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    dateLayoutParams.leftMargin = 40
                    dateLayoutParams.bottomMargin = 40
                    dateLayoutParams.addRule(RelativeLayout.BELOW, resultTextView.id)
                    dateTextView.layoutParams = dateLayoutParams
                    dateTextView.id = View.generateViewId()
                    childRelativeLayout.addView(dateTextView)
                }


            } else {
                println("History retrieval failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}