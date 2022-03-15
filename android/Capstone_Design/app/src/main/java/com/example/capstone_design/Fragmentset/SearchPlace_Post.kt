package com.example.capstone_design.Fragmentset

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor_Post
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
        var button = tmp.findViewById<Button>(R.id.Search_Tourist_Spot_Button)
        val chkbox = tmp.findViewById<RadioGroup>(R.id.RadioGroup)
        val edit = tmp.findViewById<EditText>(R.id.Search_Tourist_Spot_Edit)
        var listView = tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
        var place_list: ArrayList<PlaceInfo> = ArrayList()


        listView.layoutManager = LinearLayoutManager(tmp.context)

        button.setOnClickListener {
            val funcName = "SearchPlace"
            var typeName = ""
            val strName = edit.text.toString()
            if (chkbox.checkedRadioButtonId == R.id.radioButton_address) typeName = "ByAddr"
            else typeName = "ByName"

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
                        var SearchAdapter_Post = SearchPlaceAdaptor_Post(place_list, tmp.context)
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
        }

        return tmp
    }
}

