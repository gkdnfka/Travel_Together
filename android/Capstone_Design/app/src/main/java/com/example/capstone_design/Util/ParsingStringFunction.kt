package com.example.capstone_design.Util


import android.widget.Button
import android.widget.ImageView
import com.example.capstone_design.R

fun ParsingString (Str : String) : ArrayList<String>{
    var returnArray = ArrayList<String>()
    var tmpstr = ""
    for (i in Str.indices){
        if(Str[i] != ',') tmpstr += Str[i]
        else{
            returnArray.add(tmpstr)
            tmpstr = ""
        }
    }
    return returnArray
}