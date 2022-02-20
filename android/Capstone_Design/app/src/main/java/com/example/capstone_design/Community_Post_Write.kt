package com.example.capstone_design

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.widget.addTextChangedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Community_Post_Write : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var tmp = inflater.inflate(R.layout.post_write, container, false)
        var post_main = tmp.findViewById<Button>(R.id.post_write_main)
        var post_plan = tmp.findViewById<Button>(R.id.post_write_plan)
        var post_submit = tmp.findViewById<Button>(R.id.post_write_submit)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.101:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PostWriteInfo::class.java)
        val mActivity = activity as Activity

        post_main.setOnClickListener {
            mActivity.changeFragment(3)
        }
        post_plan.setOnClickListener {
            mActivity.changeFragment(4)
        }
        post_submit.setOnClickListener {
            var funcName = "PostWrite"
            var typeName = "default"
            var str_Title = mActivity.postName
            var str_Content = mActivity.postContent
            var User_ID = mActivity.USER_ID
            var User_NAME = mActivity.USER_NAME
            var User_CODE = mActivity.USER_CODE
            // 2022-02-20 정지원 작업
            // postmain 부분은 str_title + str_content로 묶어서 데이터 전송
            // userdata 부분은 User_ID + User_NAME + User_CODE 로 묶어서 데이터 전송 이때 서버단계에서 ']'로 스플릿해서 사용
            service.postwriteinfo(funcName, typeName, str_Title+']'+str_Content, User_CODE.toString()+']'+User_NAME).enqueue(object:Callback<ArrayList<PostInfo>> {
                override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                    Log.d("실패", t.toString())
                }
                override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                    Log.d("성공", "입출력 성공")
                }
            })
        }
        return tmp
    }


}
class Community_Post_Write_Main : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var tmp_main = inflater.inflate(R.layout.post_write_main, container, false)
        var post_main_title = tmp_main.findViewById<EditText>(R.id.post_write_main_title)
        var post_main_content = tmp_main.findViewById<EditText>(R.id.post_write_main_content)
        val mActivity = activity as Activity

        // 2022-02-20 정지원 작업
        // edittext에 리스너를 달아서 문자 변경시에 즉각 반영되게 변경
        post_main_title.addTextChangedListener {
            mActivity.postName = post_main_title.text.toString()
        }
        post_main_content.addTextChangedListener{
            mActivity.postContent = post_main_content.text.toString()
        }
        return tmp_main
    }
}
class Community_Post_Write_Plan : Fragment(){
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var tmp_plan = inflater.inflate(R.layout.post_write_plan, container, false)
        var planlist : MutableList<PlanInfo> = ArrayList()
        var plan_day = 1
        val plan_list_adapter = Post_Plan_Adapter(tmp_plan.context,planlist)
        var plan_add_button = tmp_plan.findViewById<Button>(R.id.post_write_plan_add)
        var post_plan_list = tmp_plan.findViewById<ListView>(R.id.post_write_plan_ListView)
        val mActivity = activity as Activity
        post_plan_list.adapter = plan_list_adapter
        plan_add_button.setOnClickListener {
            planlist.add(PlanInfo(plan_day.toString()+"일차",""))
            plan_list_adapter.notifyDataSetChanged()
            plan_day += 1
        }
        return tmp_plan
    }
}
class Community_Post_Write_Plan_Detail : Fragment(){
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var tmp_plan = inflater.inflate(R.layout.post_write_plan_detail, container, false)

        return tmp_plan
    }
}
