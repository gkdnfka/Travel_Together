package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception


class PostBringDetail : Fragment() {
    lateinit var travelInfo : BringPostInfo
    var dayIndex = 0
    var courseIndex = 0


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.post_bring_detail,container,false)
        val mActivity = activity as Activity
        travelInfo = mActivity.SelectedBringPostInfo
        dayIndex = mActivity.SelectedBringPostInfo.dayindex.toInt()
        courseIndex = mActivity.SelectedBringPostInfo.courseindex.toInt()
        divideCourse()
        searchPlaceData()
        Log.d("Log","!!!!!!")
        // 메인스레드와 서버통신을 위해 생성한 스레드의 동기를 맞추기위해 메인스레드를 sleep 통해 잠시 정지시켜준다.
        try {
            Thread.sleep(150)
        }catch (e:Exception){
            e.printStackTrace()
        }
        val detailPlaceName = view.findViewById<TextView>(R.id.bringDetailPlaceName)
        val detailButton = view.findViewById<Button>(R.id.bringDetailButton)
        detailPlaceName.text = (dayIndex+1).toString() + "일차 " + coursePlaceList[dayIndex][courseIndex].name
        detailButton.setOnClickListener {
            if(courseList[dayIndex][courseIndex + 1] == ""){
                if(courseList[dayIndex+1][0] == "")
                {
                    detailPlaceName.text = "여행끝"
                }
                else{
                    dayIndex += 1
                    courseIndex = 0
                    detailPlaceName.text = (dayIndex+1).toString() + "일차 " + coursePlaceList[dayIndex][courseIndex].name
                }
            }
            else{
                courseIndex += 1
                detailPlaceName.text =(dayIndex+1).toString() + "일차 " + coursePlaceList[dayIndex][courseIndex].name
            }
        }







        return view
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
                 tempQuery += "testnum = " + courseList[i][j]
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
}