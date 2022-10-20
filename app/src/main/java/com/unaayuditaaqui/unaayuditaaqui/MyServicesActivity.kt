package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMyServicesBinding

class MyServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyServicesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }
}