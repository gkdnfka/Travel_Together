package com.example.capstone_design
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class Post_Plan_Adapter(val context : Context, private val items : MutableList<PlanInfo>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다고 함 */
        val view: View = LayoutInflater.from(context).inflate(R.layout.plan_item, null)
        val Name = view.findViewById<TextView>(R.id.plan_item_day)
        val content = view.findViewById<TextView>(R.id.plan_item_course)
        val element = items[p0]
        Name.text = element.day
        content.text = element.course
        return view
    }
}