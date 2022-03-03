package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName

data class CommentInfo(
        @SerializedName("COMMENT_NUM")
        var number : String,
        @SerializedName("NOTICEBOARD_NUM")
        var post_number : String,
        @SerializedName("USER_CODE")
        var user_number : String,
        @SerializedName("DATES")
        var dates : String,
        @SerializedName("CONTENT")
        var content : String,
        @SerializedName("USER_NAME")
        var name : String
)
