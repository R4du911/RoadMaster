package com.example.roadmaster.activities

import com.example.roadmaster.utils.CountryAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.roadmaster.R
import com.example.roadmaster.model.Country
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
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

class OtherCountriesInfoActivity : AppCompatActivity() {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_countries_info)

        val myIntent = intent
        val userData = myIntent.getStringExtra("user")

        lifecycleScope.launch {
            val countries: List<Country>? = countriesGet()

            val spinner: Spinner = findViewById(R.id.spinnerCountries)
            val adapter = CountryAdapter(this@OtherCountriesInfoActivity, countries)
            spinner.adapter = adapter

            val textViewInfo: TextView = findViewById(R.id.countryInfoText)

            //spinner for displaying countries
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedCountry = adapter.getItem(position)

                    //dynamically showing the info for the selected country
                    selectedCountry?.let { country ->
                        lifecycleScope.launch {
                            val infoList : List<String>? = countryInfoPost(country)
                            val stringBuilder = StringBuilder()

                            if (infoList != null) {
                                for (info in infoList) {
                                    stringBuilder.append("• ").append(info).append("\n").append("\n")
                                }
                            }

                            textViewInfo.text = stringBuilder.toString()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        //button for going back to choose info section
        val toChooseInfoActivityButton: ImageButton = findViewById(R.id.backToChooseInfoCategoryFromOtherCountries)
        toChooseInfoActivityButton.setOnClickListener{
            val chooseInfoActivity = Intent(this, ChooseInfoActivity::class.java)
            chooseInfoActivity.putExtra("user", userData.toString())

            startActivity(chooseInfoActivity)
        }
    }

    //request for retrieving the countries from DB
    private suspend fun countriesGet(): List<Country>? {
        try {
            val response: HttpResponse = httpClient.get("http://10.0.2.2:8000/api/countries") {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")
                val countriesJSON = userData.getString("countries")

                return Json.decodeFromString(countriesJSON)

            } else {
                println("Loading countries failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    //request for retrieving the info for a given country
    private suspend fun countryInfoPost(country: Country) : List<String>? {
        try {
            val response: HttpResponse = httpClient.post("http://10.0.2.2:8000/api/country-info") {
                contentType(ContentType.Application.Json)
                body = country
            }

            if (response.status.isSuccess()) {

                val json = JSONObject(response.readText())
                val userData = json.getJSONObject("data")
                val infoJSON = userData.getString("country_info")

                return Json.decodeFromString(infoJSON)

            } else {
                println("Loading country info failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}