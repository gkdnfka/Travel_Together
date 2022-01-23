package com.example.capstone_design

import com.google.gson.annotations.SerializedName

data class PostInfo(
    @SerializedName("USER_NAME")
    var username : String,
    @SerializedName("RATE")
    var rate : String,
    @SerializedName("DATES")
    var dates : String,
    @SerializedName("COURSE")
    var course : String,
    @SerializedName("CONTENT")
    var content : String,
)
