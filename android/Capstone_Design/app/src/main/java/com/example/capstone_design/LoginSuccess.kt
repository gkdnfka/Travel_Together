package com.example.capstone_design

import com.google.gson.annotations.SerializedName

data class LoginSuccess(
        @SerializedName("number")
        var number : String,
        @SerializedName("code")
        var code : String,
        @SerializedName("name")
        var name : String

)