package com.catedium.catedium

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.catedium.catedium.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initial states: set all views to invisible
        binding.halfCircleBackground.alpha = 0f
        binding.textView1.alpha = 0f
        binding.textView2.alpha = 0f
        binding.centerButton.alpha = 0f

        startAnimations()

        // Set up button click listener to go to the next activity
        binding.centerButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startAnimations() {
        // TextView2 animation
        val circleBackground = ObjectAnimator.ofFloat(binding.halfCircleBackground, "alpha", 1f)
        circleBackground.interpolator = DecelerateInterpolator()
        circleBackground.duration = 1000

        // TextView2 animation
        val textView2Animator = ObjectAnimator.ofFloat(binding.textView2, "alpha", 1f)
        textView2Animator.interpolator = DecelerateInterpolator()
        textView2Animator.duration = 1000

        // TextView1 animation
        val textView1Animator = ObjectAnimator.ofFloat(binding.textView1, "alpha", 1f)
        textView1Animator.interpolator = DecelerateInterpolator()
        textView1Animator.startDelay = 500
        textView1Animator.duration = 1000

        // Button animation
        val buttonAnimator = ObjectAnimator.ofFloat(binding.centerButton, "alpha", 1f)
        buttonAnimator.interpolator = DecelerateInterpolator()
        buttonAnimator.startDelay = 1000
        buttonAnimator.duration = 1000

        // AnimatorSet to play animations sequentially
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            circleBackground,
            textView2Animator,
            textView1Animator,
            buttonAnimator
        )
        animatorSet.start()
    }
}