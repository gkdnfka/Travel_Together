package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class PlaceInfo(
    @SerializedName("num")
    var num : String,
    @SerializedName("placename")
    var name : String,
    @SerializedName("addr")
    var address : String,
    @SerializedName("x")
    var PosX : String,
    @SerializedName("y")
    var PosY : String,
    @SerializedName("category")
    var depart : String,
    @SerializedName("tel")
    var phonenumber : String,
    @SerializedName("siteaddr")
    var website : String,
    @SerializedName("openhour")
    var openhour : String,
    @SerializedName("distance")
    var distance : String
)
