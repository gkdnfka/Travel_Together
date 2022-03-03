package com.example.capstone_design

import com.google.gson.annotations.SerializedName

data class PlaceInfo(
    @SerializedName("testnum")
<<<<<<< HEAD
    var number : String,
=======
    var num : String,
>>>>>>> write_UI
    @SerializedName("testname")
    var name : String,
    @SerializedName("testaddr")
    var address : String,
    @SerializedName("testx")
    var PosX : String,
    @SerializedName("testy")
    var PosY : String,
    @SerializedName("testdepart")
    var depart : String
)
