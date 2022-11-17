package com.unaayuditaaqui.unaayuditaaqui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
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
    private var userUid: String? = null
    private var mService: Service? = null
    private val database = Firebase.database

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
                mService = dataSnapshot.getValue(Service::class.java)
                if (mService != null) {

                    binding.titleTextView.text = mService!!.serviceTitle
                    binding.typeTextView.text = mService!!.type
                    binding.categoryTextView2.text = mService!!.category
                    binding.descriptionTextView.text = mService!!.description
                    binding.authorTextView.text = mService!!.nameAuthor
                    imageUrl = mService!!.url.toString()

                    userUid = mService!!.uidAuthor

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

        binding.applyButton.setOnClickListener{
            //Toast.makeText(this, "Si paso: $userUid", Toast.LENGTH_SHORT).show()
            RequestDialog(
                onSubmitClickListener = { message ->
                    val toUid: String? = mService!!.uidAuthor
                    val toName: String? = mService!!.nameAuthor
                    val fromUid: String? = Firebase.auth.currentUser!!.uid
                    val fromName: String? = Firebase.auth.currentUser!!.displayName
                    val serviceTitle: String? = mService!!.serviceTitle
                    val idService: String? = mService!!.idService
                    val myRef = database.getReference("Users/$toUid/Request/Received")
                    val idRequest: String? = myRef.push().key.toString()

                    val mRequest = Request(toUid,toName,fromUid,fromName,serviceTitle,idService,message,"In process",idRequest)
                    val postValues =mRequest.toMap()
                    val childUpdates = hashMapOf<String, Any>(
                        "/Users/$fromUid/Request/Sent/$idRequest" to postValues, //De
                        "Users/$toUid/Request/Received/$idRequest" to postValues //Para
                    )
                    database.reference.updateChildren(childUpdates)
                    Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show()
                }
            ).show(supportFragmentManager, "dialog")

        }

    }
}