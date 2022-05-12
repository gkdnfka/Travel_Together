package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.TagDictSet
import com.example.capstone_design.Dataset.TagLabelSet
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface GetTagLabel {
    @GET("func={func}")
    fun getTagLabel(@Path("func") func : String): Call<ArrayList<TagLabelSet>>
}

interface GetTagDict {
    @GET("func={func}/type={type}/number={number}")
    fun getTagDict(@Path("func") func : String, @Path("type") type : String, @Path("number") number : String): Call<ArrayList<TagDictSet>>
}