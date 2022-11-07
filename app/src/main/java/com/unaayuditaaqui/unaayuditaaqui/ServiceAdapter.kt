package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ServiceAdapter(private val serviceList: ArrayList<Service>) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.service_content,
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
                //putExtra("key", "currentItem.url")

            }
            v.context.startActivity(intent)
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
    }



}