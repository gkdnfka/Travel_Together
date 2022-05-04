package com.example.capstone_design.Util

import android.app.Application
import android.content.Context
import android.util.Log


// @솔빈 2022-2-6 (토)
/*

FavoritePlaceManager 클래스

-> 앱의 어느곳이서든 사용 가능한 싱글톤 방식.
-> addFavorite()으로 게시글 북마크, 여행지 북마크 추가 모두 가능


게시글 북마크의 경우
-> addFavorite("FavoritePostList", "323")
   key는 FavoritePostList, 넣을 값은 323(게시글 번호)

여행지 북마크의 경우
-> addFavorite("FavoritePlaceList", "323")
   key는 FavoritePlaceList, 넣을 값은 323(여행지 번호)

 */


class FavoriteAddManager : Application() {
    companion object {
        lateinit var prefs: Manager
    }
    override fun onCreate() {
        prefs = Manager(applicationContext)
        super.onCreate()
    }
}

class Manager(context: Context) {
    private val prefs = context.getSharedPreferences("abcd", 0)

    fun getString(key: String, defValue: String) : String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}


// parseFavorite 은 문자열 형태의 즐겨찾기 목록을 파싱해서
// ArrayList<String> 으로 변환함.
fun parseFavorite(key : String) : ArrayList<String>{
    var string = FavoriteAddManager.prefs.getString(key, "")
    var numberlist = ArrayList<String>()
    var tmpStr = ""

    // 문자열 파싱 로직
    for (i in 0 until string.length){
        if(string[i] != ',') tmpStr += string[i]
        else{
            numberlist.add(tmpStr)
            tmpStr = ""
        }
        if(i == string.length-1) numberlist.add(tmpStr)
    }

    return numberlist
}

fun TranslateToString(arr : ArrayList<String>) : String{
    var tmpstr = ""
    for (i in 0 until arr.size){
        tmpstr += arr[i]
        if(i != arr.size-1) tmpstr += ","
    }
    return tmpstr
}


fun addFavorite(key : String, value : String){
    var string = FavoriteAddManager.prefs.getString(key, "")
    var numberlist = parseFavorite(key)
    var ret = IsInThisArray(numberlist, value)

    if(ret == -1 && string.length == 0) string = value
    else if(ret == -1 && string.length > 0) string = string + "," + value
    else if(ret >= 0) {
        numberlist.removeAt(ret)
        string = TranslateToString(numberlist)
    }

    FavoriteAddManager.prefs.setString(key, string)
}

fun IsInThisArray(arr : ArrayList<String>, str : String) : Int{
    for (i in 0 until arr.size)
        if(arr[i] == str) return i
    return -1
}




