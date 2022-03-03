package com.example.capstone_design

import com.google.gson.annotations.SerializedName

data class PlanInfo(
    @SerializedName("testname")
    var day : Int,
    @SerializedName("testaddr")
    var course : String,
    @SerializedName("testaddr")
    var place_list : MutableList<PlaceInfo>

)
