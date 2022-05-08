package com.example.capstone_design.Adapterset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.R


import android.content.Context;
import android.util.Log
import android.widget.ProgressBar

import com.example.capstone_design.Dataset.BringPostInfo;
import com.example.capstone_design.Interfaceset.SetSelectedBringPost;

import java.util.ArrayList;

class PostBringAdapter(val context :Context, private val BringPostList:ArrayList<BringPostInfo>, var implemented:SetSelectedBringPost):
    RecyclerView.Adapter<PostBringAdapter.ViewHolder>() {

    override fun getItemCount(): Int = BringPostList.size


    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val postname = view.findViewById<TextView>(R.id.bringPostName)
        val bringProgress = view.findViewById<ProgressBar>(R.id.bringProgressBar)
        val bringpercent = view.findViewById<TextView>(R.id.bringProgressBarPercent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_bring_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postname.text = BringPostList[position].postname
        holder.bringpercent.text = BringPostList[position].percent + "% " + "완료"
        holder.bringProgress.progress = BringPostList[position].percent.toInt()
        holder.itemView.setOnClickListener {
            implemented.SetSelectedBringPost(BringPostList[position])
        }
    }
}
