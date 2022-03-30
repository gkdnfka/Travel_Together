package com.example.capstone_design.Adapterset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Fragmentset.CourseFragment
import com.example.capstone_design.Interfaceset.ChangeFragment
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.changeDay
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityPostDetailPlaceAdaptor(private val items: ArrayList<PlaceInfo>, context : Context, val ChangeListener : CourseFragment.InterfaceForCourseRecycler) : RecyclerView.Adapter<CommunityPostDetailPlaceAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostDetailPlaceAdaptor.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_course_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CommunityPostDetailPlaceAdaptor.ViewHolder, position: Int) {
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

        holder.itemView.setOnClickListener {
            ChangeListener.moveCamerato(items[position].PosY.toDouble(), items[position].PosX.toDouble(), position)
        }

        holder.moreinfo.setOnClickListener{
            ChangeListener.changeFragAndInitSelectedPlace(12, items[position], mbitmap)
        }

        holder.nametext.text = items[position].name
        holder.numbertext.text = (position+1).toString()
        Toast.makeText(contexts, items[position].name + " 입니다!", Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_name)
        var numbertext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_number)
        var image = view.findViewById<ImageView>(R.id.post_detail_course_place_image)
        var moreinfo = view.findViewById<ImageView>(R.id.post_detail_course_place_item_more_info)
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
        Log.d("아이템 변화 시작", position.toString() + " 아이템 채우는 중...")
        if(changeDay.getSelectedDay() != position) holder.backview.setBackgroundColor(Color.WHITE)
        else holder.backview.setBackgroundColor(Color.parseColor("#FF000000"))

        holder.daytext.setText(items[position].toString() + " 일차")
        holder.daytext.setOnClickListener {
            changeDay.changeday(position)
        }
        holder.daytext.width = 200
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var daytext: TextView = view.findViewById<TextView>(R.id.post_detail_day_textview)
        var backview = view.findViewById<LinearLayout>(R.id.post_detail_back)
    }
}

