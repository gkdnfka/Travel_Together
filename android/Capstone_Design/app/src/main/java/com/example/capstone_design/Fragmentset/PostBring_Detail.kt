package com.example.capstone_design.Fragmentset

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Adapterset.PostBringDetailAdapter
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor
import com.example.capstone_design.Dataset.*
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.NaverAPI
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align

import java.io.IOException
import java.lang.Exception

import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.nightonke.boommenu.BoomButtons.*
import com.nightonke.boommenu.BoomMenuButton
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import org.checkerframework.checker.units.UnitsTools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList
import org.checkerframework.checker.units.UnitsTools.deg





class PostBringDetail : Fragment(), OnMapReadyCallback {
    lateinit var travelInfo : BringPostInfo
    private lateinit var mapView: MapView
    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    //권한 가져오기
    //내 위치를 가져오는 코드
    private lateinit var locationSource: FusedLocationSource
    val path = PathOverlay()
    lateinit var myNaverMap: NaverMap
    var destinationMarker = Marker()
    lateinit var dtInfo : PlaceInfo
    lateinit var cafeInfo : PlaceInfo
    var dayIndex = 0
    var courseIndex = 0
    var cafeMarker = Marker()
    var myLocationX = 0.0
    var myLocationY = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.post_bring_detail,container,false)
        val bringDrawerLayout = view.findViewById<DrawerLayout>(R.id.bring_drawer_layout)
        val bringDrawerView = view.findViewById<LinearLayout>(R.id.bring_drawer_view)

        val mActivity = activity as Activity
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetPlaceInfo::class.java)
        travelInfo = mActivity.SelectedBringPostInfo
        dayIndex = mActivity.SelectedBringPostInfo.dayindex.toInt()
        courseIndex = mActivity.SelectedBringPostInfo.courseindex.toInt()
        mapView = view.findViewById(R.id.bringMap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        locationSource =
            FusedLocationSource(activity as Activity, LOCATION_PERMISSION_REQUEST_CODE)
        divideCourse()
        searchPlaceData()
        // 메인스레드와 서버통신을 위해 생성한 스레드의 동기를 맞추기위해 메인스레드를 sleep 통해 잠시 정지시켜준다.
        try {
            Thread.sleep(150)
        }catch (e:Exception){
            e.printStackTrace()
        }
        val detailDestination = view.findViewById<TextView>(R.id.bringDetailDestination)
        val drawerList = view.findViewById<RecyclerView>(R.id.bring_drawer_List)
        val store1 = view.findViewById<Button>(R.id.convenienceStore)
        val store2 = view.findViewById<Button>(R.id.caFe)
        val bringTool = view.findViewById<BoomMenuButton>(R.id.bringTool)

        bringTool.piecePlaceEnum = PiecePlaceEnum.DOT_3_4
        bringTool.buttonPlaceEnum = ButtonPlaceEnum.SC_3_4
        dtInfo = coursePlaceList[dayIndex][courseIndex]
        detailDestination.text = dtInfo.name
        // 카페 버튼 클릭시 동작
        store2.setOnClickListener {
            if(myLocationX == 0.0 && myLocationY == 0.0)
            {
                Toast.makeText(view.context,"위치갱신 이후에 진행해 주세요!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val funcName = "SearchPlace"
                val typeName = "Cafe"
                val strName = myLocationX.toString() + ']' + myLocationY.toString()
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("실패", t.toString())
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("성공", "입출력 성공")
                            var returndata = response.body()
                            if (returndata != null) {
                                val drawerAdapter = PostBringDetailAdapter(view.context,returndata!!)
                                drawerList.adapter = drawerAdapter
                                drawerList.layoutManager = LinearLayoutManager(view.context)
                                drawerAdapter.setItemClickListener(object : PostBringDetailAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {
                                        cafeInfo = returndata[position]
                                        cafeMarker.position = LatLng(returndata[position].PosY.toDouble(),returndata[position].PosX.toDouble())
                                        cafeMarker.map = myNaverMap
                                        cafeMarker.icon = OverlayImage.fromResource(R.drawable.cafe)
                                        cafeMarker.icon = MarkerIcons.BLACK
                                        cafeMarker.iconTintColor = Color.YELLOW
                                        cafeMarker.captionText = "카페"
                                        cafeMarker.setCaptionAligns(Align.Top)
                                        cafeMarker.captionTextSize = 16f
                                        bringDrawerLayout.closeDrawer(bringDrawerView)

                                    }
                                })

                                bringDrawerLayout.openDrawer(bringDrawerView)

                            }
                        }
                    })
            }

        }
        // Tool로 버튼 모음
        for(i in 0..bringTool.piecePlaceEnum.pieceNumber()){
            // 경로탐색
            if(i == 0)
            {
                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    drawcourse()
                }).normalText("경로탐색").normalImageRes(R.drawable.content_write)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
            // 여행지 정보 불러오기
            if(i == 1)
            {
                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    var tmp = ImageInfoForLoad(dtInfo.num, "PlaceImages")
                    var serviceImage = PublicRetrofit.retrofit.create(LoadImage::class.java)
                    lateinit var mbitmap : Bitmap
                    serviceImage.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
                        override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                            Log.d("ImgLoadingObj", "이미지 출력 성공")
                            var returndata = response.body()
                            var byteArry = returndata?.data
                            var tbitmap = byteArry?.let { it1 -> BitmapFactory.decodeByteArray( byteArry, 0, it1.size) }
                            mbitmap = tbitmap!!
                            changeFragAndInitSelectedPlace(12, dtInfo, mbitmap)
                        }
                        override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                            Log.d("ImgLoadingObj", "이미지 출력 실패")
                            mbitmap = BitmapFactory.decodeResource(view.context.getResources(), R.drawable.image);
                            t.printStackTrace()
                            changeFragAndInitSelectedPlace(12, dtInfo, mbitmap)
                        }
                    })

                }).normalText("목적지 정보").normalImageRes(R.drawable.content_route)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
            // 여행 완료하는 동작
            if(i == 2)
            {
                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    if(courseList[dayIndex][courseIndex + 1] == ""){
                        if(courseList[dayIndex+1][0] == "")
                        {
                            detailDestination.text = "여행끝"
                        }
                        else{
                            dayIndex += 1
                            courseIndex = 0
                            dtInfo = coursePlaceList[dayIndex][courseIndex]
                            destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
                            detailDestination.text = dtInfo.name


                        }
                    }
                    else{
                        courseIndex += 1
                        dtInfo = coursePlaceList[dayIndex][courseIndex]
                        destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
                        detailDestination.text = dtInfo.name

                    }

                }).normalText("여행완료").normalImageRes(R.drawable.content_route)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
        }
        destinationMarker.setOnClickListener {
            dtInfo = coursePlaceList[dayIndex][courseIndex]
            detailDestination.text = dtInfo.name
            true
        }
        cafeMarker.setOnClickListener{
            dtInfo = cafeInfo
            detailDestination.text = cafeInfo.name
            true
        }
        return view
    }
    // GPS 권한 요청을 위한 함수
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                myNaverMap.locationTrackingMode = LocationTrackingMode.None
            }
            else{
                myNaverMap.locationTrackingMode = LocationTrackingMode.NoFollow
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onMapReady(naverMap: NaverMap){
        myNaverMap = naverMap
        myNaverMap.locationSource = locationSource
        ActivityCompat.requestPermissions(activity as Activity,permissions,
            LOCATION_PERMISSION_REQUEST_CODE)
        val uiSettings = naverMap.uiSettings
        uiSettings.setLocationButtonEnabled(true)
        naverMap.addOnLocationChangeListener {
            myLocationX = it.latitude
            myLocationY = it.longitude
            Log.d("Log",myLocationX.toString() + " " + myLocationY.toString())
        }
        destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
        destinationMarker.map = myNaverMap
        destinationMarker.icon = MarkerIcons.BLACK
        destinationMarker.iconTintColor = Color.RED
        destinationMarker.captionText = "여행목적지"
        destinationMarker.captionTextSize = 16f
        destinationMarker.setCaptionAligns(Align.Top)




    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }




    // 코스를 배열화 시키는 작업을 거치기위한 변수
    lateinit var courseList : Array<Array<String>>
    var coursePlaceList = ArrayList<ArrayList<PlaceInfo>>()
    private val maxDay = 30
    private val maxString = 60
    var dayCount = 0
    // 코스를 배열화 시킨다.
    private fun divideCourse(){
        val course = travelInfo.course
        courseList = Array(maxDay,{Array(maxString,{""})})
        var stringTemp : String = ""
        var courseCount = 0

        for (i in 0 .. course.length - 1){
            if(course[i] != '/' && course[i] != ','){
                stringTemp += course[i]
            }
            else{
                courseList[dayCount][courseCount] += stringTemp
                courseCount += 1
                stringTemp = ""
                if(course[i] == '/')
                {
                    dayCount += 1
                    courseCount = 0
                }

            }
            if(i == course.length - 1){
                courseList[dayCount][courseCount] += stringTemp
                dayCount += 1
            }
        }
    }

    private fun searchPlaceData(){
        val service = (activity as Activity).retrofit.create(GetPlaceInfo::class.java)
        for(i in 0 until dayCount){
            var tempQuery = ""
            coursePlaceList.add(ArrayList<PlaceInfo>())
            for(j in 0 .. maxString - 1){
                if(courseList[i][j] == ""){
                    break
                }
                tempQuery += "num = " + courseList[i][j]
                if(courseList[i][j+1] != ""){
                    tempQuery += " OR "
                }

            }

            Thread(object: Runnable{
                override fun run() {
                    try {
                        val returnData = service.getplaceinfo("SearchPlace","ByIds",tempQuery).execute().body()
                        if(returnData != null){
                            for(w in 0 until maxString){
                                if(courseList[i][w] == ""){
                                    break
                                }
                                for(x in 0 until returnData.size){
                                    if(returnData[x].num == courseList[i][w]){
                                        coursePlaceList[i].add(returnData[x])
                                        Log.d("Log","성공!" + coursePlaceList.toString())
                                    }
                                }
                            }
                        }
                    }catch (e:IOException){
                        Log.d("Log","실패")
                        e.printStackTrace()
                    }
                }
            }).start()

        }
    }
    private fun drawcourse(){
        val APIKEY_ID = "oa30nfihnk"
        val APIKEY = "C6Ok2K9CArC1Qa56Xzfzrid1piKSSNFXpHiCHHRl"
        //레트로핏 객체 생성
        val retrofit = Retrofit.Builder().
        baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/").
        addConverterFactory(GsonConverterFactory.create()).
        build()
        val courseStart : String = "${myLocationY.toString()}, ${myLocationX.toString()}"
        val courseEnd: String = "${dtInfo.PosX}, ${dtInfo.PosY}"
        Log.d("Log",courseStart +" "+ courseEnd)
        val api = retrofit.create(NaverAPI::class.java)
        //근처에서 길찾기
        val callgetPath = api.getPath(APIKEY_ID, APIKEY,courseStart, courseEnd)
        callgetPath.enqueue(object : Callback<ResultPath> {
            override fun onFailure(call: Call<ResultPath>, t: Throwable) {
                Log.d("실패", t.toString())
            }
            override fun onResponse(
                call: Call<ResultPath>,
                response: Response<ResultPath>
            ) {
                val path_cords_list = response.body()?.route?.traoptimal
                //경로 그리기 응답바디가 List<List<Double>> 이라서 2중 for문 썼음

                //MutableList에 add 기능 쓰기 위해 더미 원소 하나 넣어둠
                val path_container : MutableList<LatLng>? = mutableListOf(LatLng(0.1,0.1))
                for(path_cords in path_cords_list!!){
                    for(path_cords_xy in path_cords?.path){
                        //구한 경로를 하나씩 path_container에 추가해줌
                        path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                    }
                }
                //더미원소 드랍후 path.coords에 path들을 넣어줌.
                path.coords = path_container?.drop(1)!!
                path.color = Color.RED
                path.map = myNaverMap

                //경로 시작점으로 화면 이동
                if(path.coords != null) {
                    val cameraUpdate = CameraUpdate.scrollTo(path.coords[0]!!)
                        .animate(CameraAnimation.Fly, 3000)
                    myNaverMap!!.moveCamera(cameraUpdate)
                    Toast.makeText(activity as Activity, "경로 안내가 시작됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    fun changeFragAndInitSelectedPlace(index : Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
        (activity as Activity).SelectedPlace = placeinfo
        if(bitmap != null) (activity as Activity).SelectedBitmap = bitmap
        (activity as Activity)!!.changeFragment(index)
    }



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}