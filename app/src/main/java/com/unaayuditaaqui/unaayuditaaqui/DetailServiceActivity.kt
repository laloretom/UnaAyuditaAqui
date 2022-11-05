package com.unaayuditaaqui.unaayuditaaqui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityDetailServiceBinding

class DetailServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailServiceBinding
    private var imageUrl: String? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val dbRef =
            database.getReference("Services").child(key.toString())

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mService: Service? = dataSnapshot.getValue(Service::class.java)
                if (mService != null) {

                    binding.titleTextView.text = mService.serviceTitle
                    binding.typeTextView.text = mService.type
                    binding.categoryTextView2.text = mService.category
                    binding.descriptionTextView.text = mService.description
                    imageUrl = mService.url.toString()

                    if(fileUri == null){
                        Glide.with(applicationContext)
                            .load(imageUrl)
                            .into(binding.photo)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

    }
}