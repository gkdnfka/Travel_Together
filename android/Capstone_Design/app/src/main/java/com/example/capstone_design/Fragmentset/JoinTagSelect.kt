package com.example.capstone_design.Fragmentset

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Adapterset.TagSelectAdaptor
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.TagLabelSet
import com.example.capstone_design.Interfaceset.GetTagLabel
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.FieldPosition


class JoinTagSelect(val join_name : String, val join_id : String, val join_password : String, val gender : String, val join_address : String) : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.join_page_select_tag, container, false)

        val joinBtn = view.findViewById<Button>(R.id.join_page_select_tag_join_btn)
        val backBtn = view.findViewById<ImageView>(R.id.join_page_select_tag_back_btn)

        val selectTagView = view.findViewById<RecyclerView>(R.id.tag_select_view)

        //TagLabel 데이터 불러오기
        val funcName = "GetTagLabel"

        var mActivity = (activity as MainActivity)
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetTagLabel::class.java)

        var selectCnt = 0
        val isTagClicked = ArrayList<Int>()
        service.getTagLabel(funcName)
            .enqueue(object : Callback<ArrayList<TagLabelSet>> {
                override fun onFailure(call: Call<ArrayList<TagLabelSet>>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<TagLabelSet>>,
                    response: Response<ArrayList<TagLabelSet>>
                ) {
                    Log.d("성공", "Tag Load")
                    var returndata = response.body()
                    if(returndata != null) {

                        Log.d("성공", isTagClicked.size.toString())
                        for(i in 0..returndata.size) {
                            isTagClicked.add(0)
                        }

                        val SelectViewAdapter =
                            TagSelectAdaptor((activity as MainActivity), returndata!! )

                        var manager = GridLayoutManager((activity as MainActivity),
                        3)

                        selectTagView.apply {
                            layoutManager = manager
                        }

                        SelectViewAdapter.setItemClickListener(object:TagSelectAdaptor.OnItemClickListener{
                            override fun onClick(v: View, position: Int) {
                                Log.d("tag pos check", position.toString())
                                val itemText = v.findViewById<TextView>(R.id.tag_item_text)

                                if(isTagClicked[position] == 0) {
                                    if(selectCnt >= 5) {
                                        return
                                    }
                                    isTagClicked[position] = 1
                                    itemText.setBackgroundColor(Color.parseColor("#A0E7E5"))
                                    selectCnt++
                                }
                                else {
                                    isTagClicked[position] = 0
                                    itemText.setBackgroundColor(Color.parseColor("#D5E6C4"))
                                    selectCnt--
                                }
                            }
                        }

                        )
                        selectTagView.adapter = SelectViewAdapter
                    }

                }
            })


        backBtn.setOnClickListener{
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
        }

        joinBtn.setOnClickListener {
            // @솔빈 2022-02-21 월
            // 회원가입을 시도하는 로직
            // 회원가입 성공여부는 서버단에서 처리하고, 반환값을 건네주는 방식
            // 빈칸이 하나라도 입력되어 있는 경우 인정하지 않음.

            if (selectCnt != 0) {
                var str = ""
                for (i in 0..(isTagClicked.size - 1)) {
                    if(isTagClicked[i] == 1) {
                        str += i.toString()
                        str += ","
                    }
                }
                str = str.dropLast(1)

                (activity as MainActivity).serviceForJoin.sendJoinInfo(
                    "Join",
                    join_id,
                    join_password,
                    join_name,
                    gender,
                    join_address,
                    str
                ).enqueue(object : Callback<JoinSuccess> {
                    override fun onFailure(call: Call<JoinSuccess>, t: Throwable) {
                        Log.d("실패", t.toString())
                    }

                    override fun onResponse(
                        call: Call<JoinSuccess>,
                        response: Response<JoinSuccess>
                    ) {
                        Log.d("성공", "DB 입출력 성공")
                        var returndata = response.body()

                        // @솔빈 2022-02-21 월
                        // 회원가입에 성공한 경우 success 객체의 변수 number 에  1이 반환되고
                        // 회원가입에 실패한 경우 success 객체의 변수 number 에  0보다 작은 값이 반환된다.
                        if (returndata!!.number == "1") {
                            (activity as MainActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.login_and_join_frameLayout, SettingFragment()).commit()
                            Toast.makeText(
                                (activity as MainActivity),
                                "회원가입 완료.",
                                Toast.LENGTH_SHORT
                            ).show()


                            (activity as MainActivity).USER_PASSWORD = join_password
                            (activity as MainActivity).USER_ID = join_id
                        } else if (returndata!!.number == "-1") Toast.makeText(
                            (activity as MainActivity),
                            "이미 존재하는 아이디입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            else {
                Toast.makeText((activity as MainActivity), "1개 이상의 태그를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}


