package com.example.capstone_design.Fragmentset
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Interfaceset.GetPlaceInfo
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.R
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor
import com.example.capstone_design.Adapterset.SearchPlaceAdaptor_Post
import com.example.capstone_design.Interfaceset.PlaceDetailPageInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchPlace : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var tmp = inflater.inflate(R.layout.search_tourist_spot, container, false)
        var mActivity = (activity as Activity)
        val retrofit = mActivity.retrofit
        val service = retrofit.create(GetPlaceInfo::class.java)
        val removeText = tmp.findViewById<ImageButton>(R.id.SearchRemove)
        val SearchToggle = tmp.findViewById<ToggleButton>(R.id.Search_Tourist_Spot_Toggle)
        val edit = tmp.findViewById<EditText>(R.id.Search_Tourist_Spot_Edit)
        var listView = tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
        var place_list: ArrayList<PlaceInfo> = ArrayList()
        listView.layoutManager = LinearLayoutManager(tmp.context)


        edit.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(hasFocus){
                    removeText.visibility = View.VISIBLE
                }
                else
                {
                    removeText.visibility = View.INVISIBLE
                }
            }
        })
        edit.setOnFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if(hasFocus){
                    removeText.visibility = View.VISIBLE
                }
                else
                {
                    removeText.visibility = View.INVISIBLE
                }
            }
        })
        edit.setOnKeyListener { v, keycode, event ->

            if(event.action == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER)
            {

                // 2022-03-13 ????????? ??????
                // ??????????????? ?????? ?????? true = ??????????????? false = ????????????

                // @?????? 2022-01-26 (???)
                // ??????????????? ????????? ????????? ????????????, ?????????????????? ????????? getpostinfo??? ??????.
                // enqueue ???????????? ????????? ?????? ?????? ????????? ???????????? ??????
                // ?????? ????????? onResponse ????????? onFailure ????????? ??????????????? ?????????
                // onResponse ??? ?????? ????????? response ????????? ????????? ?????????.
                // onResponse ?????? response.body()??? ?????? ArrayList<PostInfo> ??????????????? ????????? ????????? ???????????? ??? ??????!

                val funcName = "SearchPlace"
                var typeName = ""
                val strName = edit.text.toString()
                if (SearchToggle.isChecked == true) typeName = "ByAddr"
                else typeName = "ByName"
                edit.text = null
                service.getplaceinfo(funcName, typeName, strName)
                    .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                        override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                            Log.d("??????", t.toString())
                        }

                        override fun onResponse(
                            call: Call<ArrayList<PlaceInfo>>,
                            response: Response<ArrayList<PlaceInfo>>
                        ) {
                            Log.d("??????", "????????? ??????")
                            var returndata = response.body()
                            if (returndata != null) {
                                var Implemented = object : PlaceDetailPageInterface {
                                    override fun change(index: Int, placeinfo: PlaceInfo, bitmap: Bitmap?) {
                                        (activity as Activity).SelectedPlace = placeinfo
                                        if(bitmap != null) (activity as Activity).SelectedBitmap = bitmap
                                        (activity as Activity)!!.changeFragment(index)
                                    }
                                }
                                val SearchAdapter =
                                    SearchPlaceAdaptor(returndata!!, (activity as Activity), Implemented)

                                var manager = LinearLayoutManager(
                                    (activity as Activity),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                var listView =
                                    tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
                                listView.apply {
                                    layoutManager = manager
                                }
                                place_list = response.body()!!
                                if (place_list != null) {
                                    listView.adapter = SearchAdapter
                                }
                            }
                        }
                    })
                // 2022-03-13 ????????? ??????
                // ?????? ????????? ???????????? text??? null??? ????????? ???????????? ???????????? ????????????

                edit.clearFocus()
            }
            true
        }
        removeText.setOnClickListener {
            edit.text = null
        }
        return tmp
    }
}

