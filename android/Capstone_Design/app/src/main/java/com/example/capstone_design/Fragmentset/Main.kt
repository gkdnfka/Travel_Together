package com.example.capstone_design.Fragmentset
import android.graphics.Bitmap
import android.os.Bundle
import android.os.TestLooperManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Adapterset.PopularPlaceAdaptor
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.GetPopularOperation
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.Util.FavoriteAddManager
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.example.capstone_design.selectPlace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class Main : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.main, container, false)
        var monthlyPlaceRecycler = view.findViewById<RecyclerView>(R.id.main_monthly_place_recycler)
        var monthlyPostRecycler = view.findViewById<RecyclerView>(R.id.main_monthly_post_recycler)
        var service = PublicRetrofit.retrofit.create(GetPopularOperation::class.java)
        var hellotext = view.findViewById<TextView>(R.id.main_hello)
        val mactivity = (activity as Activity)

        val currentTime : Long = System.currentTimeMillis()
        val timeformat = SimpleDateFormat("yyyy-MM")
        var nowtime = timeformat.format(currentTime).toString()
        hellotext.text = mactivity.USER_NAME + " 님, 안녕하세요"

        service.getPolpularPlace("SearchPopularPlace", nowtime + "-00", nowtime + "-31").enqueue(object : Callback<ArrayList<PlaceInfo>?> {
            override fun onResponse(call: Call<ArrayList<PlaceInfo>?>, response: Response<ArrayList<PlaceInfo>?>) {
                var returnvalue = response.body()
                if(returnvalue != null){
                    var Implemented = object : selectPlace {
                        override fun selectplace(element : PlaceInfo) : Int {
                            return (activity as com.example.capstone_design.Activityset.Activity).AddSelectedPlace(element)
                        }
                    }
                    var Implemented2 = object : PlaceDetailPageInterface {
                        override fun change(index: Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
                            (activity as com.example.capstone_design.Activityset.Activity).SelectedPlace = placeinfo
                            if(bitmap != null) (activity as com.example.capstone_design.Activityset.Activity).SelectedBitmap = bitmap
                            (activity as com.example.capstone_design.Activityset.Activity)!!.changeFragment(index)
                        }
                    }
                    monthlyPlaceRecycler.setHasFixedSize(true)
                    monthlyPlaceRecycler.adapter = PopularPlaceAdaptor(returnvalue, mactivity, Implemented, Implemented2)
                    monthlyPlaceRecycler.layoutManager = LinearLayoutManager(view.context,  LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<ArrayList<PlaceInfo>?>, t: Throwable) {
                t.printStackTrace()
            }
        })


        service.getPopularPost("SearchPopularPost", nowtime + "-00", nowtime + "-31").enqueue(object : Callback<ArrayList<PostInfo>?> {
            override fun onResponse(call: Call<ArrayList<PostInfo>?>, response: Response<ArrayList<PostInfo>?>) {
                var returnvalue = response.body()
                if(returnvalue != null){
                    var Implemented = object : SetSeletedPostInfo {
                        override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                            (activity as com.example.capstone_design.Activityset.Activity)!!.SelectedPostDone = 0
                            (activity as com.example.capstone_design.Activityset.Activity)!!.SetSelectedPostInfo(element)
                            (activity as com.example.capstone_design.Activityset.Activity)!!.changeFragment(11)
                        }
                    }
                    val communityadaptor = CommunityAdaptor(view.context, returnvalue, Implemented, mactivity.USER_CODE)
                    monthlyPostRecycler.setHasFixedSize(true)
                    monthlyPostRecycler.adapter = communityadaptor
                    monthlyPostRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<ArrayList<PostInfo>?>, t: Throwable) {
                t.printStackTrace()
            }
        })



        return view
    }
}

