package com.example.capstone_design.Fragmentset

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dinuscxj.refresh.RecyclerRefreshLayout
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Adapterset.PostBringAdapter
import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.BringPost
import com.example.capstone_design.Interfaceset.GetPostInfo
import com.example.capstone_design.Interfaceset.SetSelectedBringPost
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.R
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomMenuButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostBring : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val funcName = "BringPostGet"
        val typeName = "default"
        // 2022-03-26 정지원 작업
        // 변수들 목록
        var view = inflater.inflate(R.layout.post_bring, container, false)
        val mActivity = activity as Activity
        val retrofit = mActivity.retrofit
        val BringList = view.findViewById<RecyclerView>(R.id.post_bring_list)
        val service = retrofit.create(BringPost::class.java)
        service.bringPost(funcName,typeName,"",mActivity.USER_CODE,"").enqueue(object : Callback<ArrayList<BringPostInfo>>{
            override fun onFailure(call: Call<ArrayList<BringPostInfo>>, t: Throwable) {
                Log.d("실패", t.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<BringPostInfo>>,
                response: Response<ArrayList<BringPostInfo>>
            ) {
                Log.d("성공", "DB 입출력 성공")
                var returndata = response.body()
                if(returndata != null)
                {
                    var Implemented = object : SetSelectedBringPost{
                        override fun SetSelectedBringPost(
                            element: BringPostInfo
                        ) {
                            mActivity.SetSelectedBringPost(element)
                            mActivity.changeFragment(2)
                        }
                    }
                    val postbringadapter = PostBringAdapter(view.context,returndata!!,Implemented)
                    BringList.adapter = postbringadapter
                    BringList.layoutManager = LinearLayoutManager(view.context)
                }
            }
        })
        return view
    }
}