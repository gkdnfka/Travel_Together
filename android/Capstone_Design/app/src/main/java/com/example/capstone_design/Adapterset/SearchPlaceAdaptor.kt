package com.example.capstone_design.Adapterset

import android.content.Context
import android.content.res.ColorStateList
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
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.R
import com.example.capstone_design.Util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceAdaptor(private val items: ArrayList<PlaceInfo>, context : Context, val placeDetailPageInterface : PlaceDetailPageInterface) : RecyclerView.Adapter<SearchPlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.tourist_spot_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GetBookmarkImage("FavoritePlaceList", holder.btn, items[position].num)

        holder.tag.text = TranslateTagName(items[position].depart)
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

                val resources: Resources = (contexts).resources
                val drawable = BitmapDrawable(resources, mbitmap)
                drawable.setColorFilter(Color.parseColor("#FFA5A1A1"), PorterDuff.Mode.MULTIPLY)
                holder.Linear.setBackgroundDrawable(drawable)

            }
            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                mbitmap = BitmapFactory.decodeResource(contexts.getResources(), R.drawable.image);
                t.printStackTrace()
            }
        })

        holder.more.setOnClickListener{
            placeDetailPageInterface.change(12, items[position], mbitmap)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var tag : TextView = view.findViewById<TextView>(R.id.tourist_spot_item_tag)
        var nametext: TextView = view.findViewById<TextView>(R.id.PlaceNameText)
        var Linear : LinearLayout = view.findViewById<LinearLayout>(R.id.tourist_spot_item_Linear)
        var btn : ImageView = view.findViewById<ImageView>(R.id.search_tourist_spot_item_favorite)
        var more = view.findViewById<ImageView>(R.id.tourist_spot_item_more)
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
