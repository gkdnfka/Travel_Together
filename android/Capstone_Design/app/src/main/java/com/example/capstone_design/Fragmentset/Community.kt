package com.example.capstone_design.Fragmentset
import android.graphics.Rect
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Button
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.GetPostInfo
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import android.text.Editable
import com.dinuscxj.refresh.RecyclerRefreshLayout
import com.example.capstone_design.Dataset.TagDictSet
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomButtons.OnBMClickListener
import com.nightonke.boommenu.BoomMenuButton


class Community : Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // http통신을 위한 변수
        val funcName = "SearchPost"
        var typeName = "default"
        // 2022-03-13 정지원 작업
        // 변수들 목록
        var view = inflater.inflate(R.layout.community, container, false)
        val postRefresh = view.findViewById<RecyclerRefreshLayout>(R.id.postListRefresh)
        val listView = view.findViewById<RecyclerView>(R.id.postListView)
        val communityTool = view.findViewById<BoomMenuButton>(R.id.communityTool)
        val SearchText = view.findViewById<EditText>(R.id.communitySearch)
        val removeText = view.findViewById<ImageButton>(R.id.communitySearchRemove)
        val post_type = view.findViewById<ToggleButton>(R.id.CommunityPostType)
        val mActivity = activity as Activity
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetPostInfo::class.java)
        val user_code = mActivity.USER_CODE


        mActivity.SelectedDayInPostDetail = 0
        mActivity.SelectedTabInPostDetail = 2

        // 2022-03-12 정지원 작업
        // 검색창의 포커스가 벗어나는 경우 remove 버튼을 비활성화, 포커스가 잡히는 경우 활성화
        SearchText.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(hasFocus){
                    removeText.visibility = View.VISIBLE
                }
                else
                {
                    removeText.visibility = View.INVISIBLE
                }
            }
        })
        // 2022-03-13 정지원 작업
        // 프래그먼트 전환시 한번 그냥 탐색을해 모든 결과물 리스트를 불러온다
        service.getpostinfo(funcName, typeName,"").enqueue(object: Callback<ArrayList<PostInfo>> {
            override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                Log.d("성공", "DB 입출력 성공")
                var returndata = response.body()
                if(returndata != null){
                    // @솔빈 : 리스너 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
                    var Implemented = object : SetSeletedPostInfo {
                        override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                            (activity as Activity)!!.SelectedPostDone = 0
                            (activity as Activity)!!.SetSelectedPostInfo(element)
                            (activity as Activity)!!.changeFragment(11)
                        }
                    }
                    val communityadaptor = CommunityAdaptor(view.context, returndata!!, Implemented, user_code)
                    listView.setHasFixedSize(true)
                    listView.adapter = communityadaptor
                    listView.layoutManager = LinearLayoutManager(view.context)
                }
            }
        })
        // 2022-03-13 정지원 작업
        // 엔터키 입력시 검색시작
        SearchText.setOnKeyListener { v, keycode, event ->

            if(event.action == KeyEvent.ACTION_DOWN && keycode == KEYCODE_ENTER)
            {
                val Searchtext_tmp :String = SearchText.text.toString()
                SearchText.text = null
                // 2022-03-13 정지원 작업
                // 토글버튼에 따른 분기 true = 게시물이름 false = 유저이름
                if(post_type.isChecked)
                {
                    typeName = "ByPostName"
                }
                else
                {
                    typeName = "ByUserName"
                }
                // @솔빈 2022-01-26 (수)
                // 레트로핏의 서비스 객체를 활용해서, 인터페이스의 함수인 getpostinfo를 호출.
                // enqueue 메소드의 인자로 콜백 함수 하나를 선언해서 삽입
                // 해당 함수는 onResponse 함수와 onFailure 함수를 오버라이드 해야함
                // onResponse 에 응답 성공시 response 객체가 인자로 전달됨.
                // onResponse 내의 response.body()를 통해 ArrayList<PostInfo> 최종적으로 형태로 데이터 전달받을 수 있음!

                service.getpostinfo(funcName, typeName,Searchtext_tmp).enqueue(object: Callback<ArrayList<PostInfo>> {
                    override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                        Log.d("실패", t.toString())
                    }

                    override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                        Log.d("성공", "DB 입출력 성공")
                        var returndata = response.body()
                        if(returndata != null){
                            // @솔빈 : 리스너 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
                            var Implemented = object : SetSeletedPostInfo {
                                override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                                    (activity as Activity)!!.SelectedPostDone = 0
                                    (activity as Activity)!!.SetSelectedPostInfo(element)
                                    (activity as Activity)!!.changeFragment(11)
                                }
                            }
                            val communityadaptor = CommunityAdaptor(view.context, returndata!!, Implemented, user_code)
                            listView.setHasFixedSize(true)
                            listView.adapter = communityadaptor
                            listView.layoutManager = LinearLayoutManager(view.context)
                        }
                    }
                })
                // 2022-03-13 정지원 작업
                // 검색 종료시 검색창의 text를 null로 비우고 검색창의 포커스를 해제한다

                SearchText.clearFocus()
            }
            true
        }

        // 2022-03-13 정지원 작업
        // remove 버튼 입력시 edittext 내용 초기화
        removeText.setOnClickListener {
            SearchText.text = null
        }
        // 2022-03-13 정지원 작업
        // refresh시 전체 리스트를 다시한번 서버에서 가져온다
        postRefresh.setOnRefreshListener(RecyclerRefreshLayout.OnRefreshListener {
            service.getpostinfo(funcName, typeName,"").enqueue(object: Callback<ArrayList<PostInfo>> {
                override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    if(returndata != null){
                        // @솔빈 : 리스너 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
                        var Implemented = object : SetSeletedPostInfo {
                            override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                                (activity as Activity)!!.SelectedPostDone = 0
                                (activity as Activity)!!.SetSelectedPostInfo(element)
                                (activity as Activity)!!.changeFragment(11)
                            }
                        }
                        val communityadaptor = CommunityAdaptor(view.context, returndata!!, Implemented, user_code)
                        listView.setHasFixedSize(true)
                        listView.adapter = communityadaptor
                        listView.layoutManager = LinearLayoutManager(view.context)
                        postRefresh.setRefreshing(false)
                    }
                }
            })

        })
        // 2022-03-13 정지원 작업
        // boomMenu버튼으로 통합하여 기능관리
        // normalText에 이름넣으면 메뉴의 이름 바뀌고 normalImageRes를 통하여 아이콘 이미지 수정가능
        for(i in 0..communityTool.piecePlaceEnum.pieceNumber()){
            if(i == 0)
            {
                mActivity.isSelectedDetailTagClicked = ArrayList<ArrayList<Int>>()
                mActivity.tagDictList = ArrayList<ArrayList<TagDictSet>>()
                val builder = HamButton.Builder().listener(OnBMClickListener {
                    mActivity.changeFragment(6)
                    mActivity.reset_value()
                }).normalText("게시글 작성").normalImageRes(R.drawable.content_write)
                    .imagePadding(Rect(30,30,30,30)).textSize(17)
                communityTool.addBuilder(builder)
            }
            if(i == 1)
            {
                val builder = HamButton.Builder().listener(OnBMClickListener {
                    mActivity.changeFragment(15)
                    mActivity.reset_value()
                }).normalText("취향 태그 수정하기ㅁ").normalImageRes(R.drawable.content_route)
                    .imagePadding(Rect(30,30,30,30)).textSize(17)
                communityTool.addBuilder(builder)
            }
        }
        return view
    }
}

