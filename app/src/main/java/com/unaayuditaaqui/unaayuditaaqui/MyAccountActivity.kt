package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMyAccountBinding

class MyAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        updateUI ()

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        binding.updateProfileAppCompatButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()

            updateProfile(name)
        }
    }

    private  fun updateUI () {
        val user = auth.currentUser

        if (user != null){
            binding.emailTextView.text = user.email

            if(user.displayName != null){
                binding.nameTextView.text = user.displayName
                binding.nameEditText.setText(user.displayName)
            }

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.profileImageView)
        }
    }

    private  fun updateProfile (name : String) {

        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se realizaron los cambios correctamente.",
                        Toast.LENGTH_SHORT).show()
                    updateUI()
                }
            }
    }


}