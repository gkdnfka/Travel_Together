package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.JoinSuccess
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SendJoinInfo {
    @GET("func={func}/id={id}/password={password}/name={name}/gender={gender}/address={address}/taste={taste}")
    fun sendJoinInfo(@Path("func") func : String, @Path("id") id : String, @Path("password") password : String, @Path("name") name : String, @Path("gender") gender : String, @Path("address") address : String, @Path("taste") taste : String): Call<JoinSuccess>
}