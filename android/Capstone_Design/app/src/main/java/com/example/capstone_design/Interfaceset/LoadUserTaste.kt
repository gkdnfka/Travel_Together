package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Dataset.TasteInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LoadUserTaste {
    @GET("func={func}/code={code}/")
    fun loadUserTaste(@Path("func") func : String, @Path("code") code : String): Call<TasteInfo>
}