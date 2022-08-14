package com.richard.chatapps.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.richard.chatapps.adapter.ChatAdapter
import com.richard.chatapps.databinding.ActivityChatBinding
import com.richard.chatapps.model.ChatModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var dRef: DatabaseReference
    var senderRoom: String? = null
    var reciverRoom: String? = null

    private lateinit var userArrayChat: ArrayList<ChatModel>
    private lateinit var myAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // untuk mendapatkan nama dan uid dari lawan bicara yang dikirim dari adapter
        val nama = intent.getStringExtra("nama")
        val receverUid = intent.getStringExtra("id")

        supportActionBar!!.title = nama

        // Untuk mendapatkan User Uid dari user yang menggunakan
        val sendId = FirebaseAuth.getInstance().currentUser?.uid

        dRef = FirebaseDatabase.getInstance().reference
        userArrayChat = arrayListOf()

        myAdapter = ChatAdapter(this, userArrayChat)

        binding.rvListChat.adapter = myAdapter
        binding.rvListChat.layoutManager = LinearLayoutManager(this)
        binding.rvListChat.setHasFixedSize(true)

        senderRoom = receverUid + sendId
        reciverRoom = sendId + receverUid

        dRef.child("user_chat").child(senderRoom!!).child("chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userArrayChat.clear()
                        for (lisUserSnapsot in snapshot.children) {
                            val listUser = lisUserSnapsot.getValue(ChatModel::class.java)
                            userArrayChat.add(listUser!!)
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding.apply {
            fabKirimChat.setOnClickListener {
                val chat = etTextChat.text.toString()
                val messageObject = ChatModel(chat, sendId)

                dRef.child("user_chat").child(senderRoom!!).child("chat").push()
                    .setValue(messageObject).addOnCompleteListener {
                        dRef.child("user_chat").child(reciverRoom!!).child("chat").push()
                            .setValue(messageObject)
                    }

                etTextChat.text.clear()
            }
        }
    }
}