package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PostInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostWriteInfo {
    @GET("func={func}/type={type}/postmain={postmain}/userdata={userdata}/coursedata={coursedata}/tags={tags}/labels={labels}")
    fun postwriteinfo(@Path("func") func : String, @Path("type") type : String, @Path("postmain") postmain : String,@Path("userdata") userdata : String,@Path("coursedata") coursedata: String, @Path("tags") tags : String, @Path("labels") labels : String): Call<ArrayList<PostInfo>>
}