package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class BringPostInfo(
    @SerializedName("seq")
    var seq : String,
    @SerializedName("POST_NAME")
    var postname : String,
    @SerializedName("USER_CODE")
    var usercode : String,
    @SerializedName("COURSE")
    var course : String,
    @SerializedName("DAY_INDEX")
    var dayindex : String,
    @SerializedName("COURSE_INDEX")
    var courseindex : String,
    @SerializedName("COMPLETE_PERCENT")
    var percent : String,
    @SerializedName("COMPLETE_NUM")
    var completeNum : String
)
