package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signOutImageView.setOnClickListener {
            signOut()
        }

    }

    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        this.startActivity(intent)
    }
}