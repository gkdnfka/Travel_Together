package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Dataset.PlanInfo
import com.example.capstone_design.Adapterset.Post_Plan_Adapter
import com.example.capstone_design.Adapterset.Post_Plan_Adapter_Update
import com.example.capstone_design.Interfaceset.remove_list_interface
import com.example.capstone_design.Interfaceset.update_list_interface
import com.example.capstone_design.R
import com.example.capstone_design.Util.SwipeHelperCallback
import com.example.capstone_design.Util.SwipeHelperCallbackPlan
import com.nightonke.boommenu.BoomMenuButton


class Community_Post_Write_Plan : Fragment(), remove_list_interface {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var tmp_plan = inflater.inflate(R.layout.post_write_plan, container, false)
        val mActivity = activity as Activity
        var plan_add_button = tmp_plan.findViewById<ImageView>(R.id.post_write_plan_add)
        var post_plan_list = tmp_plan.findViewById<RecyclerView>(R.id.post_write_plan_ListView)
        var planlist: MutableList<PlanInfo> = mActivity.postplanlist
        val plan_list_adapter = Post_Plan_Adapter_Update(tmp_plan.context, planlist,this)
        post_plan_list.adapter = plan_list_adapter
        post_plan_list.layoutManager = LinearLayoutManager(tmp_plan.context)
        val callback = SwipeHelperCallbackPlan(plan_list_adapter).apply{
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        val touchHelper =  ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(post_plan_list)
        plan_add_button.setOnClickListener {
            planlist.add(PlanInfo(planlist.size + 1, "",ArrayList()))
            plan_list_adapter.notifyDataSetChanged()
            mActivity.postplanlist = planlist
        }
        post_plan_list.setOnTouchListener { _, _ ->
            callback.removePreviousClamp(post_plan_list)
            false
        }
        plan_list_adapter.setItemClickListener(object:Post_Plan_Adapter_Update.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                callback.removeCurrentClamp(post_plan_list)
            }
        })
        return tmp_plan
    }

    override fun RemoveList(position : Int) {
        val mActivity = activity as Activity
        val start = position + 1
        for(i:Int in start..(mActivity.postplanlist.size-1)){
            mActivity.postplanlist[i].day -= 1
        }
        mActivity.postplanlist.removeAt(position)
    }
    override fun changeDetail(planList: MutableList<PlanInfo>,position:Int) {
        val mActivity = activity as Activity
        mActivity.day = planList[position].component1()
        mActivity.place_list = planList[position].component3()
        mActivity.postplanlist = planList
        mActivity.changeFragment(9)
    }
}