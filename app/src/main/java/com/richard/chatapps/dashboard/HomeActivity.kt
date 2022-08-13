package com.richard.chatapps.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.richard.chatapps.adapter.ListUserAdapter
import com.richard.chatapps.databinding.ActivityHomeBinding
import com.richard.chatapps.model.ListUserModel
import com.richard.chatapps.user.LoginActivity
import com.richard.chatapps.R

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var dRef : DatabaseReference
    private lateinit var userArrayList: ArrayList<ListUserModel>
    private lateinit var myAdapter : ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dRef = FirebaseDatabase.getInstance().reference
        userArrayList = ArrayList()

        myAdapter = ListUserAdapter(this, userArrayList)

        binding.apply {
            rvListUser.adapter = myAdapter
            rvListUser.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvListUser.setHasFixedSize(true)
        }

        getListUser()
    }

    private fun getListUser() {
        dRef = FirebaseDatabase.getInstance().getReference("data_user")

        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    userArrayList.clear()
                    for (lisUserSnapsot in snapshot.children){
                        val listUser = lisUserSnapsot.getValue(ListUserModel::class.java)
                        if (Firebase.auth.uid != listUser!!.uid){
                            userArrayList.add(listUser)
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuLogout){
            if (true){
                Firebase.auth.signOut()

                Intent(this, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            startActivity(Intent(this, LoginActivity::class.java))
        }
        return true
    }
}