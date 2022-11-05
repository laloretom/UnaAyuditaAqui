package com.unaayuditaaqui.unaayuditaaqui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityAddServiceBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddServiceBinding
    private val database = Firebase.database
    private val myRef = database.getReference("Services")
    private val file = 1
    private var fileUri: Uri? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeService = resources.getStringArray(R.array.type_service)
        val adapterTypeService = ArrayAdapter(this,R.layout.list_item_type,typeService)

        with(binding.autoCompleteTextView){
            setAdapter(adapterTypeService)
        }


        binding.saveButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val uidAuthor = Firebase.auth.currentUser!!.uid
            val serviceTitle : String = binding.titleEditText.text.toString()
            val date : String = dateTime.toString()
            val category : String = binding.categoryEditText.text.toString()
            val description: String = binding.descriptionEditText.text.toString()
            val key: String = myRef.push().key.toString()
            val idService: String = key
            val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Services")
            val servicesReference : StorageReference = folder.child("img$key")
            val typeS : String = binding.autoCompleteTextView.text.toString()

            if (typeS != ""){
                if(fileUri==null){
                    val mService = Service(uidAuthor, serviceTitle,typeS, description, category,date," ",idService)
                    val postValues =mService.toMap()

                    val childUpdates = hashMapOf<String, Any>(
                        "/Services/$key" to postValues,
                        "/Users/$uidAuthor/Services/$key" to postValues
                    )
                    database.reference.updateChildren(childUpdates)
                    //myRef.child(key).setValue(mService)
                } else {
                    servicesReference.putFile(fileUri!!).addOnSuccessListener {
                        servicesReference.downloadUrl.addOnSuccessListener { uri ->
                            val mService = Service(uidAuthor, serviceTitle,typeS, description, category,date, uri.toString(), idService)
                            val postValues =mService.toMap()

                            val childUpdates = hashMapOf<String, Any>(
                                "/Services/$key" to postValues,
                                "/Users/$uidAuthor/Services/$key" to postValues
                            )
                            database.reference.updateChildren(childUpdates)
                            //myRef.child(key).setValue(mService)
                        }
                    }
                }

                finish()

            }else{
                Toast.makeText(this, "Selecione el tipo", Toast.LENGTH_SHORT).show()
            }

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