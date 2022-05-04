package com.example.capstone_design.Adapterset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.R


import android.content.Context;
import android.widget.ImageButton
import android.widget.Toast

import com.example.capstone_design.Dataset.BringPostInfo;
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.SetSelectedBringPost;
import java.lang.Math.round

import java.util.ArrayList;

class PostBringDetailAdapter(val context :Context, private val PlaceList:ArrayList<PlaceInfo>):
    RecyclerView.Adapter<PostBringDetailAdapter.ViewHolder>() {

    override fun getItemCount(): Int = PlaceList.size


    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placename = view.findViewById<TextView>(R.id.bring_drawer_placename)
        val placeaddr = view.findViewById<TextView>(R.id.bring_drawer_placeaddr)
        val remainingDistance = view.findViewById<TextView>(R.id.remainingDistance)
        val DestinationButton = view.findViewById<ImageButton>(R.id.drawerDestination)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view: View = LayoutInflater.from(context).inflate(R.layout.bring_drawer_layout_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placename.text = PlaceList[position].name
        holder.placeaddr.text = PlaceList[position].address
        var distance = round(PlaceList[position].distance.toDouble())
        holder.remainingDistance.text = distance.toString() + "m"
        holder.DestinationButton.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (!3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (!4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}
