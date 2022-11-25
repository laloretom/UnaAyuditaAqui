package com.unaayuditaaqui.unaayuditaaqui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMyServicesBinding

class MyServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyServicesBinding
    private lateinit var serviceRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private val serviceMutableList:MutableList<Service> = ArrayList()
    private lateinit var messagesListener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Uid del usuario actual
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        serviceRecyclerView = findViewById(R.id.servicesList)
        serviceRecyclerView.layoutManager = LinearLayoutManager(this)
        serviceRecyclerView.setHasFixedSize(true)

        setupRecyclerView(binding.servicesList)

        binding.addImageView.setOnClickListener { v ->
            val intent = Intent(this, AddServiceActivity::class.java)
            v.context.startActivity(intent)
        }

    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val myRef = FirebaseDatabase.getInstance().getReference("Users/$uid/Services")
        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceMutableList.clear()
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceMutableList.add(service!!)
                    }
                    recyclerView.adapter = ServiceViewAdapter(serviceMutableList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }

    class ServiceViewAdapter(private val values: List<Service>) :
        RecyclerView.Adapter<ServiceViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.service_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val currentItem = values[position]
            holder.serviceTitle.text = currentItem.serviceTitle
            holder.category.text = currentItem.category
            holder.date.text = currentItem.date
            holder.image.let {
                Glide.with(holder.itemView.context)
                    .load(currentItem.url)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailServiceActivity::class.java).apply {
                    putExtra("key",currentItem.idService )
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditServiceActivity::class.java).apply {
                    putExtra("key", currentItem.idService)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val serviceTitle : TextView = itemView.findViewById(R.id.titleTextView)
            val category: TextView = itemView.findViewById(R.id.categoryTextView)
            val date: TextView = itemView.findViewById(R.id.dateTextView)
            val image: ImageView = itemView.findViewById(R.id.imageServiceCardView)
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val dbServiceAutor = FirebaseDatabase.getInstance().getReference("Users/$uid/Services")
        val dbService = FirebaseDatabase.getInstance().getReference("Services")
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("Services/img"+serviceMutableList[viewHolder.adapterPosition].idService)
                imageFirebaseStorage.delete()

                serviceMutableList[viewHolder.adapterPosition].idService?.let {
                    dbServiceAutor.child(it).setValue(null)
                    dbService.child(it).setValue(null)}
                serviceMutableList.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}