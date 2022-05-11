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
import com.example.capstone_design.Dataset.TagUpdateSuccess
import com.example.capstone_design.Dataset.TasteInfo
import com.example.capstone_design.Interfaceset.GetTagLabel
import com.example.capstone_design.Interfaceset.LoadUserTaste
import com.example.capstone_design.Interfaceset.UserTagUpdate
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.FieldPosition


class TagUpdate() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.join_page_select_tag, container, false)

        val joinBtn = view.findViewById<Button>(R.id.join_page_select_tag_join_btn)
        joinBtn.text = "수정하기"
        val backBtn = view.findViewById<ImageView>(R.id.join_page_select_tag_back_btn)

        val selectTagView = view.findViewById<RecyclerView>(R.id.tag_select_view)

        var mActivity = (activity as Activity)
        val retrofit = mActivity.retrofit

        //UserTaste 불러오기
        val funcName1 = "LoadTaste"
        val service1 = retrofit.create(LoadUserTaste::class.java)
        var SelectedIdx = ArrayList<Int>()
        var taste: List<String> = emptyList()
        service1.loadUserTaste(funcName1, mActivity.USER_CODE)
            .enqueue(object : Callback<TasteInfo> {
                override fun onFailure(call: Call<TasteInfo>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<TasteInfo>, response: Response<TasteInfo>) {
                    Log.d("성공", "Taste Load")

                    var returndata = response.body()
                    if (returndata != null) {
                        taste = returndata.taste.split(",")
                        Log.d("Taste", taste.toString())
                        for (idx in taste) {
                            SelectedIdx.add(idx.toInt())
                        }
                    }
                }
            })

        //TagLabel 데이터 불러오기
        var funcName = "GetTagLabel"
        var service = retrofit.create(GetTagLabel::class.java)

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
                    if (returndata != null) {

                        for (i in 0..returndata.size) {
                            isTagClicked.add(0)
                        }

                        for (idx in SelectedIdx) {
                            isTagClicked[idx] = 1
                        }

                        selectCnt = SelectedIdx.size
                        val SelectViewAdapter =
                            TagSelectAdaptor(mActivity, returndata!!, SelectedIdx)

                        var manager = GridLayoutManager(
                            mActivity,
                            3
                        )

                        selectTagView.apply {
                            layoutManager = manager
                        }

                        SelectViewAdapter.setItemClickListener(object :
                            TagSelectAdaptor.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {
                                Log.d("tag pos check", position.toString())
                                val itemText = v.findViewById<TextView>(R.id.tag_item_text)

                                if (isTagClicked[position-1] == 0) {
                                    if (selectCnt >= 5) {
                                        return
                                    }
                                    isTagClicked[position-1] = 1
                                    itemText.setBackgroundColor(Color.parseColor("#A0E7E5"))
                                    selectCnt++
                                } else {
                                    isTagClicked[position-1] = 0
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


        backBtn.setOnClickListener {
            mActivity.onBackPressed()
        }

        joinBtn.setOnClickListener {
            // @우람 2022-05-08
            // UserTaste 업데이트
            // 실패시 -1 반환.

            if (selectCnt != 0) {
                var str = ""
                for (i in 0..(isTagClicked.size - 1)) {
                    if (isTagClicked[i] == 1) {
                        str += i.toString()
                        str += ","
                    }
                }
                str = str.dropLast(1)
                var funcName2 = "UserTagUpdate"
                var service2 = retrofit.create(UserTagUpdate::class.java)

                service2.userTagUpdate(funcName2, mActivity.USER_CODE, str)
                    .enqueue(object : Callback<TagUpdateSuccess> {
                        override fun onFailure(call: Call<TagUpdateSuccess>, t: Throwable) {
                            Log.d("service2 실패", t.toString())
                        }

                        override fun onResponse(
                            call: Call<TagUpdateSuccess>,
                            response: Response<TagUpdateSuccess>
                        ) {
                            var returndata = response.body()
                            if (returndata!!.number == "1") {
                                Toast.makeText(mActivity, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                (activity as Activity).changeFragment(16)
                            } else if (returndata!!.number == "-1") {
                                Toast.makeText(mActivity, "수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

            } else {
                Toast.makeText(mActivity, "1개 이상의 태그를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }


        }

        return view
    }
}


