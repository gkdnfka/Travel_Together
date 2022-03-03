package com.example.capstone_design

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CommunityPostDetailPlaceAdaptor(private val items: ArrayList<PlaceInfo>, context : Context) : RecyclerView.Adapter<CommunityPostDetailPlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostDetailPlaceAdaptor.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_course_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CommunityPostDetailPlaceAdaptor.ViewHolder, position: Int) {
        holder.nametext.text = items[position].name
        holder.addresstext.text = items[position].address
        holder.numbertext.text = (position+1).toString()
        Toast.makeText(contexts, items[position].name + " 입니다!", Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_name)
        var addresstext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_address)
        var numbertext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_number)
    }
}

class CommunityPostDetailDayAdaptor(private val items: ArrayList<Int>, context : Context, changeDay : changeDay) : RecyclerView.Adapter<CommunityPostDetailDayAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    var changeDay = changeDay
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostDetailDayAdaptor.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_course_day_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CommunityPostDetailDayAdaptor.ViewHolder, position: Int) {
        holder.daytext.text = items[position].toString() + " 일차 일정"
        holder.daytext.setOnClickListener {
            changeDay.changeday(position)
        }
        holder.daytext.width = 30
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var daytext: TextView = view.findViewById<TextView>(R.id.post_detail_day_textview)
    }
}

interface changeDay{
    fun changeday(day : Int)
}