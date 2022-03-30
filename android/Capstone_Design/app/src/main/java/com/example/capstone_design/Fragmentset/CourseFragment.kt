package com.example.capstone_design.Fragmentset

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityPostDetailDayAdaptor
import com.example.capstone_design.Adapterset.CommunityPostDetailPlaceAdaptor
import com.example.capstone_design.Adapterset.changeDay
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.ChangeFragment
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

// @솔빈 2022-01-27 (목)
// CourseFragment -> 경로를 출력하는 프래그먼트
class CourseFragment(placeinfoList : ArrayList<ArrayList<PlaceInfo>>, dayCount : Int) : Fragment(),
    OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener
{
    // mView -> 구글맵 객체
    lateinit var mView : MapView
    var placeinfoList = placeinfoList
    var selectedDay : Int = 0
    var dayCount = dayCount
    lateinit var myGooglemap: GoogleMap

    interface ChangeFragAndInitSelectedPlace{
        fun changeFragAndInitSelectedPlace(index : Int, placeinfo : PlaceInfo, bitmap : Bitmap)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_course_item, container, false)
        mView = view.findViewById<MapView>(R.id.post_detail_course_map)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)


        // @솔빈 2022-03-22 화: 프래그먼트 전환을 위한 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
        //                      여행지 세부 정보 페이지로의 전환을 위해 필요하다.
        var FragmentChangeListener = object : ChangeFragAndInitSelectedPlace {
            override fun changeFragAndInitSelectedPlace(index : Int, placeinfo: PlaceInfo, bitmap: Bitmap) {
                (activity as Activity).SelectedPlace = placeinfo
                (activity as Activity).SelectedBitmap = bitmap
                (activity as Activity)!!.changeFragment(index)
            }
        }

        var courseRecycle = view.findViewById<RecyclerView>(R.id.post_detail_course_place_RecyclerView)
        courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity), FragmentChangeListener)

        // @솔빈 2022-1-25 (화)
        // dayArray -> 날짜 배열
        // Implemented -> google map view 객체를 선택된 날짜에 따라 업데이트하고,
        // 경로 여행지 어답터를 업데이트하기 위한 인터페이스 객체
        var dayArray = ArrayList<Int>()
        for (i in 0 until dayCount) dayArray.add(i+1)
        var Implemented = object : changeDay {
            override fun changeday(day: Int) {
                selectedDay = day
                courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity), FragmentChangeListener)
                updateGooglemap()
            }
        }

        // 이하 어답터 및 매니저 붙여주는 과정
        // manager -> 날짜 리싸이클러뷰를 horizontal로 설정해주기 위해서 필요함
        var manager = LinearLayoutManager((activity as Activity), LinearLayoutManager.HORIZONTAL, false)
        var dayRecycle = view.findViewById<RecyclerView>(R.id.post_detail_course_day_RecyclerView)
        dayRecycle.apply {
            layoutManager = manager
        }
        dayRecycle.adapter = CommunityPostDetailDayAdaptor(dayArray, (activity as Activity), Implemented)

        return view
    }

    // @솔빈 2022-01-29 (토)
    // onMapReady -> 구글맵 view에서 동작할 로직이 작성되는 함수
    override fun onMapReady(googleMap: GoogleMap) {
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until placeinfoList[selectedDay].size){
            posLatLng.add(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble()))
            var marker = MarkerOptions()
            marker.position(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble())).title(placeinfoList[selectedDay][i].name)
            googleMap.addMarker(marker)
        }

        if(placeinfoList.size != 0 && placeinfoList[selectedDay].size != 0){
            val polyline = googleMap.addPolyline(posLatLng)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
            googleMap.setOnPolylineClickListener(this)
            googleMap.setOnPolygonClickListener(this)
        }
        myGooglemap = googleMap
    }

    // @솔빈 2022-2-5 (토)
    // 구글맵 객체를 업데이트하는 함수. 날짜가 바뀔때마다 날짜에 해당하는 경로로 마커와 직선이 바뀜.
    fun updateGooglemap(){
        myGooglemap.clear()
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until placeinfoList[selectedDay].size){
            posLatLng.add(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble()))
            var marker = MarkerOptions()
            marker.position(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble())).title(placeinfoList[selectedDay][i].name)
            myGooglemap.addMarker(marker)
        }

        if(placeinfoList.size != 0 && placeinfoList[selectedDay].size != 0){
            val polyline = myGooglemap.addPolyline(posLatLng)
            myGooglemap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
            myGooglemap.setOnPolylineClickListener(this)
            myGooglemap.setOnPolygonClickListener(this)
        }
    }

    override fun onStart() {
        super.onStart()
        mView.onStart()
    }
    override fun onStop() {
        super.onStop()
        mView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }

    override fun onPolylineClick(p0: Polyline) {

    }

    override fun onPolygonClick(p0: Polygon) {

    }
}