package com.example.capstone_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Community_Post_Write_Plan : Fragment(),update_list_interface {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var tmp_plan = inflater.inflate(R.layout.post_write_plan, container, false)
        val mActivity = activity as Activity
        var plan_day = mActivity.postplanlist.size + 1

        var plan_add_button = tmp_plan.findViewById<ImageButton>(R.id.post_write_plan_add)
        var post_plan_list = tmp_plan.findViewById<RecyclerView>(R.id.post_write_plan_ListView)

        var planlist: MutableList<PlanInfo> = mActivity.postplanlist
        val plan_list_adapter = Post_Plan_Adapter(tmp_plan.context, planlist)
        post_plan_list.adapter = plan_list_adapter
        post_plan_list.layoutManager = LinearLayoutManager(tmp_plan.context)
        plan_add_button.setOnClickListener {
            planlist.add(PlanInfo(plan_day, "",ArrayList()))
            plan_list_adapter.notifyDataSetChanged()
            mActivity.postplanlist = planlist
            plan_day += 1
        }
        plan_list_adapter.setItemClickListener(object: Post_Plan_Adapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                mActivity.day = planlist[position].component1()
                mActivity.place_list = planlist[position].component3()
                mActivity.postplanlist = planlist
                mActivity.changeFragment(5)
            }
        })
        return tmp_plan
    }

    override fun UpdateList() {
        val mActivity = activity as Activity
        for(i:Int in mActivity.day..(mActivity.postplanlist.size-1)){
            mActivity.postplanlist[i].day -= 1
        }
        mActivity.postplanlist.removeAt(mActivity.day-1)
    }
}