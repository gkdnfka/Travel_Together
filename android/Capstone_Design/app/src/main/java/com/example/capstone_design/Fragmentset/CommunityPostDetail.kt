package com.example.capstone_design
import android.content.ClipData
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import com.example.capstone_design.Activityset.Activity

import com.example.capstone_design.Adapterset.CommunityPostDetailCommentAdaptor
import com.example.capstone_design.Adapterset.CommunityPostDetailDayAdaptor
import com.example.capstone_design.Adapterset.CommunityPostDetailPlaceAdaptor
import com.example.capstone_design.Dataset.CommentInfo

import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Interfaceset.BringPost

import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Fragmentset.*

import com.example.capstone_design.databinding.AlertdialogEdittextBinding

import com.google.android.material.bottomnavigation.BottomNavigationView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CommunityPostDetail : Fragment()
{
    // @솔빈 2022-01-29 (토)
    // selectedPostInfo -> Activity 내에 존재하는, 자세히 보기가 적용되는 게시글의 데이터를 담고있는 PostInfo 클래스 객체
    lateinit var selectedPostInfo : PostInfo


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_fragment, container, false)
        var mActivity = (activity as Activity)
        selectedPostInfo = mActivity.SelectedPostInfo


        // @솔빈 2022-01-29 (토)
        // 1. parseCourseList() -> 경로 문자열을 파싱해서 2차원 여행지 리스트(courseList[일차][여행지]로 변환
        // 2. loadPosList()     -> courseList[일차][여행지]의 원소인 여행지 번호를 통해 DB 에서 좌표값 불러와서 placeinfo 클래스를 원소로 가지는 placeinfoList[일차][여행지] 초기화.
        if(mActivity.SelectedPostDone == 0){
            parseCourseList()
            loadPosList()
            mActivity.SelectedPostDone = 1
        }

        // @솔빈 2022-02-05 (토)
        // 문제점 발생할 여지?
        // * 레트로핏을 활용한 http 통신 방식은 비동기 방식인데,
        //   미처 http 응답을 받기 전에 인자로 placeinfoList 가 넘어가는 동작이 수행되면, null이 되지 않을까?
        var selectedTab = mActivity.SelectedTabInPostDetail
        Log.d("Tab 값 = ", selectedTab.toString())
        if(selectedTab == 1) childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, CommentFragment((activity as Activity))).commit()
        else if(selectedTab == 3) childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, CourseFragment(daycount)).commit()
        else childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, ContentFragment(selectedPostInfo.content, selectedPostInfo.title)).commit()

        var bottom : BottomNavigationView = view.findViewById(R.id.post_detail_fragment_menu)
        var BackButton = view.findViewById<ImageView>(R.id.post_detail_exit)

        var AddButton = view.findViewById<ImageView>(R.id.post_detail_content_add)
        val service = (activity as Activity).retrofit.create(BringPost::class.java)

        bottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.post_detail_comment_button -> {
                    mActivity.SelectedTabInPostDetail = 1
                    childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, CommentFragment((activity as Activity))).commit()
                }
                R.id.post_detail_content_button-> {
                    mActivity.SelectedTabInPostDetail = 2
                    childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, ContentFragment(selectedPostInfo.content, selectedPostInfo.title)).commit() }
                R.id.post_detail_course_button-> {
                    mActivity.SelectedTabInPostDetail = 3
                    childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout, CourseFragment(daycount)).commit() }
            }
            Log.d("Tab 값 = ", mActivity.SelectedTabInPostDetail.toString())
            true
        }

        BackButton.setOnClickListener {
            (activity as Activity).changeFragment(3)
        }
        AddButton.setOnClickListener{
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle("여행경로 가져오기")
                .setMessage("해당 여행 경로를 가져오시겠습니까?")
                .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    val builder = AlertDialog.Builder(view.context)
                    val builderView = inflater.inflate(R.layout.alertdialog_edittext,null)

                    val editText = builderView.findViewById<EditText>(R.id.BringPostedit)
                    builder.setTitle("포스트 이름을 입력해주세요")
                        .setView(builderView)
                        .setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                            var funcName = "BringPostPut"
                            var typeName = "default"
                            val PostName = editText.text.toString()
                            service.bringPost(funcName, typeName,PostName,(activity as Activity).USER_CODE,course).enqueue(object:Callback<ArrayList<BringPostInfo>> {
                                override fun onFailure(call : Call<ArrayList<BringPostInfo>>, t : Throwable){
                                    Log.d("실패", t.toString())
                                }
                                override fun onResponse(call: Call<ArrayList<BringPostInfo>>, response: Response<ArrayList<BringPostInfo>>) {
                                    Log.d("성공", "입출력 성공")
                                    Toast.makeText(view.context, "포스트 추가 완료", Toast.LENGTH_SHORT).show()
                                }
                            })
                        })
                        .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                    builder.show()
                })
                .setPositiveButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(view.context, "취소했습니다", Toast.LENGTH_SHORT).show()
                })
            builder.show()
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
    var daycount : Int = 0

    // @솔빈  2022-01-29 (토)
    // ---함수 설명---
    // ParseCourseList -> 자세히 보기 기능이 선택된 게시글의 이동경로 문자열을 파싱하는 함수.
    // 결과적으로 courseList 에 (행 : 날짜, 열 : 여행지 번호)로 값이 초기화됨.
    // 문자열 형식 example) 123,23,125,42/1,2,3,4,5
    // -> 1일차에 123번 -> 23번 -> 125번 -> 42번 순으로 여행지 방문
    // -> 2일차에 1번 -> 2번 -> 3번 -> 4번 -> 5번 순으로 여행지 방문

    private fun parseCourseList() {
        var mActivity = (activity as Activity)
        course = selectedPostInfo.course
        mActivity.SelectedPostPlaceList = Array(maxDayLength, { Array(maxPlaceLength, {""}) })

        var tmpStr = ""
        var courseCount = 0
        daycount = 0

        // 문자열 파싱 로직
        for (i in 0 until course.length){
            if(course[i] != '/' && course[i] != ',') tmpStr += course[i]
            else{
                mActivity.SelectedPostPlaceList[daycount][courseCount] = tmpStr
                courseCount += 1
                tmpStr = ""
                if(course[i] == '/') {
                    daycount += 1
                    courseCount = 0
                }
            }

            if(i == course.length-1){
                mActivity.SelectedPostPlaceList[daycount][courseCount] = tmpStr
                daycount += 1
            }
        }
    }

    // @솔빈  2022-01-29 (토)
    // ---함수 설명---
    // loadPosList -> 날짜별로 분류된 여행지 코스 2차원 배열인 courseList를 기반으로
    // DB에 쿼리문을 날려서 각각의 여행지의 좌표를 불러오고, placeinfoList에 저장한다.

    private fun loadPosList(){
        var mActivity = (activity as Activity)
        mActivity.SelectedPostPlaceInfoList = ArrayList<ArrayList<PlaceInfo>>()
        val service = (activity as Activity).retrofit.create(GetPlaceInfo::class.java)

        for (i in 0 until daycount){
            mActivity.SelectedPostPlaceInfoList.add(ArrayList<PlaceInfo>())
            var strForQuery = ""
            var j = 0

            while(mActivity.SelectedPostPlaceList[i][j] != ""){
                strForQuery +=  "testnum = " + mActivity.SelectedPostPlaceList[i][j]
                if(j+1 < maxPlaceLength && mActivity.SelectedPostPlaceList[i][j+1] != "" ) strForQuery += " OR "
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
                            if(mActivity.SelectedPostPlaceList[i][q] == "") break
                            for (j in 0 until returndata.size){
                                if(returndata[j].num == mActivity.SelectedPostPlaceList[i][q]){
                                    mActivity.SelectedPostPlaceInfoList[i].add(returndata[j])
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}