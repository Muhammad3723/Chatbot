package com.example.chatbot

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.chatbot.R.layout.activity_main
import org.json.JSONException
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var chatsRV: RecyclerView
    private lateinit var sendMsgButton: ImageButton
    private lateinit var userMsgInput: EditText
    private lateinit var messageList: ArrayList<MessageModel>
    private lateinit var messageAdapter: MessageRVAdapter
    private lateinit var requestQueue: RequestQueue
    private val USER_KEY = "user"
    private val BOT_KEY = "bot"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        // Retrieve the username from intent and show a welcome message
        val username = intent.getStringExtra("USERNAME") ?: "User"
        Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()

        initializeUI()
        setupRecyclerView(username) // Pass username to display a greeting message

        requestQueue = Volley.newRequestQueue(this)
        requestQueue.cache.clear()

        sendMsgButton.setOnClickListener {
            handleSendMessage()
        }
    }
    private fun initializeUI() {
        chatsRV = findViewById(R.id.idRVChats)
        sendMsgButton = findViewById(R.id.idIBSend)
        userMsgInput = findViewById(R.id.idEditMessage)
    }

    private fun setupRecyclerView(username: String) {
        messageList = ArrayList()
        messageAdapter = MessageRVAdapter(messageList, this)
        chatsRV.layoutManager = LinearLayoutManager(this)
        chatsRV.adapter = messageAdapter

        // Start Message from bot
        val greetingMessage = "Hello, $username! How can I assist you today?"
        messageList.add(MessageModel(greetingMessage, BOT_KEY))
        messageAdapter.notifyDataSetChanged()
    }
    private fun handleSendMessage() {
        val userMessage = userMsgInput.text.toString().trim()
        if (userMessage.isBlank()) {
            Toast.makeText(this, "Please enter a valid message.", Toast.LENGTH_SHORT).show()
        } else if (userMessage.length > 200) {
            Toast.makeText(this, "Message too long. Try to keep it under 200 characters.", Toast.LENGTH_SHORT).show()
        } else {
            sendMessageToBot(userMessage)
            userMsgInput.setText("")
        }
    }
    private fun sendMessageToBot(userMessage: String) {
        messageList.add(MessageModel(userMessage, USER_KEY))
        messageAdapter.notifyDataSetChanged()
        chatsRV.smoothScrollToPosition(messageList.size - 1)

        val encodedMessage = URLEncoder.encode(userMessage, "UTF-8")
        val botApiUrl = "http://api.brainshop.ai/get?bid=172514&key=n5LgcpsyAcEZ9SIG&uid=uid&msg=$encodedMessage"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, botApiUrl, null,
            { response ->
                try {
                    val botResponse = response.getString("cnt")
                    messageList.add(MessageModel(botResponse, BOT_KEY))
                    messageAdapter.notifyDataSetChanged()
                    chatsRV.smoothScrollToPosition(messageList.size - 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    showErrorMessage("No response")
                }
            },
            { error ->
                error.printStackTrace()
                showErrorMessage("Sorry no response. Try again later.")
                Toast.makeText(this, "No response from the bot.", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun showErrorMessage(message: String) {
        messageList.add(MessageModel(message, BOT_KEY))
        messageAdapter.notifyDataSetChanged()
    }
}