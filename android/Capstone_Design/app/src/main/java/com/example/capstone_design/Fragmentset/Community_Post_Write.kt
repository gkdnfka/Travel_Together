package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.PostWriteInfo
import com.example.capstone_design.R
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomButtons.OnBMClickListener
import com.nightonke.boommenu.BoomMenuButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Community_Post_Write : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var tmp = inflater.inflate(R.layout.post_write, container, false)
        // 2022-03-06 오픈 UI를 이용해 버튼 새로운 버튼 생성
        var bmb = tmp.findViewById<BoomMenuButton>(R.id.bmb)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.101:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PostWriteInfo::class.java)
        val mActivity = activity as Activity
        for(i in 0..bmb.piecePlaceEnum.pieceNumber()){
            if(i == 0)
            {
                val builder = HamButton.Builder().listener(OnBMClickListener {
                    mActivity.changeFragment(7)
                }).normalText("본문내용추가")
                bmb.addBuilder(builder)
            }
            if(i == 1)
            {
                val builder = HamButton.Builder().listener(OnBMClickListener {
                    mActivity.changeFragment(8)
                }).normalText("여행계획추가")
                bmb.addBuilder(builder)
            }
            if(i == 2)
            {
                val builder = HamButton.Builder().listener(OnBMClickListener {
                    var funcName = "PostWrite"
                    var typeName = "default"
                    var str_Title = mActivity.postName
                    var str_Content = mActivity.postContent
                    var User_ID = mActivity.USER_ID
                    var User_NAME = mActivity.USER_NAME
                    var User_CODE = mActivity.USER_CODE
                    var Course_insert : String = ""
                    // 2022-02-21 정지원 작업
                    // course 데이터도 for문 돌려서 하나의 string으로 만들어줌(Course_insert가 서버에 들어가는 Course)
                    for(i:Int in 0 .. mActivity.postplanlist.size - 1)
                    {
                        for(j:Int in 0..mActivity.postplanlist[i].place_list.size - 1)
                        {
                            Course_insert += mActivity.postplanlist[i].place_list[j].num
                            if(j == mActivity.postplanlist[i].place_list.size - 1)
                            {
                                continue
                            }
                            Course_insert += ","
                        }
                        if(i == mActivity.postplanlist.size - 1)
                        {
                            continue
                        }
                        Course_insert += "/"
                    }
                    // 2022-02-20 정지원 작업
                    // postmain 부분은 str_title + str_content로 묶어서 데이터 전송
                    // userdata 부분은 User_ID + User_NAME + User_CODE 로 묶어서 데이터 전송 이때 서버단계에서 ']'로 스플릿해서 사용
                    // 2022-02-21 정지원 작업
                    // Course_insert 삽입을 추가했음
                    service.postwriteinfo(funcName, typeName, str_Title+']'+str_Content, User_CODE.toString()+']'+User_NAME,Course_insert).enqueue(object:Callback<ArrayList<PostInfo>> {
                        override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                            Log.d("실패", t.toString())
                        }
                        override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                            Log.d("성공", "입출력 성공")
                        }
                    })
                    mActivity.changeFragment(3)
                }).normalText("게시글업로드")
                bmb.addBuilder(builder)
            }
        }
        return tmp
    }


}







