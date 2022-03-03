package com.example.capstone_design

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostWriteInfo {
    @GET("func={func}/type={type}/postmain={postmain}/userdata={userdata}/coursedata={coursedata}")
    fun postwriteinfo(@Path("func") func : String, @Path("type") type : String, @Path("postmain") postmain : String,@Path("userdata") userdata : String,@Path("coursedata") coursedata: String): Call<ArrayList<PostInfo>>
}