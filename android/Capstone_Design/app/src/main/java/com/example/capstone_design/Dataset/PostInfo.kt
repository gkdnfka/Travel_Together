package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class PostInfo(
    @SerializedName("NOTICEBOARD_NUM")
    var number : String,
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
    @SerializedName("NOTICEBOARD_TITLE")
    var title : String,
    @SerializedName("TAGS")
    var tags: String
)
