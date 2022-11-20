package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbRef: DatabaseReference
    private var serviceCategory: ArrayList<String> = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeService = resources.getStringArray(R.array.type_service)
        val adapterTypeService = ArrayAdapter(view.context,R.layout.list_item_type,typeService)

        with(binding.autoCompleteTextView){
            setAdapter(adapterTypeService)
        }

        binding.searchRootView.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            this.startActivity(intent)
        }

        getServiceData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getServiceData(){
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

                    binding.recyclerCategory.layoutManager = GridLayoutManager(context,4)
                    binding.recyclerCategory.adapter = GrindCategoryAdapter(sorted_list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



}