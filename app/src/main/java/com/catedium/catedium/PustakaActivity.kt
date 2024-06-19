package com.catedium.catedium

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PustakaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pustaka)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CatediumApi::class.java)

        val animalClass = intent.getStringExtra("animal_class")

        api.getCats().enqueue(object : Callback<CatApiResponse> {
            override fun onResponse(call: Call<CatApiResponse>, response: Response<CatApiResponse>) {
                if (response.isSuccessful) {
                    val cats = response.body()?.record?.map {
                        val imageName = it.name.lowercase().replace(" ", "_")
                        val imageResId = resources.getIdentifier(imageName, "drawable", packageName)
                        Kucing(
                            imageResId,
                            it.name,
                            it.description,
                            it.scientific_name,
                            it.distribution,
                            it.habitat,
                            it.characteristics
                        )
                    } ?: emptyList()

                    if (animalClass != null) {
                        val matchedCat = cats.find { it.name.equals(animalClass, ignoreCase = true) }
                        matchedCat?.let {
                            val intent = Intent(this@PustakaActivity, DetailHewanActivity::class.java).apply {
                                putExtra("name", it.name)
                                putExtra("imageResId", it.imageResId)
                                putExtra("description", it.description)
                                putExtra("scientific_name", it.scientific_name)
                                putExtra("habitat", ArrayList(it.habitat))
                                putExtra("characteristics", ArrayList(it.characteristics))
                                putExtra("distribution", it.distribution)
                            }
                            startActivity(intent)
                        }
                    }

                    viewPager.adapter = CatediumAdapter(cats)
                } else {
                    Log.e("PustakaActivity", "Failed to get data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CatApiResponse>, t: Throwable) {
                Log.e("PustakaActivity", "Failed to get data", t)
            }
        })
    }
}
