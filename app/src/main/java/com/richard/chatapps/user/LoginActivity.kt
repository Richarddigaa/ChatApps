package com.richard.chatapps.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.richard.chatapps.dashboard.HomeActivity
import com.richard.chatapps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var fAut : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Login"

        fAut = FirebaseAuth.getInstance()

        binding.apply {
            EtEmailLogin.doOnTextChanged { text, start, before, count -> TILayoutEmailLogin.error = null }
            EtPasswordLogin.doOnTextChanged { text, start, before, count -> TILayoutPasswordLogin.error = null }

            BtnLogin.setOnClickListener {
                val email = EtEmailLogin.text.toString()
                val password = EtPasswordLogin.text.toString()

                when {
                    email.isEmpty() -> {
                        TILayoutEmailLogin.error = "Email tidak boleh kosong"
                        EtEmailLogin.requestFocus()
                    }
                    password.isEmpty() -> {
                        TILayoutPasswordLogin.error = "Password tidak boleh kosong"
                        EtPasswordLogin.requestFocus()
                    }
                    else -> {
                        login(email, password)
                    }
                }
            }

            BtnRegistLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun login(email: String, password: String) {
        fAut.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Email atau Password salah", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val result = fAut.currentUser

        if (result != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}