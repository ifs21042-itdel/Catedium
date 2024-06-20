package com.catedium.catedium

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catedium.catedium.databinding.ActivityContactAvtivityBinding

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactAvtivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactAvtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val comment = binding.editTextComment.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || comment.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                sendEmail(firstName, lastName, email, comment)
            }
        }
    }

    private fun sendEmail(firstName: String, lastName: String, email: String, comment: String) {
        val subject = "Contact Form Submission"
        val message = "First Name: $firstName\nLast Name: $lastName\nEmail: $email\nComment: $comment"
        val recipient = "a114d4kx3943@bangkit.academy"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "Send email using..."))
        } catch (e: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }
}
