package com.example.chatbot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRVAdapter(
    private val messageList: ArrayList<MessageModel>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER = 0
    private val BOT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = if (viewType == USER) {
            LayoutInflater.from(context).inflate(R.layout.user_msg, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.bot_msg, parent, false)
        }
        return if (viewType == USER) UserViewHolder(view) else BotViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position].message
        if (getItemViewType(position) == USER) {
            (holder as UserViewHolder).userTV.text = message
        } else {
            (holder as BotViewHolder).botTV.text = message
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == "user") USER else BOT
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userTV: TextView = view.findViewById(R.id.idTVUser)
    }

    class BotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val botTV: TextView = view.findViewById(R.id.idTVBot)
    }
}