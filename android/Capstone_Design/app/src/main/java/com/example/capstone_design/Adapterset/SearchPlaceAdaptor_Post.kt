package com.example.capstone_design.Adapterset

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.R
import com.example.capstone_design.Util.GetBookmarkImage
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.Util.TranslateTagName
import com.example.capstone_design.Util.addFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceAdaptor_Post(private val items: ArrayList<PlaceInfo>, val context : Context) : RecyclerView.Adapter<SearchPlaceAdaptor_Post.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tourist_spot_item_post, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.tag.text = TranslateTagName(items[position].name)
        holder.nametext.text = items[position].name
        holder.btn.setOnClickListener {
            addFavorite("FavoritePlaceList", items[position].num)
            GetBookmarkImage("FavoritePlaceList", holder.btn, items[position].num)
        }

        var service = PublicRetrofit.retrofit.create(LoadImage::class.java)
        var tmp = ImageInfoForLoad(items[position].num, "PlaceImages")
        lateinit var mbitmap : Bitmap
        service.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
            override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                Log.d("ImgLoadingObj", "이미지 출력 성공")
                var returndata = response.body()
                var byteArry = returndata?.data
                var tbitmap = byteArry?.let { it1 -> BitmapFactory.decodeByteArray( byteArry, 0, it1.size) }
                mbitmap = tbitmap!!

                val resources: Resources = (context).resources
                val drawable = BitmapDrawable(resources, mbitmap)
                drawable.setColorFilter(Color.parseColor("#FFA5A1A1"), PorterDuff.Mode.MULTIPLY)
                holder.Linear.setBackgroundDrawable(drawable)

            }
            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                mbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
                t.printStackTrace()
            }
        })
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tag: TextView = itemView.findViewById<TextView>(R.id.tourist_spot_item_post_tag)
        var nametext: TextView = itemView.findViewById<TextView>(R.id.tourist_spot_item_post_placename)
        var Linear = itemView.findViewById<LinearLayout>(R.id.tourist_spot_item_post_Linear)
        var btn = itemView.findViewById<ImageView>(R.id.tourist_spot_item_post_favorite)
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