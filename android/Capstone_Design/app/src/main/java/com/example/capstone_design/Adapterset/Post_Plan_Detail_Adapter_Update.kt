package com.example.capstone_design.Adapterset

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.remove_list_interface
import com.example.capstone_design.Interfaceset.update_list_interface
import com.example.capstone_design.R

import java.util.*
import kotlin.collections.ArrayList

// 리사이클러뷰 어댑터
class Post_Plan_Detail_Adapter_Update(val context: Context, val PlaceList: MutableList<PlaceInfo>,val updateListInterface: update_list_interface)
    : RecyclerView.Adapter<Post_Plan_Detail_Adapter_Update.ViewHolder>() {
    private lateinit var dragListener: OnStartDragListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_write_detail_item,null)
        return ViewHolder(view)
    }

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = PlaceList[position].name
        holder.adress.text = PlaceList[position].address
        holder.remove.setOnClickListener {
            itemClickListener.onClick(it,position)
            removeData(position)
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
        val adress: TextView = itemView.findViewById(R.id.placeadress)
        val remove: TextView =itemView.findViewById(R.id.tvRemove)

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