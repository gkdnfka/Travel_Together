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
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.Interfaceset.remove_list_interface
import com.example.capstone_design.Interfaceset.update_list_interface
import com.example.capstone_design.R
import com.example.capstone_design.Util.GetBookmarkImage
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.Util.addFavorite
import com.example.capstone_design.selectPlace
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*
import kotlin.collections.ArrayList

// 리사이클러뷰 어댑터
class Post_Plan_Detail_Adapter_Update(val context: Context, val PlaceList: MutableList<PlaceInfo>, val updateListInterface: update_list_interface, val selectplace : selectPlace, val placeDetailPageInterface : PlaceDetailPageInterface)
    : RecyclerView.Adapter<Post_Plan_Detail_Adapter_Update.ViewHolder>() {
    private lateinit var dragListener: OnStartDragListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_write_detail_item,null)
        return ViewHolder(view)
    }

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = (position+1).toString()
        holder.title.text = PlaceList[position].name
        holder.remove.setOnClickListener {
            itemClickListener.onClick(it,position)
            removeData(position)
        }

        GetBookmarkImage("FavoritePlaceList", holder.bookmark, PlaceList[position].num)
        holder.bookmark.setOnClickListener {
            addFavorite("FavoritePlaceList", PlaceList[position].num)
            GetBookmarkImage("FavoritePlaceList", holder.bookmark, PlaceList[position].num)
        }


        var service = PublicRetrofit.retrofit.create(LoadImage::class.java)
        var tmp = ImageInfoForLoad(PlaceList[position].num, "PlaceImages")
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

        holder.more.setOnClickListener{
            placeDetailPageInterface.change(12, PlaceList[position], mbitmap)
        }
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = PlaceList.size


    // -----------------데이터 조작함수 추가-----------------

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        PlaceList.removeAt(position)
        updateListInterface.UpdateList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap( PlaceList, fromPos, toPos)
        updateListInterface.UpdateList()
        notifyItemMoved(fromPos, toPos)
    }
    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    // 뷰 홀더 설정
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView =itemView.findViewById(R.id.placename)
        val remove: TextView =itemView.findViewById(R.id.tvRemove)
        var Linear : LinearLayout = itemView.findViewById(R.id.post_write_detail_item_Linear)
        var bookmark = itemView.findViewById<ImageView>(R.id.post_write_detail_item_bookmark)
        var more = itemView.findViewById<ImageView>(R.id.post_write_detail_item_more)
        var number = itemView.findViewById<TextView>(R.id.post_write_detail_item_text)
    }
    fun afterDragAndDrop() {
        notifyDataSetChanged()
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