package com.example.capstone_design.Interfaceset

import android.telecom.Call
import com.example.capstone_design.Dataset.TagUpdateSuccess
import retrofit2.http.GET
import retrofit2.http.Path

interface UserTagUpdate {
    @GET("func={func}/usercode={usercode}/usertaste={usertaste}")
    fun userTagUpdate(@Path("func") func:String, @Path("usercode") usercode : String, @Path("usertaste") usertaste : String):retrofit2.Call<TagUpdateSuccess>
}