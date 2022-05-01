package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class TagLabelSet(
    @SerializedName("name")
    var name : String,
    @SerializedName("num")
    var num : String
)
