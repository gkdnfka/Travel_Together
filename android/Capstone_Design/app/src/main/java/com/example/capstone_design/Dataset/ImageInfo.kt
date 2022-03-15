package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class ImageInfo(
        @SerializedName("data")
        var data : ByteArray
)
