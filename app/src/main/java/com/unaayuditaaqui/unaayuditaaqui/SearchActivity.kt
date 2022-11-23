package com.unaayuditaaqui.unaayuditaaqui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var serviceRecyclerView: RecyclerView
    private var serviceArrayList: ArrayList<Service> = arrayListOf<Service>()
    private lateinit var adapter: ServiceAdapter
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getStringExtra("key")
        val mType = intent.getStringExtra("mType")

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceRecyclerView = findViewById(R.id.servicesList)
        serviceRecyclerView.layoutManager = LinearLayoutManager(this)
        serviceRecyclerView.setHasFixedSize(true)

        val typeService = resources.getStringArray(R.array.type_service)
        val adapterTypeService = ArrayAdapter(this,R.layout.list_item_type,typeService)

        with(binding.autoCompleteTextView){
            setAdapter(adapterTypeService)
        }

        binding.searchEditText.addTextChangedListener { titleFilter ->
           val servicesFiltered =
               serviceArrayList.filter { service ->
                   service.serviceTitle!!.lowercase().contains(titleFilter.toString().lowercase())}
            adapter.updateService(servicesFiltered as ArrayList<Service>)
        }


        binding.autoCompleteTextView.setOnDismissListener {
            val mType : String = binding.autoCompleteTextView.text.toString()
            val servicesFiltered =
                serviceArrayList.filter { service ->
                    service.type!!.contains(mType)}
            adapter.updateService(servicesFiltered as ArrayList<Service>)

        }

        if (category == null){
            getServiceData()
        } else {
            getServiceDataXcategory(category)
        }

    }

    private fun getServiceData(){
        dbRef = FirebaseDatabase.getInstance().getReference("Services")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceArrayList.add(service!!)
                    }
                    adapter = ServiceAdapter(serviceArrayList)
                    serviceRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getServiceDataXcategory(categoryFilter: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Services")
        dbRef.addValueEventListener(object : ValueEventListener {

            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        if (service!!.category.toString() == categoryFilter)
                        serviceArrayList.add(service!!)
                    }
                    adapter = ServiceAdapter(serviceArrayList)
                    serviceRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}