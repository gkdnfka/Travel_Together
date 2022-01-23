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
}