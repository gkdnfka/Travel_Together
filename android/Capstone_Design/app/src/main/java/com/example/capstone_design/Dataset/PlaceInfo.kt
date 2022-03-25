package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class PlaceInfo(
    @SerializedName("testnum")
    var num : String,
    @SerializedName("testname")
    var name : String,
    @SerializedName("testaddr")
    var address : String,
    @SerializedName("testx")
    var PosX : String,
    @SerializedName("testy")
    var PosY : String,
    @SerializedName("testdepart")
    var depart : String,
    @SerializedName("testphone")
    var phonenumber : String,
    @SerializedName("testwebsite")
    var website : String,
    @SerializedName("testopenhour")
    var openhour : String
)
