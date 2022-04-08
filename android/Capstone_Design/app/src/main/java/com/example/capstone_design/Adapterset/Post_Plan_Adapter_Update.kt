package com.example.capstone_design.Adapterset

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.PlanInfo
import com.example.capstone_design.Interfaceset.remove_list_interface
import com.example.capstone_design.R
import java.util.*
import kotlin.collections.ArrayList

// 리사이클러뷰 어댑터
class Post_Plan_Adapter_Update(val context: Context, val PlanList: MutableList<PlanInfo>, val removeListInterface: remove_list_interface)
    : RecyclerView.Adapter< Post_Plan_Adapter_Update.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.plan_item,null)
        return ViewHolder(view)
    }

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day.text = PlanList[position].day.toString() + "일차"
        holder.course.text = PlanList[position].course
        holder.change.setOnClickListener {
            removeListInterface.changeDetail(PlanList,position)
        }
        holder.remove.setOnClickListener {
            itemClickListener.onClick(it,position)
            removeData(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = PlanList.size


    // -----------------데이터 조작함수 추가-----------------

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        removeListInterface.RemoveList(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap( PlanList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    // 뷰 홀더 설정
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day : TextView = itemView.findViewById(R.id.plan_day)
        val course : TextView = itemView.findViewById(R.id.plan_course)
        val remove : TextView = itemView.findViewById(R.id.plan_remove)
        val change: TextView = itemView.findViewById(R.id.plan_change)
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