package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMyServicesBinding

class MyServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyServicesBinding
    private lateinit var serviceRecyclerView: RecyclerView
    private lateinit var serviceArrayList: ArrayList<Service>
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private var serviceSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fullScreen()

        // Uid del usuario actual
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        serviceRecyclerView = findViewById(R.id.servicesList)
        serviceRecyclerView.layoutManager = LinearLayoutManager(this)
        serviceRecyclerView.setHasFixedSize(true)

        serviceArrayList = arrayListOf<Service>()
        getServiceData()

        if (serviceSize < 4){
            binding.addImageView.visibility = View.VISIBLE
        }

        binding.addImageView.setOnClickListener { v ->
            val intent = Intent(this, AddServiceActivity::class.java)
            v.context.startActivity(intent)
        }

    }

    private fun getServiceData(){
        dbRef = FirebaseDatabase.getInstance().getReference("Users/$uid/Services")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceArrayList.add(service!!)
                        serviceSize++
                    }
                    serviceRecyclerView.adapter = MyServiceAdapter(serviceArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            }
        }
    }

}