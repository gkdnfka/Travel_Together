package com.example.capstone_design.Interfaceset
import com.example.capstone_design.Dataset.PlaceInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPlaceInfo {
    @GET("func={func}/type={type}/str={str}/")
    fun getplaceinfo(@Path("func") func : String, @Path("type") type : String, @Path("str") str : String): Call<ArrayList<PlaceInfo>>
}
