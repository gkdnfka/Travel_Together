package com.example.capstone_design

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.lang.Double.min
import java.lang.Math.*
import kotlin.math.pow

interface changeCourse{
    fun change(selectedCourse : Int)
}


class FindPathResult : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {
    // @솔빈 2022-2-10 (목)
    // INF -> dp 테이블을 채우는 과정에서 필요한 무한대의 값
    // Rvalue -> 위도, 경도를 거리로 변환시키기 위한 함수에서 사용되는 상수값
    val INF = 987654321.0
    val Rvalue = 6372.8 * 1000

    // @솔빈 2022-2-9 (수)
    // mView -> 구글맵 객체 선언(MapView 형식)
    // myGooglemap -> onMapReady() 콜백의 인자로 넘어오는 GoogleMap 객체를 저장하기 위한 변수. 추후에 구글맵 객체에 변화를 줄 때 참조하여 사용하기 위해 필요

    lateinit var mView : MapView
    lateinit var myGooglemap : GoogleMap

    // @솔빈 2022-2-9 (수)
    // placeinfoList -> 이전 페이지에서 선택한 여행지들을 순서대로 나열한 리스트, 자료형은 PlaceInfo
    // resultCourseList -> 결과로 반환된 경로들을 저장하는 이중 리스트. resultCorseList[i] -> i번째 여행지를 마지막으로 방문하는 최단 경로를 의미.
    // selectedCourse -> 선택된 경로의 인덱스(resultCourseList 리스트 상의)를 나타내는 변수

    var placeinfoList = ArrayList<PlaceInfo>()
    var resultCourseList = ArrayList<ArrayList<PlaceInfo>>()
    var selectedCourse : Int = 0

    // @솔빈 2022-2-9 (수)
    // initState -> 탐색을 시작하는 상태를 저장하는 변수. initDP 함수에서 1..1(1이 n개)으로 초기화됨. 1..1에서 시작해서 최종적으로 0..0 까지 순차적으로 진행
    // dp -> dp[now][state]로 정의. now 는 마지막으로 방문한 정점번호를 나타내고, state 는 현재 방문 상태를 나타냄. 1..1 인 경우, 모든 정점을 방문했음을 의미함.
    // distanceTable -> distanceTable[i][j]로 정의. i번째 여행지와 j번째 여행지 사이의 거리를 의미
    // lastVisit -> dp 테이블을 구하는 과정에서 현재 상태, 즉 dp[now][state]가 어떤 이전 상태공간을 참조했는지 저장(정점 번호를 저장)

    var initState = 0
    var dp = ArrayList<ArrayList<Double>>()
    var distanceTable = ArrayList<ArrayList<Double>>()
    var lastVisit = ArrayList<ArrayList<Int>>()
    var powList = ArrayList<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.path_result, container, false)

        placeinfoList = (activity as Activity).SelectedPlaceList
        initDP()
        for (i in 0 until placeinfoList.size) {
            calcDP(i, initState)
            resultCourseList.add(calcPath(i, initState))
        }

        mView = view.findViewById<MapView>(R.id.path_result_mapview)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        var courseRecycle = view.findViewById<RecyclerView>(R.id.path_result_course)
        courseRecycle.layoutManager = LinearLayoutManager((activity as Activity))
        courseRecycle.adapter = FindPathResultAdaptor(resultCourseList[selectedCourse], (activity as Activity))


        var courseNumberList = ArrayList<Int>()
        for (i in 0 until placeinfoList.size) courseNumberList.add(i)

        var courseSelectRecycle = view.findViewById<RecyclerView>(R.id.path_result_courseSelect)
        courseSelectRecycle.layoutManager = LinearLayoutManager((activity as Activity), LinearLayoutManager.HORIZONTAL, false)

        var Implemented = object : changeCourse {
            override fun change(courseNumber: Int) {
                selectedCourse = courseNumber
                courseRecycle.adapter = FindPathResultAdaptor(resultCourseList[selectedCourse], (activity as Activity))
                updateGooglemap()
            }
        }
        courseSelectRecycle.adapter = PathSelectAdaptor(courseNumberList, (activity as Activity), Implemented)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until resultCourseList[selectedCourse].size){
            Log.d("최종", resultCourseList[selectedCourse][i].toString())
            posLatLng.add(LatLng(resultCourseList[selectedCourse][i].PosY.toDouble(), resultCourseList[selectedCourse][i].PosX.toDouble()))
            var marker = MarkerOptions()
            marker.position(LatLng(resultCourseList[selectedCourse][i].PosY.toDouble(), resultCourseList[selectedCourse][i].PosX.toDouble())).title(resultCourseList[selectedCourse][i].name)
            googleMap.addMarker(marker)
        }

        val polyline = googleMap.addPolyline(posLatLng)
        if(resultCourseList[selectedCourse].size > 0){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(resultCourseList[selectedCourse][0].PosY.toDouble(), resultCourseList[selectedCourse][0].PosX.toDouble()), 10f))
            googleMap.setOnPolylineClickListener(this)
            googleMap.setOnPolygonClickListener(this)
        }

        myGooglemap = googleMap
    }

    fun updateGooglemap(){
        myGooglemap.clear()
        var posLatLng = PolylineOptions().clickable(true)
        for (i in 0 until resultCourseList[selectedCourse].size){
            Log.d("최종", resultCourseList[selectedCourse][i].toString())
            posLatLng.add(LatLng(resultCourseList[selectedCourse][i].PosY.toDouble(), resultCourseList[selectedCourse][i].PosX.toDouble()))
            var marker = MarkerOptions()
            marker.position(LatLng(resultCourseList[selectedCourse][i].PosY.toDouble(), resultCourseList[selectedCourse][i].PosX.toDouble())).title(resultCourseList[selectedCourse][i].name)
            myGooglemap.addMarker(marker)
        }

        val polyline = myGooglemap.addPolyline(posLatLng)
        if(resultCourseList[selectedCourse].size > 0){
            myGooglemap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(resultCourseList[selectedCourse][0].PosY.toDouble(), resultCourseList[selectedCourse][0].PosX.toDouble()), 10f))
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


    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (Rvalue * c)
    }

    // @솔빈 2022-2-11 금
    // 점화식 테이블을 구하기 위해 필요한 리스트, 변수들을 전처리 수행하는 함수
    fun initDP(){
        var bit = 1
        powList.add(bit)

        // 각 정점 사이의 거리를 계산하기 위한 반복문
        for (i in 0 until placeinfoList.size){
            // initState 라는 변수는 최종적으로 1..1 가 됨(1이 n개 만큼)
            initState = initState or bit
            bit *= 2
            powList.add(bit)

            var initDataForDistanceTable = ArrayList<Double>()
            for (j in 0 until placeinfoList.size) {
                // initDataForDistanceTable -> i 정점과 j 정점 사이의 거리로 초기화해준다.
                initDataForDistanceTable.add(getDistance(placeinfoList[i].PosY.toDouble(), placeinfoList[i].PosX.toDouble(),
                                                         placeinfoList[j].PosY.toDouble(), placeinfoList[j].PosX.toDouble()))
            }
            distanceTable.add(initDataForDistanceTable)
        }

        Log.d("상태공간 개수 ", bit.toString())
        var tbit = 1
        // dp 테이블을 INF 값으로 초기화하고, 테이블 값을 역산하기 위한 참조 히스토리 배열을 초기화 하기 위한 반복문
        for (i in 0 until placeinfoList.size){

            var initDataForDP = ArrayList<Double>()
            var initDataForLastVisit = ArrayList<Int>()

            // initDataForLastVisit -> 상태공간값에서 참조한 이전 상태공간의 정점 번호는 -1로 초기화한다.
            // initDataForDP -> j == 0 이면(state가 0이면)
            for (j in 0 until bit){
                initDataForDP.add(INF)
                initDataForLastVisit.add(-1)
            }

            initDataForDP[tbit] = 0.0
            dp.add(initDataForDP)
            lastVisit.add(initDataForLastVisit)
            tbit *= 2
        }
    }

    fun calcDP(now : Int, state : Int) : Double{
        // 메모라이징 되어있는 상태공간을 방문했다면, 그냥 그대로 값을 반환
        if(dp[now][state] != INF) return dp[now][state]

        // @솔빈 2022-2-11 금
        // bit -> 비트 마스킹을 통해 현재 상태와 비트연산 하기 위한 변수
        // minValue -> 현재 상태공간인 dp[now][state]에 들어갈 최소 값을 저장하기 위한 변수
        // minIdx   -> 최소 값에 해당하는 정점의 번호를 저장하기 위한 변수. 추후에 lastVisit[now][state]에 업데이트 됨.
        var bit : Int = 1
        var minValue = INF
        var minIdx = -1

        for (i in 0 until placeinfoList.size){
            // 현재 state 에서 비트가 1인 자리인지 아닌지를, bit 변수와 state 의 and 연산을 통해 검사
            if(((bit and state) != 0) && i != now){ // 1로 켜진 비트를 찾은 경우
                // 해당 정점에서 방문한 경우로 가정하고 calcDP(i, bit xor state) 재귀 함수 호출 / 의미 : 마지막으로 방문한 정점이 i 이고 방문 상태는 bit xor state
                if(minValue > calcDP(i, powList[now] xor state) + distanceTable[now][i]) {
                    minValue = calcDP(i, powList[now] xor state) + distanceTable[now][i]
                    minIdx = i
                }
            }
            bit *= 2
        }

        lastVisit[now][state] = minIdx
        dp[now][state] = minValue
        //Log.d("인자 사이즈", "now : " + now.toString() + " state : " + state.toString() + " dp값 : " + dp[now][state].toString() + " 참조 인덱스 : " + lastVisit[now][state].toString())
        return minValue
    }


    fun calcPath(start : Int, state : Int) : ArrayList<PlaceInfo>{
        var pathList = ArrayList<PlaceInfo>()
        pathList.add(placeinfoList[start])

        var now = start
        var nowState = state

        while(true){
            var lastpos = lastVisit[now][nowState]
            if(lastpos == -1) break
            pathList.add(placeinfoList[lastpos])
            nowState = nowState xor powList[now]
            now = lastpos
        }

        return pathList
    }
}


class FindPathResultAdaptor(private val items: ArrayList<PlaceInfo>, context : Context) : RecyclerView.Adapter<FindPathResultAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindPathResultAdaptor.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_course_place_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: FindPathResultAdaptor.ViewHolder, position: Int) {
        holder.nametext.text = items[position].name
        holder.addresstext.text = items[position].address
        holder.numbertext.text = (position+1).toString()
        Toast.makeText(contexts, items[position].name + " 입니다!", Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_name)
        var addresstext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_address)
        var numbertext: TextView = view.findViewById<TextView>(R.id.post_detail_course_place_number)
    }
}

class PathSelectAdaptor(private val items: ArrayList<Int>, context : Context, changeCourse : changeCourse) : RecyclerView.Adapter<PathSelectAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    var changeCourse = changeCourse

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathSelectAdaptor.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_course_day_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: PathSelectAdaptor.ViewHolder, position: Int) {
        holder.daytext.text = items[position].toString() + " 번째 경로"
        holder.daytext.setOnClickListener {
            changeCourse.change(position)
        }
        holder.daytext.width = 30
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var daytext: TextView = view.findViewById<TextView>(R.id.post_detail_day_textview)
    }
}



