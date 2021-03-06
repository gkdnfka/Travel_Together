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
import com.example.capstone_design.Adapterset.PostBringAdapter
import com.example.capstone_design.Adapterset.PostBringDetailAdapter
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor
import com.example.capstone_design.Dataset.*
import com.example.capstone_design.Interfaceset.*
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
    //?????? ????????????
    //??? ????????? ???????????? ??????
    private lateinit var locationSource: FusedLocationSource
    val path = PathOverlay()
    lateinit var myNaverMap: NaverMap
    lateinit var dtInfo : PlaceInfo

    var endFlag = 0
    var dayIndex = 0
    var courseIndex = 0


    var destinationMarker = Marker()


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
        // ?????????????????? ??????????????? ?????? ????????? ???????????? ????????? ??????????????? ?????????????????? sleep ?????? ?????? ??????????????????.
        try {
            Thread.sleep(150)
        }catch (e:Exception){
            e.printStackTrace()
        }
        val detailDestination = view.findViewById<TextView>(R.id.bringDetailDestination)
        val drawerList = view.findViewById<RecyclerView>(R.id.bring_drawer_List)
        val store1 = view.findViewById<Button>(R.id.convenienceStore)
        val store2 = view.findViewById<Button>(R.id.caFe)
        val store3 = view.findViewById<Button>(R.id.sleep)
        val store4 = view.findViewById<Button>(R.id.foodstore)
        val bringTool = view.findViewById<BoomMenuButton>(R.id.bringTool)

        bringTool.piecePlaceEnum = PiecePlaceEnum.DOT_3_4
        bringTool.buttonPlaceEnum = ButtonPlaceEnum.SC_3_4

        if(coursePlaceList[dayIndex].size > courseIndex){
            dtInfo = coursePlaceList[dayIndex][courseIndex]
            detailDestination.text = dtInfo.name
        }
        else {
            detailDestination.text = "?????????"
            endFlag = 1
        }
        // ????????? ?????? ????????? ??????
        store1.setOnClickListener {
            if(myLocationX == 0.0 && myLocationY == 0.0)
            {
                Toast.makeText(view.context,"???????????? ????????? ????????? ?????????!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val funcName = "SearchPlace"
                val typeName = "Convenience"
                val strName = myLocationX.toString() + ']' + myLocationY.toString()
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("??????", t.toString())
                            Toast.makeText(mActivity,"??????????????? ????????????",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("??????", "????????? ??????")
                            var returndata = response.body()
                            if (returndata != null) {
                                val drawerAdapter = PostBringDetailAdapter(view.context,returndata!!)
                                drawerList.adapter = drawerAdapter
                                drawerList.layoutManager = LinearLayoutManager(view.context)
                                drawerAdapter.setItemClickListener(object : PostBringDetailAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {
                                        mActivity.convenienceInfo = returndata[position]
                                        mActivity.convenienceMarker.position = LatLng(returndata[position].PosY.toDouble(),returndata[position].PosX.toDouble())
                                        mActivity.convenienceMarker.map = myNaverMap
                                        mActivity.convenienceMarker.icon = OverlayImage.fromResource(R.drawable.foodstore)
                                        mActivity.convenienceMarker.icon = MarkerIcons.BLACK
                                        mActivity.convenienceMarker.iconTintColor = Color.GREEN
                                        mActivity.convenienceMarker.captionText = "?????????"
                                        mActivity.convenienceMarker.setCaptionAligns(Align.Top)
                                        mActivity.convenienceMarker.captionTextSize = 16f
                                        (activity as Activity).convenienceFlag = 1
                                        bringDrawerLayout.closeDrawer(bringDrawerView)

                                    }
                                })

                                bringDrawerLayout.openDrawer(bringDrawerView)

                            }
                        }
                    })
            }

        }
        // ?????? ?????? ????????? ??????
        store2.setOnClickListener {
            if(myLocationX == 0.0 && myLocationY == 0.0)
            {
                Toast.makeText(view.context,"???????????? ????????? ????????? ?????????!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val funcName = "SearchPlace"
                val typeName = "Cafe"
                val strName = myLocationX.toString() + ']' + myLocationY.toString()
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("??????", t.toString())
                            Toast.makeText(mActivity,"??????????????? ????????????",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("??????", "????????? ??????")
                            var returndata = response.body()
                            if (returndata != null) {
                                val drawerAdapter = PostBringDetailAdapter(view.context,returndata!!)
                                drawerList.adapter = drawerAdapter
                                drawerList.layoutManager = LinearLayoutManager(view.context)
                                drawerAdapter.setItemClickListener(object : PostBringDetailAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {
                                        mActivity. cafeInfo = returndata[position]
                                        mActivity.cafeMarker.position = LatLng(returndata[position].PosY.toDouble(),returndata[position].PosX.toDouble())
                                        mActivity. cafeMarker.map = myNaverMap
                                        mActivity. cafeMarker.icon = OverlayImage.fromResource(R.drawable.cafe)
                                        mActivity.cafeMarker.icon = MarkerIcons.BLACK
                                        mActivity. cafeMarker.iconTintColor = Color.YELLOW
                                        mActivity. cafeMarker.captionText = "??????"
                                        mActivity. cafeMarker.setCaptionAligns(Align.Top)
                                        mActivity. cafeMarker.captionTextSize = 16f
                                        (activity as Activity).cafeFlag = 1
                                        bringDrawerLayout.closeDrawer(bringDrawerView)

                                    }
                                })

                                bringDrawerLayout.openDrawer(bringDrawerView)

                            }
                        }
                    })
            }

        }
        // ?????? ?????? ????????? ??????
        store3.setOnClickListener {
            if(myLocationX == 0.0 && myLocationY == 0.0)
            {
                Toast.makeText(view.context,"???????????? ????????? ????????? ?????????!",Toast.LENGTH_SHORT).show()
            }
            else
            {

                val funcName = "SearchPlace"
                val typeName = "Sleep"
                val strName = myLocationX.toString() + ']' + myLocationY.toString()
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("??????", t.toString())
                            Toast.makeText(mActivity,"??????????????? ????????????",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("??????", "????????? ??????")
                            var returndata = response.body()
                            if (returndata != null) {
                                val drawerAdapter = PostBringDetailAdapter(view.context,returndata!!)
                                drawerList.adapter = drawerAdapter
                                drawerList.layoutManager = LinearLayoutManager(view.context)
                                drawerAdapter.setItemClickListener(object : PostBringDetailAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {
                                        mActivity. sleepInfo = returndata[position]
                                        mActivity. sleepMarker.position = LatLng(returndata[position].PosY.toDouble(),returndata[position].PosX.toDouble())
                                        mActivity.  sleepMarker.map = myNaverMap
                                        mActivity.  sleepMarker.icon = OverlayImage.fromResource(R.drawable.sleep)
                                        mActivity.  sleepMarker.icon = MarkerIcons.BLACK
                                        mActivity.  sleepMarker.iconTintColor = Color.GRAY
                                        mActivity.  sleepMarker.captionText = "??????"
                                        mActivity.  sleepMarker.setCaptionAligns(Align.Top)
                                        mActivity.  sleepMarker.captionTextSize = 16f
                                        (activity as Activity).sleepFlag = 1
                                        bringDrawerLayout.closeDrawer(bringDrawerView)

                                    }
                                })

                                bringDrawerLayout.openDrawer(bringDrawerView)

                            }
                        }
                    })
            }

        }
        // ????????? ?????? ????????? ??????
        store4.setOnClickListener {
            if(myLocationX == 0.0 && myLocationY == 0.0)
            {
                Toast.makeText(view.context,"???????????? ????????? ????????? ?????????!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val funcName = "SearchPlace"
                val typeName = "Restaurant"
                val strName = myLocationX.toString() + ']' + myLocationY.toString()
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("??????", t.toString())
                            Toast.makeText(mActivity,"??????????????? ????????????",Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("??????", "????????? ??????")
                            var returndata = response.body()
                            if (returndata != null) {
                                val drawerAdapter = PostBringDetailAdapter(view.context,returndata!!)
                                drawerList.adapter = drawerAdapter
                                drawerList.layoutManager = LinearLayoutManager(view.context)
                                drawerAdapter.setItemClickListener(object : PostBringDetailAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {
                                        mActivity. restaurantInfo = returndata[position]
                                        mActivity. restaurantMarker.position = LatLng(returndata[position].PosY.toDouble(),returndata[position].PosX.toDouble())
                                        mActivity.  restaurantMarker.map = myNaverMap
                                        mActivity.  restaurantMarker.icon = OverlayImage.fromResource(R.drawable.sleep)
                                        mActivity.  restaurantMarker.icon = MarkerIcons.BLACK
                                        mActivity.  restaurantMarker.iconTintColor = Color.MAGENTA
                                        mActivity.  restaurantMarker.captionText = "?????????"
                                        mActivity.  restaurantMarker.setCaptionAligns(Align.Top)
                                        mActivity.  restaurantMarker.captionTextSize = 16f
                                        (activity as Activity).restaurantFlag = 1
                                        bringDrawerLayout.closeDrawer(bringDrawerView)
                                    }
                                })

                                bringDrawerLayout.openDrawer(bringDrawerView)

                            }
                        }
                    })
            }

        }
        // Tool??? ?????? ??????
        for(i in 0..bringTool.piecePlaceEnum.pieceNumber()){
            // ????????????
            if(i == 0)
            {
                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    if(coursePlaceList[dayIndex].size > courseIndex){
                        if(myLocationX == 0.0 && myLocationY == 0.0)
                        {
                            Toast.makeText(view.context,"???????????? ????????? ????????? ?????????!",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            drawcourse()
                        }

                    }
                    else{
                        Toast.makeText(view.context, "????????? ?????????????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    }
                }).normalText("????????????").normalImageRes(R.drawable.content_route)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
            // ????????? ?????? ????????????
            if(i == 1)
            {
                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    var tmp = ImageInfoForLoad(dtInfo.num, "PlaceImages")
                    var serviceImage = PublicRetrofit.retrofit.create(LoadImage::class.java)
                    lateinit var mbitmap : Bitmap
                    serviceImage.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
                        override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                            Log.d("ImgLoadingObj", "????????? ?????? ??????")
                            var returndata = response.body()
                            var byteArry = returndata?.data
                            var tbitmap = byteArry?.let { it1 -> BitmapFactory.decodeByteArray( byteArry, 0, it1.size) }
                            mbitmap = tbitmap!!
                            changeFragAndInitSelectedPlace(12, dtInfo, mbitmap)
                        }
                        override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                            Log.d("ImgLoadingObj", "????????? ?????? ??????")
                            mbitmap = BitmapFactory.decodeResource(view.context.getResources(), R.drawable.image);
                            t.printStackTrace()
                            changeFragAndInitSelectedPlace(12, dtInfo, mbitmap)
                        }
                    })

                }).normalText("????????? ??????").normalImageRes(R.drawable.infomation)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
            // ?????? ???????????? ??????
            if(i == 2)
            {
                val service2 = retrofit.create(BringPost::class.java)

                val builder = TextOutsideCircleButton.Builder().listener(OnBMClickListener {
                    path.map = null
                    if(courseList[dayIndex][courseIndex + 1] == ""){
                        if(courseList[dayIndex+1][0] == "")
                        {
                            if(detailDestination.text != "?????????")
                            {
                                courseIndex += 1
                                detailDestination.text = "?????????"
                                val funcName = "BringPostPut"
                                val typeName = "update"
                                bringCurrentCount += 1
                                var bringpercent = (bringCurrentCount.toFloat()/bringOverallPlace.toFloat()) * 100
                                Log.d("update1", bringOverallPlace.toString()+" "+bringCurrentCount.toString() +" "+ bringpercent.toString())
                                service2.bringPost(funcName, typeName,travelInfo.postname,travelInfo.usercode,travelInfo.course,travelInfo.seq,Math.round(bringpercent).toString(),dayIndex.toString(),courseIndex.toString(),bringCurrentCount.toString()).enqueue(object:Callback<ArrayList<BringPostInfo>> {
                                    override fun onFailure(call : Call<ArrayList<BringPostInfo>>, t : Throwable){
                                        Log.d("??????", t.toString())
                                    }
                                    override fun onResponse(call: Call<ArrayList<BringPostInfo>>, response: Response<ArrayList<BringPostInfo>>) {
                                        Log.d("??????", "????????? ??????")
                                        Toast.makeText(view.context, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                            else{
                                Toast.makeText(view.context,"????????? ?????????????????????.",Toast.LENGTH_SHORT).show()
                            }

                        }
                        else{
                            dayIndex += 1
                            courseIndex = 0
                            dtInfo = coursePlaceList[dayIndex][courseIndex]
                            destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
                            detailDestination.text = dtInfo.name
                            val funcName = "BringPostPut"
                            val typeName = "update"
                            bringCurrentCount += 1
                            var bringpercent = (bringCurrentCount.toFloat()/bringOverallPlace.toFloat()) * 100
                            Log.d("update1", bringOverallPlace.toString()+" "+bringCurrentCount.toString() +" "+ bringpercent.toString())
                            service2.bringPost(funcName, typeName,travelInfo.postname,travelInfo.usercode,travelInfo.course,travelInfo.seq,Math.round(bringpercent).toString(),dayIndex.toString(),courseIndex.toString(),bringCurrentCount.toString()).enqueue(object:Callback<ArrayList<BringPostInfo>> {
                                override fun onFailure(call : Call<ArrayList<BringPostInfo>>, t : Throwable){
                                    Log.d("??????", t.toString())
                                }
                                override fun onResponse(call: Call<ArrayList<BringPostInfo>>, response: Response<ArrayList<BringPostInfo>>) {
                                    Log.d("??????", "????????? ??????")
                                    Toast.makeText(view.context, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                                }
                            })


                        }
                    }
                    else{
                        courseIndex += 1
                        dtInfo = coursePlaceList[dayIndex][courseIndex]
                        destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
                        detailDestination.text = dtInfo.name
                        val funcName = "BringPostPut"
                        val typeName = "update"
                        bringCurrentCount += 1
                        var bringpercent = (bringCurrentCount.toFloat()/bringOverallPlace.toFloat()) * 100
                        Log.d("update1", bringOverallPlace.toString()+" "+bringCurrentCount.toString() +" "+ bringpercent.toString())
                        service2.bringPost(funcName, typeName,travelInfo.postname,travelInfo.usercode,travelInfo.course,travelInfo.seq,Math.round(bringpercent).toString(),dayIndex.toString(),courseIndex.toString(),bringCurrentCount.toString()).enqueue(object:Callback<ArrayList<BringPostInfo>> {
                            override fun onFailure(call : Call<ArrayList<BringPostInfo>>, t : Throwable){
                                Log.d("??????", t.toString())
                            }
                            override fun onResponse(call: Call<ArrayList<BringPostInfo>>, response: Response<ArrayList<BringPostInfo>>) {
                                Log.d("??????", "????????? ??????")
                                Toast.makeText(view.context, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                            }
                        })

                    }


                }).normalText("????????????").normalImageRes(R.drawable.destination)
                    .imagePadding(Rect(30,30,30,30)).textSize(15)
                bringTool.addBuilder(builder)
            }
        }
        destinationMarker.setOnClickListener {
            dtInfo = coursePlaceList[dayIndex][courseIndex]
            detailDestination.text = dtInfo.name
            true
        }
        mActivity.cafeMarker.setOnClickListener{
            dtInfo =mActivity. cafeInfo
            detailDestination.text = mActivity.cafeInfo.name
            true
        }
        mActivity.convenienceMarker.setOnClickListener {
            dtInfo = mActivity.convenienceInfo
            detailDestination.text = mActivity.convenienceInfo.name
            true
        }
        mActivity.sleepMarker.setOnClickListener {
            dtInfo = mActivity.sleepInfo
            detailDestination.text = mActivity.sleepInfo.name
            true
        }

        return view
    }
    // GPS ?????? ????????? ?????? ??????
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // ?????? ?????????
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
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlEnabled = false
        naverMap.addOnLocationChangeListener {
            myLocationX = it.latitude
            myLocationY = it.longitude
            Log.d("Log",myLocationX.toString() + " " + myLocationY.toString())
        }
        if(endFlag != 1){
            destinationMarker.position = LatLng(dtInfo.PosY.toDouble(),dtInfo.PosX.toDouble())
            destinationMarker.map = myNaverMap
            destinationMarker.icon = MarkerIcons.BLACK
            destinationMarker.iconTintColor = Color.RED
            destinationMarker.captionText = "???????????????"
            destinationMarker.captionTextSize = 16f
            destinationMarker.setCaptionAligns(Align.Top)
        }

        if((activity as Activity).convenienceFlag== 1){
            (activity as Activity).convenienceMarker.map = myNaverMap
        }
        if((activity as Activity).cafeFlag == 1){
            (activity as Activity).cafeMarker.map = myNaverMap
        }
        if((activity as Activity).sleepFlag == 1){
            (activity as Activity).sleepMarker.map = myNaverMap
        }
        if((activity as Activity).restaurantFlag == 1){
            (activity as Activity).restaurantMarker.map = myNaverMap
        }





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




    // ????????? ????????? ????????? ????????? ??????????????? ??????
    lateinit var courseList : Array<Array<String>>
    var coursePlaceList = ArrayList<ArrayList<PlaceInfo>>()
    private val maxDay = 30
    private val maxString = 60
    var bringOverallPlace = 0
    var bringCurrentCount = 0
    var dayCount = 0
    // ????????? ????????? ?????????.
    private fun divideCourse(){
        val course = travelInfo.course
        courseList = Array(maxDay,{Array(maxString,{""})})
        var stringTemp : String = ""
        var courseCount = 0
        bringCurrentCount = travelInfo.completeNum.toInt()
        for (i in 0 .. course.length - 1){
            if(course[i] != '/' && course[i] != ','){
                stringTemp += course[i]
            }
            else{
                courseList[dayCount][courseCount] += stringTemp
                bringOverallPlace += 1
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
                bringOverallPlace += 1
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
                                        Log.d("Log","??????!" + coursePlaceList.toString())
                                    }
                                }
                            }
                        }
                    }catch (e:IOException){
                        Log.d("Log","??????")
                        e.printStackTrace()
                    }
                }
            }).start()

        }
    }
    private fun drawcourse(){
        val APIKEY_ID = "oa30nfihnk"
        val APIKEY = "C6Ok2K9CArC1Qa56Xzfzrid1piKSSNFXpHiCHHRl"
        //???????????? ?????? ??????
        val retrofit = Retrofit.Builder().
        baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/").
        addConverterFactory(GsonConverterFactory.create()).
        build()
        val courseStart : String = "${myLocationY.toString()}, ${myLocationX.toString()}"
        val courseEnd: String = "${dtInfo.PosX}, ${dtInfo.PosY}"
        Log.d("Log",courseStart +" "+ courseEnd)
        val api = retrofit.create(NaverAPI::class.java)
        //???????????? ?????????
        val callgetPath = api.getPath(APIKEY_ID, APIKEY,courseStart, courseEnd)
        callgetPath.enqueue(object : Callback<ResultPath> {
            override fun onFailure(call: Call<ResultPath>, t: Throwable) {
                Log.d("??????", t.toString())
            }
            override fun onResponse(
                call: Call<ResultPath>,
                response: Response<ResultPath>
            ) {
                val path_cords_list = response.body()?.route?.traoptimal
                //?????? ????????? ??????????????? List<List<Double>> ????????? 2??? for??? ??????

                //MutableList??? add ?????? ?????? ?????? ?????? ?????? ?????? ?????????
                val path_container : MutableList<LatLng>? = mutableListOf(LatLng(0.1,0.1))
                for(path_cords in path_cords_list!!){
                    for(path_cords_xy in path_cords?.path){
                        //?????? ????????? ????????? path_container??? ????????????
                        path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                    }
                }
                //???????????? ????????? path.coords??? path?????? ?????????.
                path.coords = path_container?.drop(1)!!
                path.color = Color.RED
                path.map = myNaverMap

                //?????? ??????????????? ?????? ??????
                if(path.coords != null) {
                    val cameraUpdate = CameraUpdate.scrollTo(path.coords[0]!!)
                        .animate(CameraAnimation.Fly, 3000)
                    myNaverMap!!.moveCamera(cameraUpdate)
                    Toast.makeText(activity as Activity, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
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