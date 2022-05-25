package com.example.capstone_design.Util

fun TranslateTagName(Name : String) : String{
    // var nameArray = arrayOf("휴양", "액티비티", "쇼핑", "역사적 장소",   "자연 경관",	"전시물 관람",  "공연 관람", "성별")
    if(Name == "Activity") return "액티비티"
    else if(Name == "Activity/Exhibition") return "전시"
    else if(Name == "Attraction") return "관광명소"
    else if(Name == "Exhibition") return "전시"
    else if(Name == "Exhibition/Performance") return "전시"
    else if(Name == "Legacy") return "역사적장소"
    else if(Name == "Legacy/Activity") return "역사적장소"
    else if(Name == "Legacy/Exhibition") return "전시"
    else if(Name == "Legacy/Restaurant") return "역사적장소"
    else if(Name == "Nature") return "자연경관"
    else if(Name == "Performance") return "공연"
    else if(Name == "Rest") return "휴양"
    else if(Name == "Rest/Activity") return "휴양"
    else if(Name == "Rest/Exhibition") return "휴양"
    else if(Name == "Rest/Legacy") return "휴양"
    else if(Name == "Rest/Nature") return "휴양"
    else if(Name == "Shopping") return "쇼핑"
    else if(Name == "Restaurant") return "식당"
    else if(Name == "Sleep") return "숙소"
    else return "자연경관"
}