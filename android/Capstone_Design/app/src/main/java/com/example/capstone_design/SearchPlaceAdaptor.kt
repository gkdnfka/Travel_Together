package com.example.capstone_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class SearchPlaceAdaptorR(private val items: ArrayList<PlaceInfo>, context : Context) : RecyclerView.Adapter<SearchPlaceAdaptorR.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceAdaptorR.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.tourist_spot_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: SearchPlaceAdaptorR.ViewHolder, position: Int) {
        holder.nametext.text = items[position].name
        holder.addresstext.text = items[position].address
        holder.btn.setOnClickListener {
            addFavorite("FavoritePlaceList", items[position].num)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.PlaceNameText)
        var addresstext: TextView = view.findViewById<TextView>(R.id.PlaceAddrText)
        var btn : ImageView = view.findViewById<ImageView>(R.id.search_tourist_spot_item_favorite)
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (!3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: Post_Plan_Adapter.OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (!4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : Post_Plan_Adapter.OnItemClickListener
}