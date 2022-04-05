package com.example.capstone_design.Activityset
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.capstone_design.*
import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PlanInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Fragmentset.*
import com.example.capstone_design.Interfaceset.BringPost
import com.example.capstone_design.Interfaceset.SetSelectedBringPost
import com.example.capstone_design.Util.FavoriteAddManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Activity : AppCompatActivity() {
    // @솔빈 2022-1-29 토
    // SelectedPostInfo : 프래그먼트 화면 전환시(게시글 자세히 보기), 넘겨 받아야할 게시글 정보를 담는 변수
    // SelectedPlaceList : 최단 경로 찾기에서 사용되는, 선택한 여행지들을 담는 리스트
    // SelectedBitmap : 선택된 여행지의 이미지를 담고 있는 비트맵 객체
    // SelectedPlace  : 선택된 여행지에 대한 정보를 담고있는 객체
    lateinit var SelectedBitmap : Bitmap
    lateinit var SelectedPlace : PlaceInfo
    lateinit var SelectedPostInfo : PostInfo
    lateinit var SelectedBringPostInfo : BringPostInfo
    var SelectedPlaceList = ArrayList<PlaceInfo>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 로그인 정보 불러오기
        InitLoginInfo()

        ReplaceFragment("Main", Main(), R.id.MainFrameLayout)

        supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, Main()).commit()

        var bottom: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottom.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.MainButton -> {
                    changeFragment(1)
                }
                R.id.RecommendButton -> {
                    changeFragment(2)
                }
                R.id.CommunityButton -> {
                    changeFragment(3)
                }
                R.id.PathButton -> {
                    changeFragment(4)
                }
                R.id.TestSearch -> {
                    changeFragment(5)
                }
            }
            true
        }
    }
    // 변수 현황 (2022-03-03) 업데이트시 작업자 날짜 기록
    var USER_ID = ""
    var USER_PASSWORD = ""
    var USER_NAME = ""
    var USER_CODE = ""
    var postName = ""
    var postContent = ""
    var day = 1
    var postplanlist: MutableList<PlanInfo> = ArrayList()
    var place_list : MutableList<PlaceInfo> = ArrayList()


    // @솔빈 2022-02-25 금
    // InitLoginInfo() -> SharedPreference에서 로그인 정보를 불러와서 로그인 변수들에 삽입하는 함수

    fun InitLoginInfo(){
        USER_ID = FavoriteAddManager.prefs.getString("ID", "")
        USER_NAME = FavoriteAddManager.prefs.getString("NAME", "")
        USER_PASSWORD = FavoriteAddManager.prefs.getString("PASSWORD", "")
        USER_CODE = FavoriteAddManager.prefs.getString("CODE", "")
    }


    // @솔빈 2022-1-25 수
    // SetSelectedPostInfo -> SelectedPostInfo를 change
    fun SetSelectedPostInfo(element : PostInfo) {
        SelectedPostInfo = element
    }
    // @지원 2022-03-26 토
    // SetSelectedPostInfo -> SelectedPostInfo를 change
    fun SetSelectedBringPost(element : BringPostInfo) {
        SelectedBringPostInfo = element
    }
    // @솔빈 2022-02-08 (화)
    // SelectedPalceList에 원소를 추가하는 함수
    // 이미 SelectedPlaceList에 원소가 존재하면 삭제하고
    // 존재하지 않으면 넣어준다.

    // 1이면 삽입 했음을 알리고
    // 0이면 제거 했음을 알림
    // -> 경로 페이지에서, 여행지를 홀수번 선택하면 선택, 짝수번 선택하면 선택 해제이므로
    fun AddSelectedPlace(newElement : PlaceInfo) : Int{
        var flag = 1
        for (i in 0 until SelectedPlaceList.size){
            if(SelectedPlaceList[i].num == newElement.num){
                SelectedPlaceList.removeAt(i)
                flag = 0
                return 0
            }
        }
        SelectedPlaceList.add(newElement)
        return 1
    }

    // 2022-03-02 수
    // ChangeFragment -> 프래그먼트 교체를 위해 activity 내에 선언된 함수
    fun ReplaceFragment(TagName : String, fragment : Fragment, ID : Int){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()

        supportFragmentManager.popBackStack(TagName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentTransaction.replace(ID, fragment, TagName)
        fragmentTransaction.addToBackStack(TagName)

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
        fragmentTransaction.isAddToBackStackAllowed
    }


    // @솔빈 2022-02-08 (화)
    // SelectedPalceLise를 초기화 하는 함수
    fun ResetSelectedPlace() {
        SelectedPlaceList.clear()
    }
    // 2022-02-20 정지원 작업:  community_post_write의 edittext를 위한 변수명 추가
    // 2022-02-22 정지원 작업:  place를 전달하기위한 place_data 변수를 추가

    // 2022-02-20 정지원 작업 , 2022-03-03 정지원 작업 : changeFragment 내용 변경 및 ReplaceFragment로 변경
    // 1번 버튼 = Main화면 // 2번버튼 = PostBring화면 // 3번 버튼 = Community화면 // 4번 버튼 = FindPath화면
    // 5번 버튼 = SearchPlace화면 // 6번 버튼 = PostWrite화면 // 7번 버튼 = PostWriteMain 화면 // 8번 버튼 = PostWritePlan 화면
    // 9번 버튼 = PostWriteDetail화면 // 10번 버튼 = SearchPlace_Post화면 // 11번 버튼 = 게시글 세부 화면
    // 12번 버튼 = PlaceDetailFragment 화면 // 13번 버튼 = PostBringDetail 화면
    fun changeFragment(index: Int){
        when(index){
            1 -> {
                ReplaceFragment("Main", Main(), R.id.MainFrameLayout)
            }
            2 -> {
                ReplaceFragment("PostBring", PostBring(), R.id.MainFrameLayout)
            }
            // 2022-02-20 정지원 작업
            // setFragment를 통하여 write_main과 write_plan을 전환하는 방식으로 변경
            // 2022-02-22 정지원 작업
            // hide,show 방식으로 구현하려고했으나 버그가 너무 많고 프래그 생명주기면에서 너무 비효율적임을 느낌. + 코드가 복잡해지고, 강제종료가 자주일어남
            3 -> {
                ReplaceFragment("Community", Community(), R.id.MainFrameLayout)
            }
            4 -> {
                ReplaceFragment("FindPath", FindPath(), R.id.MainFrameLayout)
            }
            5 -> {
                ReplaceFragment("SearchPlace", SearchPlace(), R.id.MainFrameLayout)

            }
            6 ->{
                ReplaceFragment("PostWrite", Community_Post_Write(),R.id.MainFrameLayout)
            }
            7 ->{
                ReplaceFragment("PostWriteMain", Community_Post_Write_Main(),R.id.Post_Write_FrameLayout)

            }
            8 ->{
                ReplaceFragment("PostWritePlan", Community_Post_Write_Plan(),R.id.Post_Write_FrameLayout)
            }
            9 ->{
                ReplaceFragment("PostWritePlanDetail",Community_Post_Write_Plan_Detail(),R.id.Post_Write_FrameLayout)
            }
            10->{
                ReplaceFragment("SearchPlacePost",SearchPlace_Post(),R.id.Post_Write_FrameLayout)
            }
            11->{
                ReplaceFragment("CommunityPostDetail",CommunityPostDetail(),R.id.MainFrameLayout)
            }
            12->{
                ReplaceFragment("PlaceDetailFragment",Place_Detail_Fragment(),R.id.MainFrameLayout)
            }
            13->{
                ReplaceFragment("PostBringDetail",PostBringDetail(),R.id.MainFrameLayout)
            }
            // 2022-02-20 정지원 작업
            // 프래그먼트 매니저 탐색해서 프래그먼트 스택을 전부 삭제시켜주는 기능추가
        }
    }
    fun reset_value(){
        postName = ""
        postContent = ""
        postplanlist = ArrayList()
        place_list = ArrayList()
    }

    // @솔빈 2022-03-14 (월)
    // 프래그먼트들에서 사용할 레트로핏 객체 Activity에 선언
    var retrofit   = Retrofit.Builder()
        .baseUrl("http://192.168.219.101:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
