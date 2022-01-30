package com.example.capstone_design
import android.content.Context
import android.graphics.text.TextRunShaper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView

import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class CommentFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.post_detail_comment_item, container, false)
    }
}

// @솔빈 2022-01-27 (목)
// ContentFragment -> 게시글의 본문을 출력하는 프래그먼트
class ContentFragment(content : String) : Fragment()
{
    var mcontent = content
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_content_item, container, false)
        var contentTextView = view.findViewById<TextView>(R.id.post_detail_content_textview)
        contentTextView.text = mcontent
        return view
    }
}

// @솔빈 2022-01-27 (목)
// CourseFragment -> 경로를 출력하는 프래그먼트
class CourseFragment(courseList : Array<Array<String>>, placeinfoList : Array<ArrayList<PlaceInfo>>) : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener
{
    // mView -> 구글맵 객체
    lateinit var mView : MapView
    var courseList = courseList
    var placeinfoList = placeinfoList
    var selectedDay : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_course_item, container, false)
        mView = view.findViewById<MapView>(R.id.post_detail_course_map)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        for (i in 0 until placeinfoList[selectedDay].size){
            Log.d("전달 테스트 ", placeinfoList[selectedDay][i].name)
        }

        var recycle = view.findViewById<RecyclerView>(R.id.post_detail_course_place_list)
        recycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity))
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

        val polyline = googleMap.addPolyline(posLatLng)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 6f))
        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)
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




class CommunityPostDetail : Fragment()
{
    // @솔빈 2022-01-29 (토)
    // selectedPostInfo -> Activity 내에 존재하는, 자세히 보기가 적용되는 게시글의 데이터를 담고있는 PostInfo 클래스 객체
    lateinit var selectedPostInfo : PostInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_fragment, container, false)
        selectedPostInfo = (activity as Activity).SelectedPostInfo

        // @솔빈 2022-01-29 (토)
        // 1. 경로 문자열 파싱 -> parseCourseList()
        // 2. DB 에서 검색 통해서 좌표값 불러와서 저장 -> loadPosList()
        parseCourseList()
        loadPosList()

        childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,Main()).commit()
        var bottom : BottomNavigationView = view.findViewById(R.id.post_detail_fragment_menu)
        var BackButton = view.findViewById<Button>(R.id.post_detail_exit)

        bottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.post_detail_comment_button ->{ childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,CommentFragment()).commit() }
                R.id.post_detail_content_button-> { childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,ContentFragment(selectedPostInfo.content)).commit() }
                R.id.post_detail_course_button-> { childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,CourseFragment(courseList, placeinfoList)).commit() }
            }
            true
        }

        BackButton.setOnClickListener {
            (activity as Activity).supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, Community()).commit()
        }
        return view
    }

    // @솔빈  2022-01-29 (토)
    // ---변수 ---
    // course -> 이동 경로 문자열을 저장하는 변수
    // courseList -> 행이 일(day)차, 열이 경로를 나타내는 2차원 배열
    // posList -> 각각의 여행지의 x,y 좌표를 저장하는 3차원 배열
    // dayCount -> 여행 일수

    private var course : String = ""
    private val maxDayLength = 20
    private val maxPlaceLength = 50
    lateinit var courseList : Array<Array<String>>
    lateinit var placeinfoList : Array<ArrayList<PlaceInfo>>
    var daycount : Int = 0

    // @솔빈  2022-01-29 (토)
    // ---함수 설명---
    // ParseCourseList -> 자세히 보기 기능이 선택된 게시글의 이동경로 문자열을 파싱하는 함수.
    // 결과적으로 courseList 에 (행 : 날짜, 열 : 여행지 번호)로 값이 초기화됨.
    // 문자열 형식 example) 123,23,125,42/1,2,3,4,5
    // -> 1일차에 123번 -> 23번 -> 125번 -> 42번 순으로 여행지 방문
    // -> 2일차에 1번 -> 2번 -> 3번 -> 4번 -> 5번 순으로 여행지 방문

    private fun parseCourseList() {
        course = selectedPostInfo.course
        courseList = Array(maxDayLength, { Array(maxPlaceLength, {""}) })

        var tmpStr = ""
        var courseCount = 0

        // 문자열 파싱 로직
        for (i in 0 until course.length){
            if(course[i] != '/' && course[i] != ',') tmpStr += course[i]
            else{
                courseList[daycount][courseCount] = tmpStr
                courseCount += 1
                tmpStr = ""
                if(course[i] == '/') {
                    daycount += 1
                    courseCount = 0
                }
            }

            if(i == course.length-1){
                courseList[daycount][courseCount] = tmpStr
                daycount += 1
            }
        }
    }

    // @솔빈  2022-01-29 (토)
    // ---함수 설명---
    // loadPosList -> 날짜별로 분류된 여행지 코스 2차원 배열인 courseList를 기반으로
    // DB에 쿼리문을 날려서 각각의 여행지의 좌표를 불러오고, posList에 저장한다.

    private fun loadPosList(){
        placeinfoList = Array(maxDayLength, { ArrayList<PlaceInfo>() })

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GetPlaceInfo::class.java)


        for (i in 0 until daycount){
            var strForQuery = ""
            var j = 0

            while(courseList[i][j] != ""){
                strForQuery +=  "testnum = " + courseList[i][j]
                if(j+1 < maxPlaceLength && courseList[i][j+1] != "" ) strForQuery += " OR "
                j++
            }

            service.getplaceinfo("SearchPlace", "ByIds", strForQuery).enqueue(object: Callback<ArrayList<PlaceInfo>> {
                override fun onFailure(call : Call<ArrayList<PlaceInfo>>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<ArrayList<PlaceInfo>>, response: Response<ArrayList<PlaceInfo>>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()

                    // @솔빈 2022-01-29 (토)
                    // 이하 로직은 좌표값을 posList에 저장함.
                    // mysql 쿼리문의 결과로, 여행지의 순서에 상관없이 여행지의 고유번호에 따라 오름차순으로 결과가 반환되므로
                    // 2중 포문을 통해 여행지 고유번호 순서에 맞게 데이터를 넣어주는 동작을 수행함.
                    if(returndata != null){
                        for (q in 0 until maxPlaceLength){
                            if(courseList[i][q] == "") break
                            for (j in 0 until returndata.size){
                                if(returndata[j].number == courseList[i][q]){
                                    placeinfoList[i].add(returndata[j])
                                }
                            }
                        }

                    }
                }
            })
        }
    }
}