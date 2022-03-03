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

        // @솔빈 2022-02-21 월
        // 회원가입 페이지의 뷰 선언 모음
        val join_name = view.findViewById<EditText>(R.id.join_page_name_edit)
        val join_id =  view.findViewById<EditText>(R.id.join_page_id_edit)
        val join_password =  view.findViewById<EditText>(R.id.join_page_password_edit)
        val join_gender =  view.findViewById<RadioGroup>(R.id.join_page_gender_select)
        val join_address =  view.findViewById<EditText>(R.id.join_page_address_edit)
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
            // @솔빈 2022-02-21 월
            // 회원가입을 시도하는 로직
            // 회원가입 성공여부는 서버단에서 처리하고, 반환값을 건네주는 방식

            // 빈칸이 하나라도 입력되어 있는 경우 인정하지 않음.
            if(join_name.text.toString() == "" || join_id.text.toString() == "" || join_password.text.toString() == ""
                                               || gender == "" || join_address.text.toString() == ""){
                Toast.makeText((activity as MainActivity), "필요한 정보를 모두 입력해주세요. ", Toast.LENGTH_SHORT).show()
            }

            else{
                (activity as MainActivity).serviceForJoin.sendJoinInfo("Join", join_id.text.toString(), join_password.text.toString(), join_name.text.toString(), gender, join_address.text.toString(),).enqueue(object: Callback<JoinSuccess> {
                    override fun onFailure(call : Call<JoinSuccess>, t : Throwable){
                        Log.d("실패", t.toString())
                    }

                    override fun onResponse(call: Call<JoinSuccess>, response: Response<JoinSuccess>) {
                        Log.d("성공", "DB 입출력 성공")
                        var returndata = response.body()

                        // @솔빈 2022-02-21 월
                        // 회원가입에 성공한 경우 success 객체의 변수 number 에  1이 반환되고
                        // 회원가입에 실패한 경우 success 객체의 변수 number 에  0보다 작은 값이 반환된다.
                        if(returndata!!.number == "1") {
                            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
                            Toast.makeText((activity as MainActivity), "회원가입 완료.", Toast.LENGTH_SHORT).show()
                        }
                        else if(returndata!!.number == "-1") Toast.makeText((activity as MainActivity), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        return view
    }
}