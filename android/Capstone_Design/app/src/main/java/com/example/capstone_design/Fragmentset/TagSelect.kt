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
import com.example.capstone_design.Adapterset.TagSelectAdaptorForDict
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.TagDictSet
import com.example.capstone_design.Dataset.TagLabelSet
import com.example.capstone_design.Interfaceset.GetTagDict
import com.example.capstone_design.Interfaceset.GetTagLabel
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.FieldPosition


class TagSelect() : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.page_select_tag, container, false)

        val backBtn = view.findViewById<ImageView>(R.id.page_select_backbtn)
        val selectThemeView = view.findViewById<RecyclerView>(R.id.page_select_theme)
        val selectTagView = view.findViewById<RecyclerView>(R.id.page_select_tagrecycler)

        //TagLabel 데이터 불러오기
        val funcName = "GetTagLabel"

        var mActivity = (activity as Activity)
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetTagLabel::class.java)
        val serviceForTagDict = retrofit.create(GetTagDict::class.java)

        var selectedIndex = 0
        val isTagClicked = ArrayList<Int>()
        val isSelectedDetailTagClicked = mActivity.isSelectedDetailTagClicked
        var tagDictList = mActivity.tagDictList


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
                        for(i in 0..returndata.size+1) isTagClicked.add(0)
                        if(isSelectedDetailTagClicked.size == 0){
                            for(i in 0..returndata.size+1){
                                isSelectedDetailTagClicked.add(ArrayList<Int>())
                                tagDictList.add(ArrayList<TagDictSet>())
                            }
                        }

                        val SelectViewAdapter =
                            TagSelectAdaptor(mActivity, returndata!!, isTagClicked )

                        var manager = LinearLayoutManager(mActivity)
                        manager.orientation = RecyclerView.HORIZONTAL

                        selectThemeView.apply {
                            layoutManager = manager
                        }

                        serviceForTagDict.getTagDict("GetTagDict", "ALL", "")
                            .enqueue(object : Callback<ArrayList<TagDictSet>> {
                                override fun onFailure(call: Call<ArrayList<TagDictSet>>, t: Throwable) {
                                    Log.d("실패", t.toString())
                                }

                                override fun onResponse(call: Call<ArrayList<TagDictSet>>, response: Response<ArrayList<TagDictSet>>){
                                    var returndata = response.body()

                                    if(isSelectedDetailTagClicked[1].size == 0){
                                        for (i in 0 until returndata!!.size) {
                                            isSelectedDetailTagClicked[returndata[i].labelnum.toInt()].add(0)
                                            tagDictList[returndata[i].labelnum.toInt()].add(returndata[i])
                                        }
                                    }

                                    SelectViewAdapter.setItemClickListener(object:TagSelectAdaptor.OnItemClickListener{
                                        override fun onClick(v: View, position: Int) {
                                            Log.d("tag pos check", position.toString())
                                            val itemText = v.findViewById<TextView>(R.id.tag_item_text)

                                            if(isTagClicked[position-1] == 0) {
                                                isTagClicked[selectedIndex] = 0
                                                selectedIndex = position-1
                                                isTagClicked[position-1] = 1
                                                itemText.setBackgroundColor(Color.parseColor("#A0E7E5"))
                                                SelectViewAdapter.notifyDataSetChanged()


                                                var tmp = TagSelectAdaptorForDict(mActivity, tagDictList[selectedIndex+1], isSelectedDetailTagClicked[selectedIndex+1])

                                                tmp.setItemClickListener(object :TagSelectAdaptorForDict.OnItemClickListener{
                                                    override fun onClick(v: View, position: Int) {
                                                        if(isSelectedDetailTagClicked[selectedIndex+1][position] == 0) {
                                                            isSelectedDetailTagClicked[selectedIndex+1][position] = 1
                                                            tmp.notifyDataSetChanged()
                                                        }
                                                        else {
                                                            isSelectedDetailTagClicked[selectedIndex+1][position] = 0
                                                            tmp.notifyDataSetChanged()
                                                        }
                                                    }
                                                })

                                                var manager = GridLayoutManager((activity as Activity),
                                                    3)
                                                selectTagView.apply{ layoutManager = manager }
                                                selectTagView.adapter = tmp
                                            }
                                        }
                                    }

                                    )
                                }
                            })
                        selectThemeView.adapter = SelectViewAdapter
                    }

                }
            })





        backBtn.setOnClickListener{
            mActivity.onBackPressed()
        }

        return view
    }
}


