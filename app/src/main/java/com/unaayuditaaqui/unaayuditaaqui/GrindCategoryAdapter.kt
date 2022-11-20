package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GrindCategoryAdapter (private var categryList: List<String>) : RecyclerView.Adapter<GrindCategoryAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item,
            parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = categryList[position]

        holder.category.text = currentItem

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, SearchActivity::class.java).apply {
                putExtra("key", currentItem)
            }
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categryList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val category: TextView = itemView.findViewById(R.id.categoryTextView)
    }


}