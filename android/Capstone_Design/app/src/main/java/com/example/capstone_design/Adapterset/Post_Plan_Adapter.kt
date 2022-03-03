package com.example.capstone_design.Adapterset
import android.view.LayoutInflater
import android.view.ViewGroup

import android.content.Context

import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.PlanInfo
import com.example.capstone_design.R

class Post_Plan_Adapter(val context: Context, val planList: MutableList<PlanInfo>):
    RecyclerView.Adapter<Post_Plan_Adapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val day: TextView = itemView.findViewById(R.id.plan_item_day)
        val course: TextView = itemView.findViewById(R.id.plan_item_course)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.plan_item, null)
        return ViewHolder(view)
    }
    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return planList.size
    }
    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day.text = planList[position].day.toString() + "일차"
        holder.course.text = planList[position].course
        // (!1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    // (!2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (!3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (!4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
    // (4) 레이아웃 내 View 연결


}
