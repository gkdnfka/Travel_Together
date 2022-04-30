package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPopularOperation {
    @GET("func={func}/Start={Start}/End={End}")
    fun getPolpularPlace(@Path("func") func : String, @Path("Start") Start : String, @Path("End") End: String): Call<ArrayList<PlaceInfo>>

    @GET("func={func}/Start={Start}/End={End}")
    fun getPopularPost(@Path("func") func : String, @Path("Start") Start : String, @Path("End") End: String): Call<ArrayList<PostInfo>>
}