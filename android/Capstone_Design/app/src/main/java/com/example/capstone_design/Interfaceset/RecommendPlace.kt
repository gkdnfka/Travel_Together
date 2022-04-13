package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PlaceInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendPlace {
    @GET("func={func}/cat={cat}/")
    fun recommendplace(@Path("func") func : String, @Path("cat") cat : String) : Call<ArrayList<PlaceInfo>>
}