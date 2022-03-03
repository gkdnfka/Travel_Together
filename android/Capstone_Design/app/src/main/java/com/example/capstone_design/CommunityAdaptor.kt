package com.example.capstone_design
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView


class CommunityAdaptor(
    val context: Context,
    private val PostList: ArrayList<PostInfo>,
    var implemented: SetSeletedPostInfo
) : BaseAdapter() {

    override fun getCount(): Int {
        return PostList.size
    }

    override fun getItem(p0: Int): Any {
        return PostList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다고 함 */
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_item, null)
        val Name = view.findViewById<TextView>(R.id.post_item_username)
        val content = view.findViewById<TextView>(R.id.post_item_content_preview)
        val title =  view.findViewById<TextView>(R.id.post_item_title)
        val element = PostList[p0]
        Name.text = element.username
        content.text = element.content
        title.text = element.title
        view.setOnClickListener{
            implemented.setSelectedPostInfo("ComminityPostDetail", element)
        }


        return view
    }
}