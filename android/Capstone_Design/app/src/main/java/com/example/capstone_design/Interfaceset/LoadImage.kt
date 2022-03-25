package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import retrofit2.http.Body
import retrofit2.http.POST

interface LoadImage {
    @POST("/load")
    fun loadImage(@Body data : ImageInfoForLoad): retrofit2.Call<ImageInfo>
}