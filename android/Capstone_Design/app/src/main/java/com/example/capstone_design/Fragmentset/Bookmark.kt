package com.example.capstone_design
import PathBookmarkAdapter
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Adapterset.FavoritePlaceAdaptor
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.GetPostInfo
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.Util.FavoriteAddManager
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.Util.parseFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface selectPlace{
    fun selectplace(element: PlaceInfo) : Int
}


class Bookmark : Fragment()
{
    // 2022-04-20 @솔빈
    // selectedTab -> 현재 선택된 탭의 종류를 저장하는 변수
    var selectedTab : Int = 0
    val serviceforplace = PublicRetrofit.retrofit.create(GetPlaceInfo::class.java)
    val serviceforpost = PublicRetrofit.retrofit.create(GetPostInfo::class.java)
    lateinit var ly : LinearLayout
    lateinit var btn : Button
    lateinit var favoritePlaceListRecycle : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var mActivity = activity as Activity
        var view = inflater.inflate(R.layout.bookmark, container, false)
        var placetab = view.findViewById<LinearLayout>(R.id.bookmark_placebutton)
        var posttab = view.findViewById<LinearLayout>(R.id.bookmark_postbutton)
        var pathtab = view.findViewById<LinearLayout>(R.id.bookmark_pathbutton)
        ly = view.findViewById<LinearLayout>(R.id.bookmark_Linear)

        //#EEF3E9
        mActivity.SelectedPlaceList.clear()
        favoritePlaceListRecycle = view.findViewById<RecyclerView>(R.id.bookmark_favorite_place_list)

        var postNumberList  = parseFavorite("FavoritePostList")
        changeToPlaceBookMarkPage()
        addButton()

        placetab.setOnClickListener {
            favoritePlaceListRecycle.adapter = null
            changeToPlaceBookMarkPage()
            if(selectedTab != 1) addButton()
            selectedTab = 1
            posttab.setBackgroundColor(resources.getColor(R.color.bookmark))
            placetab.setBackgroundColor(resources.getColor(R.color.black))
            pathtab.setBackgroundColor(resources.getColor(R.color.bookmark))
        }

        posttab.setOnClickListener {
            favoritePlaceListRecycle.adapter = null
            changeToPostBookMarkPage()
            ly.removeViewInLayout(btn)
            placetab.setBackgroundColor(resources.getColor(R.color.bookmark))
            posttab.setBackgroundColor(resources.getColor(R.color.black))
            pathtab.setBackgroundColor(resources.getColor(R.color.bookmark))
            selectedTab = 0
        }

        pathtab.setOnClickListener {
            favoritePlaceListRecycle.adapter = null
            changeToPathBookMarkPage()
            ly.removeViewInLayout(btn)
            pathtab.setBackgroundColor(resources.getColor(R.color.black))
            placetab.setBackgroundColor(resources.getColor(R.color.bookmark))
            posttab.setBackgroundColor(resources.getColor(R.color.bookmark))
            selectedTab = 2
        }



        return view
    }


    fun changeToPlaceBookMarkPage(){
        var placeNumberList = parseFavorite("FavoritePlaceList")
        var strForQuery = ""
        for(i in 0 until placeNumberList.size){
            strForQuery +=  "testnum = " + placeNumberList[i]
            if(i+1 < placeNumberList.size) strForQuery += " OR "
        }

        serviceforplace.getplaceinfo("SearchPlace", "ByIds", strForQuery).enqueue(object: Callback<ArrayList<PlaceInfo>> {
            override fun onFailure(call : Call<ArrayList<PlaceInfo>>, t : Throwable){
                Log.d("FindPath / 실패", t.toString())
            }
            override fun onResponse(call: Call<ArrayList<PlaceInfo>>, response: Response<ArrayList<PlaceInfo>>) {
                Log.d("FindPath / DB 입출력 성공", "DB 입출력 성공")
                var returndata = response.body()
                if(returndata != null){
                    var Implemented = object : selectPlace {
                        override fun selectplace(element : PlaceInfo) : Int {
                            return (activity as Activity).AddSelectedPlace(element)
                        }
                    }
                    var Implemented2 = object : PlaceDetailPageInterface {
                        override fun change(index: Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
                            (activity as Activity).SelectedPlace = placeinfo
                            if(bitmap != null) (activity as Activity).SelectedBitmap = bitmap
                            (activity as Activity)!!.changeFragment(index)
                        }
                    }
                    favoritePlaceListRecycle.adapter = FavoritePlaceAdaptor(returndata, (activity as Activity), Implemented, Implemented2)
                }
            }
        })
    }

    fun changeToPostBookMarkPage(){
        var postNumberList = parseFavorite("FavoritePostList")
        var strForQuery = ""
        for(i in 0 until postNumberList.size){
            strForQuery +=  "NOTICEBOARD_NUM = " + postNumberList[i]
            if(i+1 < postNumberList.size) strForQuery += " OR "
        }

        serviceforpost.getpostinfo("SearchPost", "ByIds", strForQuery).enqueue(object: Callback<ArrayList<PostInfo>> {
            override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                Log.d("FindPath / 실패", t.toString())
            }
            override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                Log.d("FindPath / DB 입출력 성공", "DB 입출력 성공")
                var returndata = response.body()
                if(returndata != null){
                    var Implemented = object : SetSeletedPostInfo {
                        override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                            (activity as Activity)!!.SelectedPostDone = 0
                            (activity as Activity)!!.SetSelectedPostInfo(element)
                            (activity as Activity)!!.changeFragment(11)
                        }
                    }
                    favoritePlaceListRecycle.adapter = CommunityAdaptor(activity as Activity, returndata, Implemented, (activity as Activity).USER_CODE)
                }
            }
        })
    }

    interface pathbookmarkinterface {
        fun change(pathnum : String)
    }

    fun changeToPathBookMarkPage(){
        var pathNumberList = parseFavorite("FavoritePathList")

        var tmp = object : pathbookmarkinterface{
            override fun change(pathnum : String) {
                Log.d("현재 pathlist ", pathNumberList.toString())
                var placeNumberList = parseFavorite("path"+pathnum)
                Log.d("현재 platNumberList ", placeNumberList.toString())
                var strForQuery = ""
                for(i in 0 until placeNumberList.size){
                    strForQuery +=  "testnum = " + placeNumberList[i]
                    if(i+1 < placeNumberList.size) strForQuery += " OR "
                }

                strForQuery += " ORDER BY FIELD(testnum,"
                for (i in 0 until placeNumberList.size){
                    strForQuery += placeNumberList[i]
                    if(i+1 < placeNumberList.size) strForQuery += ","
                }
                strForQuery += ")"

                serviceforplace.getplaceinfo("SearchPlace", "ByIds", strForQuery).enqueue(object: Callback<ArrayList<PlaceInfo>> {
                    override fun onFailure(call : Call<ArrayList<PlaceInfo>>, t : Throwable){
                    }
                    override fun onResponse(call: Call<ArrayList<PlaceInfo>>, response: Response<ArrayList<PlaceInfo>>) {
                        var returndata = response.body()
                        if(returndata != null){
                            (activity as Activity).SelectedPlaceList = returndata
                            (activity as Activity).SelectedPlaceFlag = 1
                            (activity as Activity).changeFragment(13)
                        }
                    }
                })
            }
        }
        favoritePlaceListRecycle.adapter = PathBookmarkAdapter(pathNumberList , activity as Activity, tmp)
    }

    fun addButton(){
        btn = Button((activity as Activity))
        var lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5f)
        lp.weight = 6f
        lp.height = 0
        btn.text = "경로 추천"
        btn.setBackgroundColor(resources.getColor(R.color.main_theme))
        btn.layoutParams = lp

        btn.setOnClickListener {
            (activity as Activity).SelectedPlaceFlag = 0
            (activity as Activity).changeFragment(13)
        }
        ly.addView(btn)
    }
}






