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
import com.google.firebase.database.*
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
    private lateinit var dbRef: DatabaseReference
    private var serviceCategory: ArrayList<String> = arrayListOf<String>()

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

        getServiceData()

        binding.saveButton.setOnClickListener{
            val dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val dateInverted = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("-yyyyMMddHHmmss"))
            val uidAuthor = Firebase.auth.currentUser!!.uid
            val nameAuthor = Firebase.auth.currentUser!!.displayName
            val serviceTitle : String = binding.titleEditText.text.toString()
            val date : String = dateTime.toString()
            val category : String = binding.categoryAutoCompleteTextView.text.toString().lowercase()
            val description: String = binding.descriptionEditText.text.toString()
            val key: String = myRef.push().key.toString()
            val idService: String = key
            val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Services")
            val servicesReference : StorageReference = folder.child("img$key")
            val typeS : String = binding.autoCompleteTextView.text.toString()

            if (typeS != ""){
                if(fileUri==null){
                    Toast.makeText(this, "No se detectó una imagen", Toast.LENGTH_SHORT).show()
                } else {
                    servicesReference.putFile(fileUri!!).addOnSuccessListener {
                        servicesReference.downloadUrl.addOnSuccessListener { uri ->
                            val mService = Service(uidAuthor, nameAuthor, serviceTitle,typeS, description, category,date,dateInverted, uri.toString(), idService)
                            val postValues =mService.toMap()

                            val childUpdates = hashMapOf<String, Any>(
                                "/Services/$key" to postValues,
                                "/Users/$uidAuthor/Services/$key" to postValues
                            )
                            database.reference.updateChildren(childUpdates)
                        }
                    }
                    finish()
                }

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

    private fun getServiceData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Services")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceCategory.add(service!!.category.toString())
                    }

                    val distinct: List<String> = LinkedHashSet(serviceCategory).toMutableList()
                    val sorted_list = distinct.sortedWith(naturalOrder())

                    val adapterCategoryService = ArrayAdapter(this@AddServiceActivity,R.layout.list_item_type,sorted_list)
                    with(binding.categoryAutoCompleteTextView){
                        setAdapter(adapterCategoryService)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}