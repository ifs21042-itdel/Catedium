package com.catedium.catedium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catedium.catedium.databinding.ActivityComingBinding

class ComingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}