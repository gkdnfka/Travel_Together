package com.example.capstone_design
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.FavoritePlaceAdaptor
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Fragmentset.FindPathResult
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.Util.parseFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface selectPlace{
    fun selectplace(element: PlaceInfo) : Int
}


class FindPath : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.path, container, false)
        var mActivity = activity as Activity
        mActivity.SelectedPlaceList.clear()
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetPlaceInfo::class.java)


        // @솔빈 2022-2-05 토
        // parseFavorite 함수를 통해 ArrayList<string> 형태로 파싱된 여행지 리스트를 반환받아 placeNumberList 에 저장한다.
        var placeNumberList = parseFavorite("FavoritePlaceList")

        var strForQuery = ""
        for(i in 0 until placeNumberList.size){
            strForQuery +=  "testnum = " + placeNumberList[i]
            if(i+1 < placeNumberList.size) strForQuery += " OR "
        }

        service.getplaceinfo("SearchPlace", "ByIds", strForQuery).enqueue(object: Callback<ArrayList<PlaceInfo>> {
            override fun onFailure(call : Call<ArrayList<PlaceInfo>>, t : Throwable){
                Log.d("FindPath / 실패", t.toString())
            }
            override fun onResponse(call: Call<ArrayList<PlaceInfo>>, response: Response<ArrayList<PlaceInfo>>) {
                Log.d("FindPath / DB 입출력 성공", "DB 입출력 성공")
                var returndata = response.body()
                if(returndata != null){
                    var favoritePlaceListRecycle = view.findViewById<RecyclerView>(R.id.path_favorite_place_list)

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

        var btn = view.findViewById<Button>(R.id.path_button)
        btn.setOnClickListener {
            (activity as Activity).changeFragment(13)
        }
        return view
    }


}






