package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.CommentInfo
import com.example.capstone_design.Dataset.Success
import com.example.capstone_design.Fragmentset.CommentFragment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface LikeOperation{
    @GET("func={func}/NoticeNum={NoticeNum}")
    fun getLikeCnt(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String): Call<String>

    @GET("func={func}/NoticeNum={NoticeNum}/UserCode={UserCode}/Dates={Dates}")
    fun addLike(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String, @Path("UserCode") UserCode:String , @Path("Dates") Dates:String): Call<String>
}
