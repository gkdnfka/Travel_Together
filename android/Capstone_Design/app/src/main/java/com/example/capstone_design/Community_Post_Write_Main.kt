package com.example.capstone_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

class Community_Post_Write_Main : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var tmp_main = inflater.inflate(R.layout.post_write_main, container, false)
        var post_main_title = tmp_main.findViewById<EditText>(R.id.post_write_main_title)
        var post_main_content = tmp_main.findViewById<EditText>(R.id.post_write_main_content)
        val mActivity = activity as Activity
        post_main_title.setText(mActivity.postName)
        post_main_content.setText(mActivity.postContent)
        // 2022-02-20 정지원 작업
        // edittext에 리스너를 달아서 문자 변경시에 즉각 반영되게 변경
        post_main_title.addTextChangedListener {
            mActivity.postName = post_main_title.text.toString()
        }
        post_main_content.addTextChangedListener{
            mActivity.postContent = post_main_content.text.toString()
        }
        return tmp_main
    }
}