package com.example.capstone_design.Util

import android.widget.Button
import android.widget.ImageView
import com.example.capstone_design.R

fun GetBookmarkImage (key : String, btn : ImageView, number : String){
    var tmpArray = parseFavorite(key)
    var result = IsInThisArray(tmpArray, number)
    if(result >= 0) btn.setImageResource(R.drawable.bookmarked)
    else {
        if(key == "FavoritePostList") btn.setImageResource(R.drawable.bookmarkgray)
        else btn.setImageResource(R.drawable.bookmark)
    }
}