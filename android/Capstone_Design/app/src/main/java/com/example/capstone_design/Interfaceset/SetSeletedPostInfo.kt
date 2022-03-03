package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.PostInfo

// @솔빈 2022-01-26 수
// 어답터와 통신하기 위한 인터페이스
interface SetSeletedPostInfo{
    fun setSelectedPostInfo(fragmentName : String, element : PostInfo)
}