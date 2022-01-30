package com.example.capstone_design
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView



class Activity : AppCompatActivity() {
    // @솔빈 2022-1-29 토
    // 프래그먼트 화면 전환시(게시글 자세히 보기), 넘겨 받아야할 게시글 정보를 담는 변수
    lateinit var SelectedPostInfo : PostInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Main()).commit()
        var bottom : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.MainButton ->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Main()).commit() }
                R.id.RecommendButton->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Recommend()).commit()}
                R.id.CommunityButton->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,Community()).commit()}
                R.id.PathButton->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,FindPath()).commit()}
                R.id.TestSearch->{ supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout,SearchPlace()).commit()}
            }
            true
        }
    }

    // @솔빈 2022-1-25 수
    // ChangeFragmentInActivity -> 프래그먼트 전환을 위한 함수
    // 파라미터 설명 -> fragmentName : 조건 분기를 위한 문자열, element : 게시글에 대한 정보가 담겨있는 data 클래스
    fun ChangeFragmentInActivity(fragmentName: String, element : PostInfo) {
        when(fragmentName){
            // @솔빈 2022-1-25 수
            // 게시글 상세 페이지로 넘어가는 전환
            "ComminityPostDetail" ->{
                SelectedPostInfo = element
                supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, CommunityPostDetail()).commit()
            }

        }

    }

}