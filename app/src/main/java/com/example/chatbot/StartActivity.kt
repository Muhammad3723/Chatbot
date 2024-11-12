package com.example.chatbot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    // Declare UI components
    private lateinit var usernameInput: EditText
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Initialize UI components
        usernameInput = findViewById(R.id.usernameInput)
        continueButton = findViewById(R.id.continueButton)

        // Set listener for continue button
        continueButton.setOnClickListener {
            val username = usernameInput.text.toString().trim() // Get the username input

            // Check if the username is empty
            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed to MainActivity and pass the username
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USERNAME", username)
                startActivity(intent)
                finish() // Finish StartActivity to prevent going back to it
            }
        }
    }
}
