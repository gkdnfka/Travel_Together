package com.example.capstone_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class SearchPlaceAdaptor(val context : Context, private val PlaceList : ArrayList<PlaceInfo>) : BaseAdapter() {
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
        val view: View = LayoutInflater.from(context).inflate(R.layout.tourist_spot_item, null)


        val Photo = view.findViewById<ImageView>(R.id.PlaceImage)
        val Name = view.findViewById<TextView>(R.id.PlaceNameText)
        val Addr = view.findViewById<TextView>(R.id.PlaceAddrText)

        val element = PlaceList[p0]
        Name.text = element.name
        Addr.text = element.address
        return view
    }
}