package com.example.capstone_design.Fragmentset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityPostDetailDayAdaptor
import com.example.capstone_design.Adapterset.CommunityPostDetailPlaceAdaptor
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.ChangeFragment
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.Interfaceset.changeDay
import com.example.capstone_design.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import org.w3c.dom.Text

// @솔빈 2022-01-27 (목)
// CourseFragment -> 경로를 출력하는 프래그먼트
class CourseFragment() : Fragment(),
    OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener
{

    // mView -> 구글맵 객체
    lateinit var mView : MapView
    var placeinfoList = ArrayList<ArrayList<PlaceInfo>>()
    var selectedDay : Int = 0
    var dayCount = 0
    var tempMarkerQueue = ArrayList<Marker?>()
    lateinit var marker_root_view : View
    lateinit var tv_marker : TextView
    lateinit var myGooglemap: GoogleMap

    interface InterfaceForCourseRecycler{
        fun changeFragAndInitSelectedPlace(index : Int, placeinfo : PlaceInfo, bitmap : Bitmap?)
        fun moveCamerato(PosX: Double, PosY: Double, idx : Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_course_item, container, false)
        var mActivity = (activity as Activity)

        marker_root_view = LayoutInflater.from((activity as Activity)).inflate(R.layout.marker_layout, null)
        tv_marker = marker_root_view.findViewById<TextView>(R.id.marker_layout_text)

        selectedDay = mActivity.SelectedDayInPostDetail
        placeinfoList = mActivity.SelectedPostPlaceInfoList
        dayCount = placeinfoList.size

        mView = view.findViewById<MapView>(R.id.post_detail_course_map)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)


        // @솔빈 2022-03-22 화: 프래그먼트 전환을 위한 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
        //                      여행지 세부 정보 페이지로의 전환을 위해 필요하다.
        var interfaceForCourseRecycler = object : InterfaceForCourseRecycler {
            override fun changeFragAndInitSelectedPlace(index : Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
                (activity as Activity).SelectedPlace = placeinfo
                if(bitmap != null) (activity as Activity).SelectedBitmap = bitmap
                (activity as Activity)!!.changeFragment(index)
            }
            override fun moveCamerato(PosX: Double, PosY: Double, idx : Int) {
                myGooglemap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(PosX,PosY),10f))
                tempMarkerQueue[idx]?.showInfoWindow()
            }
        }
        var courseRecycle = view.findViewById<RecyclerView>(R.id.post_detail_course_place_RecyclerView)
        if(placeinfoList.size != 0){
            var manager_course = LinearLayoutManager((activity as Activity), LinearLayoutManager.VERTICAL, false)
            courseRecycle.apply {
                layoutManager = manager_course
            }
            courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity), interfaceForCourseRecycler)
        }

        // @솔빈 2022-1-25 (화)
        // dayArray -> 날짜 배열
        // Implemented -> google map view 객체를 선택된 날짜에 따라 업데이트하고,
        // 경로 여행지 어답터를 업데이트하기 위한 인터페이스 객체
        // manager -> 날짜 리싸이클러뷰를 horizontal로 설정해주기 위해서 필요함
        var manager_day = LinearLayoutManager((activity as Activity), LinearLayoutManager.HORIZONTAL, false)

        var dayRecycle = view.findViewById<RecyclerView>(R.id.post_detail_course_day_RecyclerView)
        dayRecycle.apply {
            layoutManager = manager_day
        }

        var dayArray = ArrayList<Int>()
        for (i in 0 until dayCount) dayArray.add(i+1)
        var Implemented = object : changeDay {
            override fun changeday(day: Int){
                mActivity.SelectedDayInPostDetail = day
                selectedDay = mActivity.SelectedDayInPostDetail
                dayRecycle.adapter!!.notifyDataSetChanged()
                if(placeinfoList.size != 0) courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity), interfaceForCourseRecycler)
                updateGooglemap()
            }

            override fun getSelectedDay() : Int{
                return selectedDay
            }
        }

        // 이하 어답터 및 매니저 붙여주는 과정

        dayRecycle.adapter = CommunityPostDetailDayAdaptor(dayArray, (activity as Activity), Implemented)
        return view
    }

    private val PATTERN_GAP_LENGTH_PX = 10
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    // @솔빈 2022-01-29 (토)
    // onMapReady -> 구글맵 view에서 동작할 로직이 작성되는 함수
    override fun onMapReady(googleMap: GoogleMap) {
        tempMarkerQueue.clear()
        if(placeinfoList.size == 0) return
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until placeinfoList[selectedDay].size){
            posLatLng.add(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble()))
            var marker = MarkerOptions()
            tv_marker.setText((i+1).toString())
            marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView((activity as Activity), marker_root_view)));
            marker.position(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble())).title((i+1).toString() + "번, " + placeinfoList[selectedDay][i].name)
            var tmarker = googleMap.addMarker(marker)
            tempMarkerQueue.add(tmarker)
        }

        val polyline = googleMap.addPolyline(posLatLng)
        polyline.pattern = PATTERN_POLYLINE_DOTTED
        if(placeinfoList.size != 0 && placeinfoList[selectedDay].size != 0){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
            googleMap.setOnPolylineClickListener(this)
            googleMap.setOnPolygonClickListener(this)
        }
        myGooglemap = googleMap
    }

    // @솔빈 2022-2-5 (토)
    // 구글맵 객체를 업데이트하는 함수. 날짜가 바뀔때마다 날짜에 해당하는 경로로 마커와 직선이 바뀜.
    fun updateGooglemap(){
        myGooglemap.clear()
        tempMarkerQueue.clear()
        if(placeinfoList.size == 0) return
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until placeinfoList[selectedDay].size){
            posLatLng.add(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble()))
            var marker = MarkerOptions()
            tv_marker.setText((i+1).toString())
            marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView((activity as Activity), marker_root_view)));
            marker.position(LatLng(placeinfoList[selectedDay][i].PosY.toDouble(), placeinfoList[selectedDay][i].PosX.toDouble())).title((i+1).toString() + "번, " + placeinfoList[selectedDay][i].name)
            var tmarker = myGooglemap.addMarker(marker)
            tempMarkerQueue.add(tmarker)
        }

        val polyline = myGooglemap.addPolyline(posLatLng)
        polyline.pattern = PATTERN_POLYLINE_DOTTED

        if(placeinfoList.size != 0 && placeinfoList[selectedDay].size != 0){
            myGooglemap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
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

    fun createDrawableFromView(context : Context, view : View) : Bitmap {
        var displayMetrics = DisplayMetrics();
        (activity as Activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        var bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        var canvas = Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}