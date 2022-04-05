package com.example.capstone_design.Fragmentset

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.Post_Plan_Adapter_Update
import com.example.capstone_design.Util.ItemTouchHelperCallback
import com.example.capstone_design.Adapterset.Post_Plan_Detail_Adapter
import com.example.capstone_design.Adapterset.Post_Plan_Detail_Adapter_Update
import com.example.capstone_design.Interfaceset.update_list_interface
import com.example.capstone_design.R
import com.example.capstone_design.Util.SwipeHelperCallback
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomButtons.OnBMClickListener
import com.nightonke.boommenu.BoomMenuButton

class Community_Post_Write_Plan_Detail : Fragment(), update_list_interface {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mActivity = activity as Activity
        var tmp_plan = inflater.inflate(R.layout.post_write_plan_detail, container, false)
        // 게시글 수정 버튼 추가
        var detail_search = tmp_plan.findViewById<ImageButton>(R.id.detailSearchButton)
        var detail_check = tmp_plan.findViewById<ImageButton>(R.id.detailCheckButton)
        var detail_day = tmp_plan.findViewById<TextView>(R.id.post_write_plan_detail_day)
        var detail_backspace = tmp_plan.findViewById<ImageButton>(R.id.detailBackSpace)

        var detail_list = tmp_plan.findViewById<RecyclerView>(R.id.post_write_plan_detail_list)
        // 리사이클러뷰 어댑터 달기
        val detail_list_adapter = Post_Plan_Detail_Adapter_Update(tmp_plan.context,mActivity.place_list,this)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val callback = SwipeHelperCallback(detail_list_adapter).apply{
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        // touchHelper 선언
        val touchHelper =  ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(detail_list)

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        detail_list.setOnTouchListener { _, _ ->
            callback.removePreviousClamp(detail_list)
            false
        }

        detail_list.adapter = detail_list_adapter
        detail_list.layoutManager = LinearLayoutManager(tmp_plan.context)
        // adapater에 있는 인터페이스 상속받아 선언하는 부분
        detail_list_adapter.setItemClickListener(object: Post_Plan_Detail_Adapter_Update.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // remove 동작시 이전에 선택했던 뷰와 현재 뷰에 대한 swipe 고정 해제
                callback.removeCurrentClamp(detail_list)
                callback.removePreviousClamp(detail_list)
            }
        })
        detail_day.text = mActivity.day.toString() + "일차 여행계획"
        // 여행지 검색 기능
        detail_search.setOnClickListener {
            mActivity.changeFragment(10)
        }
        detail_check.setOnClickListener{

                val builder = AlertDialog.Builder(tmp_plan.context)
                builder.setTitle("완료하시겠습니까?")
                    .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        var CurrentCourse = ""
                        for (i in 0..mActivity.postplanlist[mActivity.day-1].place_list.size-1)
                        {
                            CurrentCourse += mActivity.postplanlist[mActivity.day-1].place_list[i].name
                            if(i == mActivity.postplanlist[mActivity.day-1].place_list.size-1)
                            {
                                continue
                            }
                            CurrentCourse += " -> "
                        }
                        mActivity.postplanlist[mActivity.day-1].place_list = mActivity.place_list
                        mActivity.postplanlist[mActivity.day-1].day = mActivity.day
                        mActivity.changeFragment(8)
                    })
                    .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        Toast.makeText(tmp_plan.context,"취소했습니다", Toast.LENGTH_SHORT).show()
                    })
                builder.show()
        }
        /* 취소기능
          val builder = HamButton.Builder().listener(OnBMClickListener {
                    val builder = AlertDialog.Builder(tmp_plan.context)
                    builder.setTitle("삭제하시겠습니까?")
                        .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                            for(i:Int in mActivity.day..(mActivity.postplanlist.size-1)){
                                mActivity.postplanlist[i].day -= 1
                            }
                            mActivity.postplanlist.removeAt(mActivity.day-1)
                            mActivity.changeFragment(8)
                        })
                        .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                            Toast.makeText(tmp_plan.context,"취소했습니다", Toast.LENGTH_SHORT).show()
                        })

                    builder.show()
        * */
        detail_backspace.setOnClickListener{
            // postwrite_plan으로 이동
         mActivity.changeFragment(8)
        }
        return tmp_plan
    }

    override fun UpdateList() {
        val mActivity = activity as Activity
        mActivity.postplanlist[mActivity.day-1].place_list = mActivity.place_list
        var CurrentCourse = ""
        for (i in 0..mActivity.postplanlist[mActivity.day-1].place_list.size-1)
        {
            CurrentCourse += mActivity.postplanlist[mActivity.day-1].place_list[i].name
            if(i == mActivity.postplanlist[mActivity.day-1].place_list.size-1)
            {
                continue
            }
            CurrentCourse += " -> "
        }
        mActivity.postplanlist[mActivity.day-1].course = CurrentCourse
    }
}