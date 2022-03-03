package com.example.capstone_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchPlaceAdaptor_Post(private val items: ArrayList<PlaceInfo>, context : Context) : RecyclerView.Adapter<SearchPlaceAdaptor_Post.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceAdaptor_Post.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.tourist_spot_item_post, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: SearchPlaceAdaptor_Post.ViewHolder, position: Int) {
        holder.nametext.text = items[position].name
        holder.addresstext.text = items[position].address
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.PlaceNameText)
        var addresstext: TextView = view.findViewById<TextView>(R.id.PlaceAddrText)
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (!3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: SearchPlaceAdaptor_Post.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (!4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : SearchPlaceAdaptor_Post.OnItemClickListener
}