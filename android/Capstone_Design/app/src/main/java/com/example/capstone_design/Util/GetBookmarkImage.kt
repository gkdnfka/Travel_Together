package com.example.capstone_design.Util

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.capstone_design.R

fun GetBookmarkImage (key : String, btn : ImageView, number : String){
    var tmpArray = parseFavorite(key)
    var result = IsInThisArray(tmpArray, number)
    if(result >= 0) {
        btn.setImageResource(R.drawable.bookmarked)
        Log.d("GetBookmark테스트", result.toString())
    }
    else {
        Log.d("GetBookmark테스트", result.toString())
        if(key == "FavoritePostList" ) btn.setImageResource(R.drawable.bookmarkgray)
        else if(key == "FavoritePathList") btn.setImageResource(R.drawable.bookmarkgray)
        else btn.setImageResource(R.drawable.bookmark)
    }
}