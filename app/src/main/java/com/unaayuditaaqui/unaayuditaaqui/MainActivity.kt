package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        updateUI ()

        binding.navHome.setOnClickListener{
            Toast.makeText(this, "Home.",
                Toast.LENGTH_SHORT).show()
        }

        binding.navAccount.setOnClickListener{
            val intent = Intent(this, MyAccountActivity::class.java)
            this.startActivity(intent)
        }

        binding.signOutImageView.setOnClickListener {
            signOut()
        }



    }

    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        this.startActivity(intent)
    }

    private  fun updateUI () {
        val user = auth.currentUser

        if (user != null){
            binding.emailTextView.text = user.email

            if(user.displayName != null){
                binding.nameTextView.text = user.displayName
            }

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.profileImageView)
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.bgProfileImageView)
        }
    }

}