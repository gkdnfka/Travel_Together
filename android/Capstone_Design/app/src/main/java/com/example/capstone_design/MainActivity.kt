package com.example.capstone_design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firstpage)
        val btnLogin = findViewById<Button>(R.id.LoginButton)
        btnLogin.setOnClickListener{
            val Intent = Intent(this, Activity::class.java) // 인텐트를 생성
            startActivity(Intent)
        }
    }
}