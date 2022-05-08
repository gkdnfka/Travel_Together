package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.TagLabelSet
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface GetTagLabel {
    @GET("func={func}")
    fun getTagLabel(@Path("func") func : String): Call<ArrayList<TagLabelSet>>
}