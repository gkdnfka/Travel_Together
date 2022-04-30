package com.example.capstone_design.Fragmentset
import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.R
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor_Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchPlace : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var tmp = inflater.inflate(R.layout.search_tourist_spot, container, false)
        var mActivity = (activity as Activity)
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
                // onResponse 내의 response.body()를 통해 ArrayList<PostInfo> 최종적으로 형태로 데이터 전달받을 수 있음!

                val funcName = "SearchPlace"
                var typeName = ""
                val strName = edit.text.toString()
                if (SearchToggle.isChecked == true) typeName = "ByAddr"
                else typeName = "ByName"
                edit.text = null
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("실패", t.toString())
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("성공", "입출력 성공")
                            var returndata = response.body()
                            if (returndata != null) {
                                val SearchAdapter =
                                    SearchPlaceAdaptor(returndata!!, (activity as Activity))

                                var manager = LinearLayoutManager(
                                    (activity as Activity),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                var listView =
                                    tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
                                listView.apply {
                                    layoutManager = manager
                                }
                                place_list = response.body()!!
                                if (place_list != null) {
                                    val SearchAdapter = SearchPlaceAdaptor(place_list, tmp.context)
                                    listView.adapter = SearchAdapter
                                }
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

