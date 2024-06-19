package com.catedium.catedium

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.catedium.catedium.databinding.ActivityDetailHewanBinding
import com.catedium.catedium.databinding.ActivityHomeScreenBinding

class DetailHewanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHewanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHewanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageViewHewan: ImageView = findViewById(R.id.imageViewHewan)
        val textViewJudulHewan: TextView = findViewById(R.id.textViewJudulHewan)
        val textViewDeskripsiHewan: TextView = findViewById(R.id.textViewDeskripsiHewan)
        val textViewScientificName: TextView = findViewById(R.id.textViewScientificName)
        val textViewDistribution: TextView = findViewById(R.id.textViewDistribution)
        val textViewHabitat: TextView = findViewById(R.id.textViewHabitat)
        val textViewCharacteristics: TextView = findViewById(R.id.textViewCharacteristics)

        // Ambil data dari Intent
        val name = intent.getStringExtra("name")
        val imageResId = intent.getIntExtra("imageResId", 0)
        val description = intent.getStringExtra("description")
        val scientific_name = intent.getStringExtra("scientific_name")
        val distribution = intent.getStringExtra("distribution")
        val habitat = intent.getStringArrayListExtra("habitat")
        val characteristics = intent.getStringArrayListExtra("characteristics")

        // Update UI with the details
        textViewJudulHewan.text = name
        textViewDeskripsiHewan.text = description
        imageViewHewan.setImageResource(imageResId)
        textViewScientificName.text = scientific_name
        textViewDistribution.text = distribution

        // Display habitat and characteristics if they are not null
        textViewHabitat.text = habitat?.joinToString(", ") ?: "Data not available"
        textViewCharacteristics.text = characteristics?.joinToString(", ") ?: "Data not available"

        binding.buttonIdentifikasiKembali.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }
    }

}
