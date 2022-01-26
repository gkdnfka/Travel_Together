package com.example.capstone_design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
            3 -> {
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, Community_Post_Write_Main()).commit()
            }
            4 -> {
                supportFragmentManager.beginTransaction().replace(R.id.Post_Write_FrameLayout, Community_Post_Write_Plan()).commit()
            }
        }
    }
}