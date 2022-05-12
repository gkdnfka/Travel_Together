package com.example.capstone_design.Activityset

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.capstone_design.Fragmentset.FirstPage
import com.example.capstone_design.Interfaceset.SendJoinInfo
import com.example.capstone_design.Interfaceset.SendLoginInfo
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MainActivity : AppCompatActivity() {
    // 회원가입, 로그인 기능을 위해 레트로핏 선언
    val retrofit = PublicRetrofit.retrofit
    val serviceForLogin = retrofit.create(SendLoginInfo::class.java)
    val serviceForJoin = retrofit.create(SendJoinInfo::class.java)
    var USER_ID = ""
    var USER_PASSWORD = ""
    var USER_NAME = ""
    var USER_CODE = ""
    lateinit var intents : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_and_join)
        intents = Intent(this, Activity::class.java) // 인텐트를 생성

        supportFragmentManager.beginTransaction().replace(
            R.id.login_and_join_frameLayout,
            FirstPage()
        ).commit()
    }
}