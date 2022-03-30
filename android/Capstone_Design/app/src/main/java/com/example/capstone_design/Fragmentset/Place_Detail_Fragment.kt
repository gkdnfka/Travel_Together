package com.example.capstone_design.Fragmentset


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.R

class Place_Detail_Fragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.place_detail_fragment, container, false)

        // @솔빈 2022-03-22 (화)
        // 뷰 객체 및 액티비티 객체 초기화 파트
        var mActivity = (activity as Activity)

        var placeImage = view.findViewById<ImageView>(R.id.place_detail_fragment_image)
        var placeName = view.findViewById<TextView>(R.id.place_detail_fragment_place_name)
        var placeAddr = view.findViewById<TextView>(R.id.place_detail_fragment_place_addr)
        var placePhone = view.findViewById<TextView>(R.id.place_detail_fragment_phone)
        var placeWeb = view.findViewById<TextView>(R.id.place_detail_fragment_website)
        var openHourLinear = view.findViewById<LinearLayout>(R.id.place_detail_fragment_open_hour_linear_layout)
        var openHourInfoString = mActivity.SelectedPlace.openhour
        var backbutton = view.findViewById<ImageView>(R.id.place_detail_back_button)

        backbutton.setOnClickListener {
            mActivity.onBackPressed()
            //mActivity.changeFragment(11)
        }

        if(mActivity.SelectedBitmap != null) placeImage.setImageBitmap(mActivity.SelectedBitmap)
        placeName.setText(mActivity.SelectedPlace.name)
        placeAddr.setText(mActivity.SelectedPlace.address)
        placePhone.setText(mActivity.SelectedPlace.phonenumber)
        placeWeb.setText(mActivity.SelectedPlace.website)

        var idx = 0
        var string = ""

        while(idx < openHourInfoString.length){
            if(openHourInfoString[idx] == '/') {
                var newTextView = TextView(context)
                newTextView.setText(string)
                openHourLinear.addView(newTextView)
                string = ""
            }
            else string += openHourInfoString[idx]
            idx++
        }

        return view
    }
}