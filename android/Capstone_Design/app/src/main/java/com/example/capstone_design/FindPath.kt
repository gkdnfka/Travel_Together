package com.example.capstone_design
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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
        (activity as Activity).SelectedPlaceList.clear()


        // @솔빈 2022-2-05 토
        // parseFavorite 함수를 통해 ArrayList<string> 형태로 파싱된 여행지 리스트를 반환받아 placeNumberList 에 저장한다.
        var placeNumberList = parseFavorite("FavoritePlaceList")

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.219.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(GetPlaceInfo::class.java)

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
                    favoritePlaceListRecycle.adapter = FavoritePlaceAdaptor(returndata, (activity as Activity), Implemented)
                }
            }
        })

        var btn = view.findViewById<Button>(R.id.path_button)
        btn.setOnClickListener {
            (activity as Activity).supportFragmentManager.beginTransaction().replace(R.id.MainFrameLayout, FindPathResult()).commit()
        }
        return view
    }


}






