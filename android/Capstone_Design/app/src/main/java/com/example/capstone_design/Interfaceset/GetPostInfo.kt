package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PostInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPostInfo {
    @GET("func={func}/type={type}/")
    fun getpostinfo(@Path("func") func : String, @Path("type") type : String): Call<ArrayList<PostInfo>>
}