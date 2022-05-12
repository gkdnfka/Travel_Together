package com.example.capstone_design.Adapterset

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.TagDictSet
import com.example.capstone_design.Dataset.TagLabelSet
import com.example.capstone_design.R

class TagSelectAdaptor(
    val context: Context,
    private val TagList: ArrayList<TagLabelSet>,
    private val SelectedIdx: ArrayList<Int> = ArrayList<Int>()
) : RecyclerView.Adapter<TagSelectAdaptor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
<<<<<<< HEAD

        if(SelectedIdx.size > 0 && SelectedIdx[position] == 1) holder.name.setBackgroundColor(Color.parseColor("#A0E7E5"))
        else if(SelectedIdx.size > 0) holder.name.setBackgroundColor(Color.parseColor("#D5E6C4"))

        /*
        if(SelectedIdx.size != 0) {
            for(i in SelectedIdx) {
                if(position == i) {
=======
        if (SelectedIdx.size != 0) {
            for (i in SelectedIdx) {
                if (position == i) {
>>>>>>> 505ea54ab03fb5aec335b48b9906172c043edf5e
                    holder.name.setBackgroundColor(Color.parseColor("#A0E7E5"))
                }
            }
        }*/


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

class TagSelectAdaptorForDict (
    val context: Context,
    private val TagList: ArrayList<TagDictSet>,
    private val SelectedIdx: ArrayList<Int> = ArrayList<Int>()
)
    : RecyclerView.Adapter<TagSelectAdaptorForDict.ViewHolder>() {
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

        if(SelectedIdx.size > 0 && SelectedIdx[position] == 1) holder.name.setBackgroundColor(Color.parseColor("#A0E7E5"))
        else if(SelectedIdx.size > 0) holder.name.setBackgroundColor(Color.parseColor("#D5E6C4"))

        /*
        if(SelectedIdx.size != 0) {
            for(i in SelectedIdx) {
                if(position == i) {
                    holder.name.setBackgroundColor(Color.parseColor("#A0E7E5"))
                }
            }
        }*/


        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, TagList[position].num.toInt())
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