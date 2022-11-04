package com.unaayuditaaqui.unaayuditaaqui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityEditServiceBinding

class EditServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditServiceBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private val file = 1
    private var fileUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val dbRef =
            database.getReference("Services").child(key.toString())

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mService: Service? = dataSnapshot.getValue(Service::class.java)
                if (mService != null) {

                    binding.titleEditText.text = Editable.Factory.getInstance().newEditable(mService.serviceTitle)
                    binding.dateEditText.text = Editable.Factory.getInstance().newEditable(mService.date)
                    binding.categoryEditText.text = Editable.Factory.getInstance().newEditable(mService.category)
                    binding.descriptionEditText.text = Editable.Factory.getInstance().newEditable(mService.description)

                    imageUrl = mService.url.toString()

                    if(fileUri == null){
                        Glide.with(applicationContext)
                            .load(imageUrl)
                            .into(binding.posterImageView)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        binding.saveButton.setOnClickListener {
            val uidAuthor = Firebase.auth.currentUser!!.uid
            val serviceTitle : String = binding.titleEditText.text.toString()
            val date : String = binding.dateEditText.text.toString()
            val category : String = binding.categoryEditText.text.toString()
            val description: String = binding.descriptionEditText.text.toString()

            val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Services")
            val serviceReference : StorageReference = folder.child("img$key")

            if(fileUri == null){
                val mService = Service(uidAuthor, serviceTitle, description, category, date, imageUrl)
                val postValues =mService.toMap()
                val childUpdates = hashMapOf<String, Any>(
                    "/Services/$key" to postValues,
                    "/Users/$uidAuthor/Services/$key" to postValues
                )
                database.reference.updateChildren(childUpdates)
                //dbRef.setValue(mService)
            } else {
                serviceReference.putFile(fileUri!!).addOnSuccessListener {
                    serviceReference.downloadUrl.addOnSuccessListener { uri ->
                        val mService = Service(uidAuthor, serviceTitle, description, category, date, uri.toString())
                        val postValues =mService.toMap()
                        val childUpdates = hashMapOf<String, Any>(
                            "/Services/$key" to postValues,
                            "/Users/$uidAuthor/Services/$key" to postValues
                        )
                        database.reference.updateChildren(childUpdates)
                        //dbRef.setValue(mService)
                    }
                }
            }

            finish()
        }

        binding.posterImageView.setOnClickListener {
            fileUpload()
        }
    }

    private fun fileUpload() {
        previewImage.launch("image/*")
    }

    private val previewImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        fileUri = uri
        binding.posterImageView.setImageURI(uri)
    }
}