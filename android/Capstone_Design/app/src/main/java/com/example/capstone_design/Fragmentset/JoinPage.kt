package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path



class JoinPage : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.join_page, container, false)

        // 휴양	액티비티	쇼핑  역사적_장소   자연_경관	전시물_관람  공연_관람 성별

        // @솔빈 2022-02-21 월
        // 회원가입 페이지의 뷰 선언 모음
        val join_name = view.findViewById<EditText>(R.id.join_page_name_edit)
        val join_id =  view.findViewById<EditText>(R.id.join_page_id_edit)
        val join_password =  view.findViewById<EditText>(R.id.join_page_password_edit)
        val join_gender =  view.findViewById<RadioGroup>(R.id.join_page_gender_select)
        val joinBtn = view.findViewById<Button>(R.id.join_page_join_btn)
        val backBtn = view.findViewById<ImageView>(R.id.join_page_back)

        // gender은 추후에 남성, 여성 라디오 버튼 선택함에 따라 변경됨.
        var gender = ""
        join_gender.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.join_page_male -> gender = "남성"
                R.id.join_page_female -> gender = "여성"
            }
        }

        backBtn.setOnClickListener{
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
        }

        joinBtn.setOnClickListener {
            // @솔빈 2022-03-12 월
            // 취향 조사 페이지로 넘어가는 동작 구현
            if(join_name.text.toString() == "" || join_id.text.toString() == "" || join_password.text.toString() == ""
                || gender == ""){
                Toast.makeText((activity as MainActivity), "회원 정보를 모두 입력해주세요. ", Toast.LENGTH_SHORT).show()
            }
            else{
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, JoinTagSelect(join_name.text.toString(), join_id.text.toString(), join_password.text.toString(), gender, "") ).commit()
            }

        }

        return view
    }
}