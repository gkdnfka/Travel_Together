package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Dataset.TagLabelSet
import com.example.capstone_design.Dataset.TasteInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetLabelFromUser {
    @GET("func={func}/code={code}/")
    fun loadUserTaste(@Path("func") func : String, @Path("code") code : String): Call<ArrayList<TagLabelSet>>
}