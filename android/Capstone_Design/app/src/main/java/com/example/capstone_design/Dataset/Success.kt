package com.example.capstone_design.Dataset

import com.google.gson.annotations.SerializedName
// @솔빈 2022-02-26 토
// DB 통신 실패 성공 여부 판단을 위한 dataclass
data class Success(
    @SerializedName("number")
    var number : String
)