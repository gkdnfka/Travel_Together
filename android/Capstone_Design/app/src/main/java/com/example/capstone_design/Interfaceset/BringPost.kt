package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.BringPostInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BringPost {
    @GET("func={func}/type={type}/postname={postname}/usercode={usercode}/coursedata={coursedata}/seq={seq}/percent={percent}/dayindex={dayindex}/courseindex={courseindex}/coursecurrent={coursecurrent}")
    fun bringPost(@Path("func") func : String, @Path("type") type : String,@Path("postname") postname: String,@Path("usercode") usercode: String,@Path("coursedata") coursedata : String,
    @Path("seq") seq : String, @Path("percent") percent : String,@Path("dayindex") dayindex : String,@Path("courseindex") courseindex : String,@Path("coursecurrent")coursecurrent:String): Call<ArrayList<BringPostInfo>>
}