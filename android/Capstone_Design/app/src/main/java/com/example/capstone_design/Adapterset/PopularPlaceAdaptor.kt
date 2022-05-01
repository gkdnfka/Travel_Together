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
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.selectPlace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularPlaceAdaptor(private val items: ArrayList<PlaceInfo>, val context : Context, selectplace : selectPlace,  val placeDetailPageInterface : PlaceDetailPageInterface) : RecyclerView.Adapter<PopularPlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(context).inflate(R.layout.popular_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textview.text = items[position].name
        holder.itemView.setOnClickListener {
            placeDetailPageInterface.change(12, items[position], holder.textview.background.toBitmap())
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
                holder.textview.setBackgroundDrawable(drawable)
            }
            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                mbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
                t.printStackTrace()
            }
        })
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var textview: TextView = view.findViewById<TextView>(R.id.popular_place_text)
    }
}