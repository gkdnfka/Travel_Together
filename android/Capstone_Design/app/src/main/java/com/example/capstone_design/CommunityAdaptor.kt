package com.example.capstone_design
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class CommunityAdaptor(val context : Context, private val PlaceList : ArrayList<PostInfo>) : BaseAdapter() {
    override fun getCount(): Int {
        return PlaceList.size
    }

    override fun getItem(p0: Int): Any {
        return PlaceList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다고 함 */
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_item, null)


        val Name = view.findViewById<TextView>(R.id.post_item_username)
        val content = view.findViewById<TextView>(R.id.post_item_content_preview)

        val element = PlaceList[p0]
        Name.text = element.username
        content.text = element.content
        return view
    }
}