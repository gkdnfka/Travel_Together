package com.example.capstone_design.Dataset

//@ 솔빈 3월 18일
// 이미지를 불러올 때 retrofit 함수의 넘겨주는 데이터 클래스
// number 에는 사진의 번호가 들어가고
// type 에는 'ProfileImage' or 'PlaceImage' 가 들어간다.
data class ImageInfoForLoad(
    var number : String,
    var type : String
)
