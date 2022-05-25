package com.example.capstone_design.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Interfaceset.LoadImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// @ 솔빈 2022-03-19 (토)
// 이미지 불러오는 객체(싱글톤 방식) 선언
object PublicRetrofit {
    // 서버 주소로 바꾸려면
    // -> baseUrl을 http://ec2-13-124-93-31.ap-northeast-2.compute.amazonaws.com:8080/
    var retrofit   = Retrofit.Builder()
        .baseUrl("http://ec2-13-124-93-31.ap-northeast-2.compute.amazonaws.com:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}