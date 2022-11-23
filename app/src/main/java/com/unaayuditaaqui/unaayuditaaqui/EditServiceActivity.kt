package com.unaayuditaaqui.unaayuditaaqui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityEditServiceBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditServiceBinding
    private val file = 1
    private var fileUri: Uri? = null
    private var imageUrl: String? = null
    private lateinit var dbRef: DatabaseReference
    private var serviceCategory: ArrayList<String> = arrayListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val dbRef =
            database.getReference("Services").child(key.toString())

        val typeService = resources.getStringArray(R.array.type_service)
        val adapterTypeService = ArrayAdapter(this,R.layout.list_item_type,typeService)

        with(binding.autoCompleteTextView){
            setAdapter(adapterTypeService)
        }

        getServiceData()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mService: Service? = dataSnapshot.getValue(Service::class.java)
                if (mService != null) {

                    binding.titleEditText.text = Editable.Factory.getInstance().newEditable(mService.serviceTitle)
                    binding.categoryAutoCompleteTextView.text = Editable.Factory.getInstance().newEditable(mService.category)
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
            val date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val uidAuthor = Firebase.auth.currentUser!!.uid
            val nameAuthor = Firebase.auth.currentUser!!.displayName
            val serviceTitle : String = binding.titleEditText.text.toString()
            val category : String = binding.categoryAutoCompleteTextView.text.toString().lowercase()
            val description: String = binding.descriptionEditText.text.toString()
            val typeS : String = binding.autoCompleteTextView.text.toString()
            val idService: String? = key

            if (typeS != ""){
                val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Services")
                val serviceReference : StorageReference = folder.child("img$key")

                if(fileUri == null){
                    val mService = Service(uidAuthor, nameAuthor, serviceTitle, typeS, description, category, date, imageUrl, idService)
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
                            val mService = Service(uidAuthor, nameAuthor, serviceTitle, typeS, description, category, date, uri.toString(), idService)
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

                    val adapterCategoryService = ArrayAdapter(this@EditServiceActivity,R.layout.list_item_type,sorted_list)
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