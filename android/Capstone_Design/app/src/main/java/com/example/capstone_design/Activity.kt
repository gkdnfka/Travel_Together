package com.example.capstone_design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
class Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Main()).commit()
        var bottom : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.MainButton ->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Main()).commit() }
                R.id.RecommendButton->{supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Recommend()).commit()}
                R.id.CommunityButton->{supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Community()).commit()}
                R.id.PathButton->{supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,FindPath()).commit()}
                R.id.TestSearch->{supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,SearchPlace()).commit()}
            }
            true
        }
    }

    // 2022-02-20 정지원 작업:  community_post_write의 edittext를 위한 변수명 추가
    // 2022-02-22 정지원 작업:  place를 전달하기위한 place_data 변수를 추가
    var USER_ID = "wldnjs3690"
    var USER_NAME = "정지원"
    var USER_CODE = 2
    var postName = ""
    var postContent = ""
    var day = 1
    var postplanlist: MutableList<PlanInfo> = ArrayList()
    var place_list : MutableList<PlaceInfo> = ArrayList()
    // 2022-02-20 정지원 작업1번 버튼 = community main // 2번버튼 = post_write // 3번 버튼 = post_write_main // 4번 버튼 = post_write_plan
    fun changeFragment(index: Int){
        when(index){
            1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, Community()).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, Community_Post_Write()).commit()
            }
            // 2022-02-20 정지원 작업
            // setFragment를 통하여 write_main과 write_plan을 전환하는 방식으로 변경
            // 2022-02-22 정지원 작업
            // hide,show 방식으로 구현하려고했으나 버그가 너무 많고 프래그 생명주기면에서 너무 비효율적임을 느낌. + 코드가 복잡해지고, 강제종료가 자주일어남
            3 -> {
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, Community_Post_Write_Main()).commit()
            }
            4 -> {
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, Community_Post_Write_Plan()).commit()
            }
            5 -> {
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, Community_Post_Write_Plan_Detail()).commit()
            }
            6 ->{
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, SearchPlace()).commit()
            }
            7 ->{
                supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Community()).commit()
            }
            // 2022-02-20 정지원 작업
            // 프래그먼트 매니저 탐색해서 프래그먼트 스택을 전부 삭제시켜주는 기능추가
        }
    }
    fun reset_value(){
        postName = ""
        postContent = ""
        postplanlist = ArrayList()
    }
}