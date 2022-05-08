package com.example.capstone_design.Adapterset

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.TagLabelSet
import com.example.capstone_design.R

class TagSelectAdaptor (
    val context: Context,
    private val TagList: ArrayList<TagLabelSet>)
    : RecyclerView.Adapter<TagSelectAdaptor.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tag_item_text)
    }

    override fun getItemCount(): Int {
        return TagList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = "#" + TagList[position].name

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener
}