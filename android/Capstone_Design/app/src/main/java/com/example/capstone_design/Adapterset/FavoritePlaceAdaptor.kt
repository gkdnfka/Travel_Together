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
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.R
import com.example.capstone_design.Util.GetBookmarkImage
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.Util.TranslateTagName
import com.example.capstone_design.Util.addFavorite
import com.example.capstone_design.selectPlace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoritePlaceAdaptor(private val items: ArrayList<PlaceInfo>, context : Context, selectplace : selectPlace, val placeDetailPageInterface : PlaceDetailPageInterface) : RecyclerView.Adapter<FavoritePlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    var selectplace = selectplace

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.path_select_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GetBookmarkImage("FavoritePlaceList", holder.bookmark, items[position].num)
        holder.bookmark.setOnClickListener {
            addFavorite("FavoritePlaceList", items[position].num)
            GetBookmarkImage("FavoritePlaceList", holder.bookmark, items[position].num)
        }

        holder.Name.text = items[position].name
        holder.chkbox.setOnClickListener {
            var retvalue : Int = selectplace.selectplace(items[position])
        }
        holder.tag.setText(TranslateTagName(items[position].depart))
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
                holder.LinearLayout.setBackgroundDrawable(drawable)
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
        val Name = view.findViewById<TextView>(R.id.path_select_place_name)
        val LinearLayout = view.findViewById<LinearLayout>(R.id.path_select_linear)
        val tag = view.findViewById<TextView>(R.id.path_select_place_tag)
        val chkbox = view.findViewById<CheckBox>(R.id.path_select_place_chkbox)
        val more = view.findViewById<ImageView>(R.id.path_select_place_more)
        val bookmark = view.findViewById<ImageView>(R.id.path_select_place_bookmark)
    }
}