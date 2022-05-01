package com.example.capstone_design.Util

fun TranslateTagName(Name : String) : String{
    // var nameArray = arrayOf("휴양", "액티비티", "쇼핑", "역사적 장소",   "자연 경관",	"전시물 관람",  "공연 관람", "성별")
    if(Name == "Leisure") return "휴양"
    else if(Name == "Activity") return "액티비티"
    else if(Name == "Shopping") return "쇼핑"
    else if(Name == "History") return "역사적장소"
    else if(Name == "Attraction") return "자연경관"
    else if(Name == "Culture") return "전시물관람"
    else return "자연경관"
}