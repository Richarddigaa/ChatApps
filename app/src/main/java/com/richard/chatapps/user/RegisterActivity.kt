package com.richard.chatapps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.richard.chatapps.databinding.ActivityRegisterBinding
import com.richard.chatapps.model.UserModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var fAut : FirebaseAuth
    private lateinit var dRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Register"

        fAut = FirebaseAuth.getInstance()
        dRef = FirebaseDatabase.getInstance().reference

        binding.apply {
            EtNamaRegist.doOnTextChanged { text, start, before, count -> TILayoutNamaRegist.error = null }
            EtEmailRegist.doOnTextChanged { text, start, before, count -> TILayoutEmailRegist.error = null }
            EtPasswordRegist.doOnTextChanged { text, start, before, count -> TILayoutPasswordRegist.error = null }

            BtnDaftar.setOnClickListener {
                val nama = EtNamaRegist.text.toString()
                val email = EtEmailRegist.text.toString()
                val password = EtPasswordRegist.text.toString()

                when {
                    nama.isEmpty() -> {
                        TILayoutNamaRegist.error = "Nama tidak boleh kosong"
                        EtNamaRegist.requestFocus()
                    }
                    email.isEmpty() -> {
                        TILayoutEmailRegist.error = "Email tidak boleh kosong"
                        EtEmailRegist.requestFocus()
                    }
                    password.isEmpty() -> {
                        TILayoutPasswordRegist.error = "Password tidak boleh kosong"
                        EtPasswordRegist.requestFocus()
                    }
                    else -> {
                        register(email, password)
                    }
                }
            }

            BtnLoginRegist.setOnClickListener {
                Intent(this@RegisterActivity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
    }

    private fun dataUser(){
        binding.apply {
            val nama = EtNamaRegist.text.toString()
            val email = EtEmailRegist.text.toString()
            val password = EtPasswordRegist.text.toString()
            val uid =fAut.uid.toString()

            dRef.child("data_user").child(uid).setValue(UserModel(email, nama, password, uid))
        }

        if (true){
            Firebase.auth.signOut()
        }
    }

    private fun register(email : String, password : String){
        fAut.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    dataUser()
                    binding.EtNamaRegist.text!!.clear()
                    binding.EtEmailRegist.text!!.clear()
                    binding.EtPasswordRegist.text!!.clear()
                    Toast.makeText(this, "Daftar Berhasil", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(this, "Daftar Gagal", Toast.LENGTH_LONG).show()
                }
            }
    }
}