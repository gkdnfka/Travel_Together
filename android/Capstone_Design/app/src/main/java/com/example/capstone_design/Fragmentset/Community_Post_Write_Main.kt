package com.example.capstone_design.Fragmentset

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.TagSelectAdaptorForDict
import com.example.capstone_design.Dataset.TagDictSet
import com.example.capstone_design.R

class Community_Post_Write_Main : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var tmp_main = inflater.inflate(R.layout.post_write_main, container, false)
        var post_main_title = tmp_main.findViewById<EditText>(R.id.post_write_main_title)
        var post_main_content = tmp_main.findViewById<EditText>(R.id.post_write_main_content)
        var tagaddbtn = tmp_main.findViewById<ImageView>(R.id.post_write_main_tag_add)
        var tagLinear = tmp_main.findViewById<LinearLayout>(R.id.post_write_main_tag_Linear)
        var tagSelectList = ArrayList<TagDictSet>()
        val mActivity = activity as Activity

        val inflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sizes = mActivity.isSelectedDetailTagClicked.size
        for (i in 0 until sizes){
            val s = mActivity.isSelectedDetailTagClicked[i].size
            for (j in 0 until s){
                if(1 == mActivity.isSelectedDetailTagClicked[i][j]){
                    var flag = 0
                    for (q in 0 until tagSelectList.size){
                        if(tagSelectList[q].name == mActivity.tagDictList[i][j].name){
                            flag = 1
                            break;
                        }
                    }
                    if(flag == 0){
                        tagSelectList.add(mActivity.tagDictList[i][j])
                        var view = inflater.inflate(R.layout.tag_item, tagLinear, false)
                        view.findViewById<TextView>(R.id.tag_item_text).text =  "#" + mActivity.tagDictList[i][j].name
                        tagLinear.addView(view)
                    }
                }
            }
        }
        mActivity.sendingList = tagSelectList

        post_main_title.setText(mActivity.postName)
        post_main_content.setText(mActivity.postContent)
        // 2022-02-20 ????????? ??????
        // edittext??? ???????????? ????????? ?????? ???????????? ?????? ???????????? ??????
        post_main_title.addTextChangedListener {
            mActivity.postName = post_main_title.text.toString()
        }
        post_main_content.addTextChangedListener{
            mActivity.postContent = post_main_content.text.toString()
        }
        tagaddbtn.setOnClickListener {
            mActivity.changeFragment(16)
        }



        return tmp_main
    }
}