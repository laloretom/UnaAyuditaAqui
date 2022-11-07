package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MyServiceAdapter(private val serviceList: ArrayList<Service>) : RecyclerView.Adapter<MyServiceAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_service_content,
        parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = serviceList[position]

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
                putExtra("key", currentItem.idService)
                //putExtra("key", "Si paso")

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

        holder.delete.setOnClickListener{v ->
            MaterialAlertDialogBuilder(v.context)
                .setTitle("¿Realmente quieres borrar?")
                .setPositiveButton("Sí, eliminar") { dialogInterface, i ->
                    val uidAuthor = currentItem.uidAuthor
                    val id = currentItem.idService
                    val dbService = FirebaseDatabase.getInstance().reference.child("Services")
                    val dbServiceAutor = FirebaseDatabase.getInstance().getReference("Users/$uidAuthor/Services")

                    v.context?.let {
                        val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("Services/img$id")

                        imageFirebaseStorage.delete().addOnCompleteListener {   resultado ->
                            if(resultado.isSuccessful){
                                dbService.child("$id").removeValue()
                                dbServiceAutor.child("$id").removeValue()
                            }else{
                                Toast.makeText(v.context, "No se pudo borrar la publicación", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()

        }

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val serviceTitle : TextView = itemView.findViewById(R.id.titleTextView)
        val category: TextView = itemView.findViewById(R.id.categoryTextView)
        val date: TextView = itemView.findViewById(R.id.dateTextView)
        val image: ImageView = itemView.findViewById(R.id.imageServiceCardView)
        val delete: ImageView = itemView.findViewById(R.id.deleteService)
    }




}