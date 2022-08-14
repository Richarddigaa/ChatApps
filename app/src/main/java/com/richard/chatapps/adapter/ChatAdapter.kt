package com.richard.chatapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.richard.chatapps.R
import com.richard.chatapps.model.ChatModel

class ChatAdapter(val context: Context?, val userListChat: ArrayList<ChatModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view : View = LayoutInflater.from(context).inflate(R.layout.chat_kiri, parent, false)
            return ReceiveViewHolder(view)
        } else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.chat_kanan, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatList = userListChat[position]
        if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = chatList.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = chatList.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chatList = userListChat[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(chatList.sendId)){
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return userListChat.size
    }

    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.chat_kanan)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.chat_kiri)
    }
}