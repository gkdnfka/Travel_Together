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
    var USER_ID = "wldnjs3690"
    var USER_NAME = "정지원"
    var USER_CODE = 2
    var postName = ""
    var postContent = ""

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
            3 -> {
                setFragment("post_write_main",Community_Post_Write_Main())
            }
            4 -> {

                setFragment("post_write_plan", Community_Post_Write_Plan())
            }

            5 -> {
                setFragment("post_write_plan_detail",Community_Post_Write_Plan_Detail())

            }
            // 2022-02-20 정지원 작업
            // 프래그먼트 매니저 탐색해서 프래그먼트 스택을 전부 삭제시켜주는 기능추가
        }
    }
    fun deleteFragment(){
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
    }
    // 2022-02-20 정지원 작업
    // replace로 전환을 하게되면 데이터를 유지못하고 소멸하는 문제가 발생하여 hide,show 방식으로 바꾸었습니다.

    private fun setFragment(tag: String, fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (supportFragmentManager.findFragmentByTag(tag) == null) { // 태그에 해당하는 값이 프래그매니저에 없는 경우 프래그를 add해 준다.
            ft.add(R.id.Post_Write_FrameLayout, fragment, tag)

        }

        val post_write_main = supportFragmentManager.findFragmentByTag("post_write_main")
        val post_write_plan = supportFragmentManager.findFragmentByTag("post_write_plan")


        // Hide all Fragment
        if (post_write_main != null) {
            ft.hide(post_write_main)
        }
        if (post_write_plan != null) {
            ft.hide(post_write_plan)
        }


        // Show  current Fragment
        if (tag == "post_write_main") {
            if (post_write_main != null) {
                ft.show(post_write_main)
            }
        }
        if (tag == "post_write_plan") {
            if (post_write_plan != null) {
                ft.show(post_write_plan)
            }
        }

        ft.commitAllowingStateLoss()
    }
}