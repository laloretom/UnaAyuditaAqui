package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val fileResult = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        database = FirebaseDatabase.getInstance()

        binding.saveButton.setOnClickListener {
            val username = binding.userNameEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()

            if (firstName.isNotEmpty()){
                database.reference.child("Users").child(auth.currentUser!!.uid).child("firstName").setValue(firstName)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se el nombre se actualizo correctamente.",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
            }
            if (lastName.isNotEmpty()){
                database.reference.child("Users").child(auth.currentUser!!.uid).child("lastName").setValue(lastName)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se el apellido se actualizo correctamente.",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
            }
            if (username.isNotEmpty()){
                updateProfile(username)
            }
        }

        binding.userImageView.setOnClickListener {
            fileManager()
        }

        /*
        binding.deleteAccountTextView.setOnClickListener {
            Toast.makeText(this,"Eliminar cuenta", Toast.LENGTH_SHORT).show()
        }

        binding.updatePasswordTextView.setOnClickListener {
            Toast.makeText(this,"Actualizar contraseña", Toast.LENGTH_SHORT).show()
        }

         */

        updateUI ()
    }

    private  fun updateProfile (username : String) {

        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = username
            database.reference.child("Users").child(auth.currentUser!!.uid).child("userName").setValue(username)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se realizaron los cambios correctamente.",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }

    private fun fileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, fileResult)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data

                uri?.let { imageUpload(it) }

            }
        }
    }

    private fun imageUpload(mUri: Uri) {

        val user = auth.currentUser
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Users")
        val fileName: StorageReference = folder.child("img"+user!!.uid)

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->

                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(uri.toString())
                }

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se realizaron los cambios correctamente.",
                                Toast.LENGTH_SHORT).show()
                            updateUI()
                        }
                    }
            }
        }.addOnFailureListener {
            Log.i("TAG", "file upload error")
        }
    }

    private  fun updateUI () {
        val user = auth.currentUser
        if (user != null){
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.userImageView)
        }

    }

}