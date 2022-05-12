package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class TagLabelSet(
    @SerializedName("num")
    var num : String,
    @SerializedName("name")
    var name : String
)

data class TagDictSet(
    @SerializedName("num")
    var num : String,
    @SerializedName("name")
    var name : String,
    @SerializedName("labelnum")
    var labelnum : String
)

