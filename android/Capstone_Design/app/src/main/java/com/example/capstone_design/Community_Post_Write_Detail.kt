package com.example.capstone_design

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Community_Post_Write_Plan_Detail : Fragment(),update_list_interface{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mActivity = activity as Activity

        var tmp_plan = inflater.inflate(R.layout.post_write_plan_detail, container, false)
        var detail_day = tmp_plan.findViewById<TextView>(R.id.post_write_plan_detail_day)
        var detail_search = tmp_plan.findViewById<Button>(R.id.post_write_plan_detail_search)

        var detail_list = tmp_plan.findViewById<RecyclerView>(R.id.post_write_plan_detail_list)
        val detail_list_adapter = Post_Plan_Detail_Adapter(tmp_plan.context,mActivity.place_list,this)
        var detail_button = tmp_plan.findViewById<Button>(R.id.post_write_plan_detail_complete)
        var detail_delete = tmp_plan.findViewById<Button>(R.id.post_write_plan_detail_delete)
        val callback = ItemTouchHelperCallback(detail_list_adapter)
        val touchHelper =  ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(detail_list)
        detail_list.adapter = detail_list_adapter
        detail_list.layoutManager = LinearLayoutManager(tmp_plan.context)

        detail_list_adapter.startDrag(object : Post_Plan_Detail_Adapter.OnStartDragListener{
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
                mActivity.postplanlist[mActivity.day-1].place_list = mActivity.place_list
            }
        })
        detail_day.text = mActivity.day.toString() + "일차 여행계획"
        detail_search.setOnClickListener {
            mActivity.changeFragment(6)
        }
        detail_button.setOnClickListener {
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
                    mActivity.changeFragment(4)
                })
                .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(tmp_plan.context,"취소했습니다", Toast.LENGTH_SHORT).show()
                })

            builder.show()

        }
        detail_delete.setOnClickListener{
            val builder = AlertDialog.Builder(tmp_plan.context)
            builder.setTitle("삭제하시겠습니까?")
                .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    for(i:Int in mActivity.day..(mActivity.postplanlist.size-1)){
                        mActivity.postplanlist[i].day -= 1
                    }
                    mActivity.postplanlist.removeAt(mActivity.day-1)
                    mActivity.changeFragment(4)
                })
                .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(tmp_plan.context,"취소했습니다", Toast.LENGTH_SHORT).show()
                })

            builder.show()


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