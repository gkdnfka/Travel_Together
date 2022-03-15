package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class TasteInfo(
        @SerializedName("USER_TASTE")
        var taste : String,
        @SerializedName("USER_GENDER")
        var gender : String
)
