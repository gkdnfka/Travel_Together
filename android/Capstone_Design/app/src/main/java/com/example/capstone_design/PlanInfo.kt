package com.example.capstone_design

import com.google.gson.annotations.SerializedName

data class PlanInfo(
    @SerializedName("testname")
    var day : String,
    @SerializedName("testaddr")
    var course : String
)
