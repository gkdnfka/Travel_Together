package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Fragmentset.SettingFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadImage {
    @Multipart
    @POST("/upload/")
    fun uploadImage(@Part image: MultipartBody.Part?, @Part("upload") name: RequestBody?): retrofit2.Call<SettingFragment.success>
}