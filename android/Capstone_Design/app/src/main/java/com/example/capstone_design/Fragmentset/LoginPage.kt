package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Dataset.LoginSuccess
import com.example.capstone_design.R
import com.example.capstone_design.Util.FavoriteAddManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path



class LoginPage : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.login_page, container, false)

        // @솔빈 2022-02-21 월
        // 로그인 페이지의 뷰 선언 모음
        val login_id =  view.findViewById<EditText>(R.id.login_page_id_edit)
        val login_password =  view.findViewById<EditText>(R.id.login_page_password_edit)
        val loginBtn = view.findViewById<TextView>(R.id.login_page_login_btn)
        val backBtn = view.findViewById<ImageView>(R.id.login_page_back)
        var joinBtn = view.findViewById<TextView>(R.id.login_page_text_to_join)

        backBtn.setOnClickListener{
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
        }

        joinBtn.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.login_and_join_frameLayout,
                JoinPage()
            ).commit()
        }

        loginBtn.setOnClickListener {
            // @솔빈 2022-02-21 월
            // 로그인을 시도하는 로직
            // 로그인 성공여부는 서버단에서 처리하고, 반환값을 건네주는 방식
            (activity as MainActivity).serviceForLogin.sendLoginInfo("Login", login_id.text.toString(), login_password.text.toString()).enqueue(object: Callback<LoginSuccess> {
                override fun onFailure(call : Call<LoginSuccess>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<LoginSuccess>, response: Response<LoginSuccess>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    //Log.d("returnValueChk", returndata!!.number)

                    // @솔빈 2022-02-21 월
                    // 로그인에 성공한 경우 success 객체의 변수 number 에  1이 반환되고
                    // 로그인에 실패한 경우 success 객체의 변수 number 에 -1이 반환된다.
                    if(returndata!!.number == "1") {
                        // sharedpreference 를 통해 내부에 아이디, 이름, 비밀번호, 유저 CODE 저장.
                        FavoriteAddManager.prefs.setString("ID", login_id.text.toString())
                        FavoriteAddManager.prefs.setString("NAME", returndata.name)
                        FavoriteAddManager.prefs.setString("PASSWORD", login_password.text.toString())
                        FavoriteAddManager.prefs.setString("CODE", returndata.code)
                        startActivity((activity as MainActivity).intents)
                    }
                    else Toast.makeText((activity as MainActivity), "아이디나 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }
}