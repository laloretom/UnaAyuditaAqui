package com.unaayuditaaqui.unaayuditaaqui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceRecyclerView: RecyclerView
    private var serviceCategory:MutableList<String> = ArrayList()
    private lateinit var messagesListener: ValueEventListener

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

        serviceRecyclerView = view.findViewById(R.id.recyclerCategory)
        serviceRecyclerView.layoutManager = GridLayoutManager(context,4,GridLayoutManager.VERTICAL,false)
        serviceRecyclerView.setHasFixedSize(true)

        binding.searchRootView.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            this.startActivity(intent)
        }

        setupRecyclerView(binding.recyclerCategory)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val myRef = FirebaseDatabase.getInstance().getReference("Services")
        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceCategory.clear()
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceCategory.add(service!!.category.toString())
                    }
                    val distinct: List<String> = LinkedHashSet(serviceCategory).toMutableList()
                    val sorted_list = distinct.sortedWith(naturalOrder())

                    recyclerView.adapter = CategoryViewAdapter(sorted_list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

    }

    class CategoryViewAdapter(private val values: List<String>) :
        RecyclerView.Adapter<CategoryViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = values[position]

            holder.category.text = currentItem

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, SearchActivity::class.java).apply {
                    putExtra("key", currentItem)
                }
                v.context.startActivity(intent)
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val category: TextView = itemView.findViewById(R.id.categoryTextView)
        }
    }



}