package com.example.capstone_design.Adapterset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.Util.TranslateTagName
import com.example.capstone_design.Util.addFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
    date  : 2022-04-05 ~
    worker: 김우람
    note  : recommend fragment 에서 사용될 Adaptor제작.

 */
class RecommendAdaptor(private val items: ArrayList<PlaceInfo>, context : Context) : RecyclerView.Adapter<RecommendAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.tourist_spot_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tag.text = TranslateTagName(items[position].name)
        holder.nametext.text = items[position].name
        holder.btn.setOnClickListener {
            addFavorite("FavoritePlaceList", items[position].num)
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
                holder.image.setImageBitmap(mbitmap)
            }
            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                mbitmap = BitmapFactory.decodeResource(contexts.getResources(), R.drawable.image);
                t.printStackTrace()
            }
        })
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var tag : TextView = view.findViewById<TextView>(R.id.tourist_spot_item_tag)
        var nametext: TextView = view.findViewById<TextView>(R.id.PlaceNameText)
        var image : ImageView = view.findViewById<ImageView>(R.id.PlaceImage)
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