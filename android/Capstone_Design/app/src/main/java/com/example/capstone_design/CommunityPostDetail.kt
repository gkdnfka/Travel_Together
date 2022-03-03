package com.example.capstone_design
import android.content.Context
import android.graphics.text.TextRunShaper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.annotations.SerializedName

import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.SimpleDateFormat
import java.time.LocalDate



interface CommentModifyInterface{
    fun DeleteComment(CommentNum: String)
    fun EditComment(newContent : String, commentNum : String)
}

// @솔빈 2022-02-22 (목)
// CommentFragment -> 게시글에 달린 댓글을 출력하는 프래그먼트
class CommentFragment() : Fragment()
{
    var commentInfoList = ArrayList<CommentInfo>()
    val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // @ 솔빈 2022-02-26 토
    // serviceForComment -> 댓글에 대한 DB 입출력을 위한 모든 함수들을 담고있는 인터페이스 객체
    val serviceForComment = retrofit.create(CommentInterface::class.java)

    lateinit var commentRecycler : RecyclerView
    lateinit var edittxt : EditText
    var editFlag = 0
    var editCommentNum = "0"

    // @솔빈 2022-02-26 토
    // DB 통신 실패 성공 여부 판단을 위한 dataclass
    data class Success(
            @SerializedName("number")
            var number : String
    )

    // @ 솔빈 2022-02-26 토
    // service(DB 입출력) 을 위한 인터페이스 선언
    // GetCommentInfo -> 댓글 목록 요청(인자 => 게시글 번호)
    // WriteComment -> 댓글 추가 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)
    // DeleteComment -> 댓글 삭제 요청(인자 => 댓글 번호)
    // EditComment   -> 댓글 수정 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)

    interface CommentInterface{
        @GET("func={func}/number={number}")
        fun getCommentInfo(@Path("func") func : String, @Path("number") number : String): Call<ArrayList<CommentInfo>>

        @GET("func={func}/NoticeNum={NoticeNum}/UserCode={UserCode}/UserName={UserName}/Dates={Dates}/Content={Content}")
        fun writecomment(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String, @Path("UserCode") UserCode : String, @Path("UserName") UserName : String,@Path("Dates") Dates : String, @Path("Content") Content : String): Call<Success>

        @GET("func={func}/CommentNum={CommentNum}")
        fun deletecomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String): Call<Success>

        @GET("func={func}/CommentNum={CommentNum}/Dates={Dates}/Content={Content}")
        fun editcomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String, @Path("Dates") Dates : String, @Path("Content") Content : String): Call<Success>
    }


    // @솔빈 2022-02-28 일
    // deleteCommentInterface -> 댓글 삭제를 위한 인터페이스 객체 선언
    // editCommentInterface   -> 댓글 수정을 위한 인터페이스 객체 선언
    var commentModifyInterface = object : CommentModifyInterface {
        override fun DeleteComment(CommentNum : String) {
            serviceForComment.deletecomment("DeleteComment", CommentNum).enqueue(object: Callback<Success> {
                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    LoadCommentList()
                }
            })
        }

        val currentTime : Long = System.currentTimeMillis()
        val timeformat = SimpleDateFormat("yyyy-MM-dd")
        var nowtime = timeformat.format(currentTime).toString()

        override fun EditComment(newContent : String, commentNum : String) {
            editFlag = 1
            edittxt.setText(newContent)
            editCommentNum = commentNum
        }
    }

    // @솔빈 2022-02-25 금
    // 해당 게시글에 속하는 댓글들을 데이터베이스에서 불러와서 commentInfoList에 저장하고
    // adaptor을 붙여주는 동작을 수행하는 함수
    fun LoadCommentList(){
        serviceForComment.getCommentInfo("SearchComment", (activity as Activity).SelectedPostInfo.number.toString()).enqueue(object: Callback<ArrayList<CommentInfo>> {
            override fun onFailure(call: Call<ArrayList<CommentInfo>>, t: Throwable) {
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<CommentInfo>>, response: Response<ArrayList<CommentInfo>>) {
                Log.d("성공", "DB 입출력 성공")
                var returndata = response.body()
                commentInfoList = returndata!!
                commentRecycler.adapter = CommunityPostDetailCommentAdaptor(commentInfoList, (activity as Activity), (activity as Activity).USER_CODE, commentModifyInterface)
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_comment_item, container, false)
        var btn = view.findViewById<Button>(R.id.post_detail_comment_add_button)
        edittxt = view.findViewById<EditText>(R.id.post_detail_comment_edit)

        // @솔빈 2022-02-25 금
        // commentRecyler 초기화 한 다음
        // LoadCommentList() 호출
        commentRecycler = view.findViewById<RecyclerView>(R.id.post_detail_comment_recycler)
        LoadCommentList()

        btn.setOnClickListener {
            val currentTime : Long = System.currentTimeMillis()
            val timeformat = SimpleDateFormat("yyyy-MM-dd")
            var nowtime = timeformat.format(currentTime).toString()

            if(editFlag == 0){
                // @솔빈 2022-02-25 금
                // 댓글 작성 DB 입출력 로직 구현
                serviceForComment.writecomment("WriteComment", (activity as Activity).SelectedPostInfo.number, (activity as Activity).USER_CODE, (activity as Activity).USER_NAME, nowtime, edittxt.text.toString()).enqueue(object: Callback<Success> {
                    override fun onFailure(call : Call<Success>, t : Throwable){
                        Toast.makeText((activity as Activity),"네트워크 문제로 댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        var returndata = response.body()
                        Toast.makeText((activity as Activity),"댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        LoadCommentList()
                    }
                })
            }
            else{
                serviceForComment.editcomment("EditComment", editCommentNum,  nowtime, edittxt.text.toString()).enqueue(object : Callback<Success> {
                    override fun onFailure(call: Call<Success>, t: Throwable) {
                        Log.d("실패", t.toString())
                        Toast.makeText((activity as Activity),"네트워크 문제로 댓글이 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        editFlag = 0
                    }

                    override fun onResponse(call: Call<Success>, response: Response<Success>) {
                        Log.d("성공", "DB 입출력 성공")
                        Toast.makeText((activity as Activity),"댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        var returndata = response.body()
                        LoadCommentList()
                        editFlag = 0
                    }
                })
            }
        }
        return view
    }
}

// @솔빈 2022-01-27 (목)
// ContentFragment -> 게시글의 본문을 출력하는 프래그먼트
class ContentFragment(content : String, title : String) : Fragment()
{
    var mcontent = content
    var mtitle = title
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_content_item, container, false)
        var contentTextView = view.findViewById<TextView>(R.id.post_detail_content_content_text)
        var titleTextView = view.findViewById<TextView>(R.id.post_detail_content_title_text)
        if(mcontent != null) contentTextView.text = mcontent
        if(mtitle != null) titleTextView.text = mtitle
        return view
    }
}

// @솔빈 2022-01-27 (목)
// CourseFragment -> 경로를 출력하는 프래그먼트
class CourseFragment(courseList : Array<Array<String>>, placeinfoList : Array<ArrayList<PlaceInfo>>, dayCount : Int) : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener
{
    // mView -> 구글맵 객체
    lateinit var mView : MapView
    var courseList = courseList
    var placeinfoList = placeinfoList
    var selectedDay : Int = 0
    var dayCount = dayCount

    lateinit var myGooglemap:GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_course_item, container, false)
        mView = view.findViewById<MapView>(R.id.post_detail_course_map)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        var courseRecycle = view.findViewById<RecyclerView>(R.id.post_detail_course_place_RecyclerView)
        courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity))

        // @솔빈 2022-1-25 (화)
        // dayArray -> 날짜 배열
        // Implemented -> google map view 객체를 선택된 날짜에 따라 업데이트하고,
        // 경로 여행지 어답터를 업데이트하기 위한 인터페이스 객체
        var dayArray = ArrayList<Int>()
        for (i in 0 until dayCount) dayArray.add(i+1)
        var Implemented = object : changeDay {
            override fun changeday(day: Int) {
                selectedDay = day
                courseRecycle.adapter = CommunityPostDetailPlaceAdaptor(placeinfoList[selectedDay], (activity as Activity))
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

        val polyline = googleMap.addPolyline(posLatLng)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)

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

        val polyline = myGooglemap.addPolyline(posLatLng)
        myGooglemap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placeinfoList[selectedDay][0].PosY.toDouble(), placeinfoList[selectedDay][0].PosX.toDouble()), 10f))
        myGooglemap.setOnPolylineClickListener(this)
        myGooglemap.setOnPolygonClickListener(this)
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
        // 1. parseCourseList() -> 경로 문자열을 파싱해서 2차원 여행지 리스트(courseList[일차][여행지]로 변환
        // 2. loadPosList()     -> courseList[일차][여행지]의 원소인 여행지 번호를 통해 DB 에서 좌표값 불러와서 placeinfo 클래스를 원소로 가지는 placeinfoList[일차][여행지] 초기화.

        parseCourseList()
        loadPosList()


        // @솔빈 2022-02-05 (토)
        // 문제점 발생할 여지?
        // * 레트로핏을 활용한 http 통신 방식은 비동기 방식인데,
        //   미처 http 응답을 받기 전에 인자로 placeinfoList 가 넘어가는 동작이 수행되면, null이 되지 않을까?


        childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,Main()).commit()
        var bottom : BottomNavigationView = view.findViewById(R.id.post_detail_fragment_menu)
        var BackButton = view.findViewById<ImageView>(R.id.post_detail_exit)

        bottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.post_detail_comment_button ->{ childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,CommentFragment()).commit() }
                R.id.post_detail_content_button-> { childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,ContentFragment(selectedPostInfo.content, selectedPostInfo.title)).commit() }
                R.id.post_detail_course_button-> { childFragmentManager.beginTransaction().replace(R.id.post_detail_FrameLayout,CourseFragment(courseList, placeinfoList, daycount)).commit() }
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
        daycount = 0

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
    // DB에 쿼리문을 날려서 각각의 여행지의 좌표를 불러오고, placeinfoList에 저장한다.

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