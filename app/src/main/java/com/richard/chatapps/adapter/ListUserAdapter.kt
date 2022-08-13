package com.richard.chatapps.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.richard.chatapps.R
import com.richard.chatapps.dashboard.ChatActivity
import com.richard.chatapps.model.ListUserModel

class ListUserAdapter (var context: Context, var result: ArrayList<ListUserModel>) :
    RecyclerView.Adapter<ListUserAdapter.MyViewHolder>() {

    class MyViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_Nama)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater.from(context).inflate(R.layout.list_user, parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = result[position]

        holder.tvNama.text = result.nama

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("nama", result.nama)
            intent.putExtra("id", result.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = result.size
}