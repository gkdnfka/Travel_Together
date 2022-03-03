package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.LoginSuccess
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SendLoginInfo {
    @GET("func={func}/id={id}/password={password}")
    fun sendLoginInfo(@Path("func") func : String, @Path("id") id : String, @Path("password") password : String): Call<LoginSuccess>
}