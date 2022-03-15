package com.example.capstone_design.Fragmentset
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstone_design.R

// @솔빈 2022-01-27 (목)
// ContentFragment -> 게시글의 본문을 출력하는 프래그먼트
class ContentFragment(content : String, title : String) : Fragment()
{
    var mcontent = content
    var mtitle = title
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_content_item, container, false)
        var contentTextView = view.findViewById<TextView>(R.id.post_detail_content_content_text)
        var titleTextView = view.findViewById<TextView>(R.id.post_detail_content_title_text)
        if(mcontent != null) contentTextView.text = mcontent
        else contentTextView.text = ""
        if(mtitle != null) titleTextView.text = mtitle
        else titleTextView.text = ""

        return view
    }
}
