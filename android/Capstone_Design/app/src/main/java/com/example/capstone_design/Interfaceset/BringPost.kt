package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BringPost {
    @GET("func={func}/type={type}/postname={postname}/usercode={usercode}/coursedata={coursedata}")
    fun bringPost(@Path("func") func : String, @Path("type") type : String,@Path("postname") postname: String,@Path("usercode") usercode: String,@Path("coursedata") coursedata : String ): Call<ArrayList<BringPostInfo>>
}