package com.example.capstone_design.Fragmentset

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.R
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor_Post
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// 2022-03-03 정지원 작업
// SearchPlace 사용 중첩으로 인한 Post로 분기를 하나 했음. 기존의 listview 방식에서 recyclerview 방식으로 바꿨음
class SearchPlace_Post : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var tmp = inflater.inflate(R.layout.search_tourist_spot, container, false)
        val mActivity = activity as Activity
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetPlaceInfo::class.java)
        val removeText = tmp.findViewById<ImageButton>(R.id.SearchRemove)
        val SearchToggle = tmp.findViewById<ToggleButton>(R.id.Search_Tourist_Spot_Toggle)
        val edit = tmp.findViewById<EditText>(R.id.Search_Tourist_Spot_Edit)
        var listView = tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
        var place_list: ArrayList<PlaceInfo> = ArrayList()


        listView.layoutManager = LinearLayoutManager(tmp.context)
        edit.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(hasFocus){
                    removeText.visibility = View.VISIBLE
                }
                else
                {
                    removeText.visibility = View.INVISIBLE
                }
            }
        })
        edit.setOnKeyListener { v, keycode, event ->

            if(event.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER)
            {

                // 2022-03-13 정지원 작업
                // 토글버튼에 따른 분기 true = 게시물이름 false = 유저이름

                // @솔빈 2022-01-26 (수)
                // 레트로핏의 서비스 객체를 활용해서, 인터페이스의 함수인 getpostinfo를 호출.
                // enqueue 메소드의 인자로 콜백 함수 하나를 선언해서 삽입
                // 해당 함수는 onResponse 함수와 onFailure 함수를 오버라이드 해야함
                // onResponse 에 응답 성공시 response 객체가 인자로 전달됨.
                // onResponse 내의 response.body()를 통해 최종적으로 ArrayList<PostInfo>  형태로 데이터 전달받을 수 있음!

                val funcName = "SearchPlace"
                var typeName = ""
                val strName = edit.text.toString()
                if (SearchToggle.isChecked == true) typeName = "ByAddr"
                else typeName = "ByName"
                edit.text = null
                service.getplaceinfo(funcName, typeName, strName).enqueue(object :
                    Callback<ArrayList<PlaceInfo>> {
                    override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                        Log.d("실패", t.toString())
                    }

                    override fun onResponse(
                        call: Call<ArrayList<PlaceInfo>>,
                        response: Response<ArrayList<PlaceInfo>>
                    ) {
                        Log.d("성공", "입출력 성공")
                        place_list = response.body()!!
                        if (place_list != null) {
                            var Implemented = object : PlaceDetailPageInterface {
                                override fun change(index: Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
                                    (activity as Activity).SelectedPlace = placeinfo
                                    if(bitmap != null) (activity as Activity).SelectedBitmap = bitmap
                                    (activity as Activity)!!.changeFragment(index)
                                }
                            }

                            var SearchAdapter_Post = SearchPlaceAdaptor_Post(place_list, tmp.context, Implemented)
                            listView.adapter = SearchAdapter_Post
                            SearchAdapter_Post.setItemClickListener(object : SearchPlaceAdaptor_Post.OnItemClickListener {
                                override fun onClick(v: View, position: Int) {
                                    val builder = AlertDialog.Builder(tmp.context)
                                    var select_place = place_list[position]
                                    builder.setTitle(place_list[position].component2())
                                        .setMessage("선택하시겠습니까?")
                                        .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                                            mActivity.place_list.add(select_place)
                                            mActivity.postplanlist[mActivity.day - 1].place_list = mActivity.place_list
                                            var CurrentCourse = ""
                                            for (j in 0..mActivity.postplanlist[mActivity.day - 1].place_list.size - 1) {
                                                CurrentCourse += mActivity.postplanlist[mActivity.day - 1].place_list[j].name
                                                if (j == mActivity.postplanlist[mActivity.day - 1].place_list.size - 1) {
                                                    continue
                                                }
                                                CurrentCourse += " -> "
                                            }
                                            mActivity.postplanlist[mActivity.day - 1].course = CurrentCourse
                                            mActivity.changeFragment(9)
                                        })
                                        .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                                            Toast.makeText(tmp.context, "취소했습니다", Toast.LENGTH_SHORT).show()
                                        })

                                    builder.show()
                                }
                            })
                        }
                    }
                })
                // 2022-03-13 정지원 작업
                // 검색 종료시 검색창의 text를 null로 비우고 검색창의 포커스를 해제한다

                edit.clearFocus()
            }
            true
        }
        removeText.setOnClickListener {
           edit.text = null
        }
        return tmp
    }
}

