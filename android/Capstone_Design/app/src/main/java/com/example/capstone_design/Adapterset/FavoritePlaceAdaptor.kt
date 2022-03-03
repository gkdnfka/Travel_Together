package com.example.capstone_design.Adapterset

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.R
import com.example.capstone_design.selectPlace


class FavoritePlaceAdaptor(private val items: ArrayList<PlaceInfo>, context : Context, selectplace : selectPlace) : RecyclerView.Adapter<FavoritePlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    var selectplace = selectplace

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.path_select_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Name.text = items[position].name
        holder.Addr.text = items[position].address
        holder.btn.setOnClickListener {
            var retvalue : Int = selectplace.selectplace(items[position])
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val Photo = view.findViewById<ImageView>(R.id.path_select_place_image)
        val Name = view.findViewById<TextView>(R.id.path_select_place_name)
        val Addr = view.findViewById<TextView>(R.id.path_select_place_address)
        val btn = view.findViewById<TextView>(R.id.path_select_place_check)
    }
}