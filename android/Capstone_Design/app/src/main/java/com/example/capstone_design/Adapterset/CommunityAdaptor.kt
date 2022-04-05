package com.example.capstone_design.Adapterset
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.google.android.gms.maps.model.Circle
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CommunityAdaptor(val context: Context,private val PostList: ArrayList<PostInfo>, var implemented: SetSeletedPostInfo) :
    RecyclerView.Adapter<CommunityAdaptor.ViewHolder>() {
    override fun getItemCount(): Int =  PostList.size

    class ViewHolder(view :View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.post_item_username)
        val content = view.findViewById<TextView>(R.id.post_item_content_preview)
        val title =  view.findViewById<TextView>(R.id.post_item_title)
        val image = view.findViewById<ImageView>(R.id.post_item_image)
        val date =  view.findViewById<TextView>(R.id.post_item_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_item,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = PostList[position].username
        holder.content.text = PostList[position].content
        holder.title.text = PostList[position].title
        holder.date.text = PostList[position].dates

        var service = PublicRetrofit.retrofit.create(LoadImage::class.java)
        var tmp = ImageInfoForLoad(PostList[position].number, "ProfileImages")
        service.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
            override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                Log.d("ImgLoadingObj", "이미지 출력 성공")
                var returndata = response.body()
                var byteArry = returndata?.data
                var tbitmap = byteArry?.let { it1 -> BitmapFactory.decodeByteArray( byteArry, 0, it1.size) }
                holder.image.setImageBitmap(tbitmap)
            }
            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                t.printStackTrace()
            }
        })

        holder.itemView.setOnClickListener {
            implemented.setSelectedPostInfo("ComminityPostDetail", PostList[position])
        }
    }
}