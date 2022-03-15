package com.example.capstone_design.Fragmentset
import android.os.Bundle
import android.util.Log
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
        var button = tmp.findViewById<Button>(R.id.Search_Tourist_Spot_Button)
        val chkbox = tmp.findViewById<RadioGroup>(R.id.RadioGroup)
        val edit = tmp.findViewById<EditText>(R.id.Search_Tourist_Spot_Edit)

        var listView = tmp.findViewById<RecyclerView>(R.id.Search_Tourist_Spot_ListView)
        var place_list: ArrayList<PlaceInfo> = ArrayList()

        button.setOnClickListener {
            val funcName = "SearchPlace"
            var typeName = ""
            val strName = edit.text.toString()
            if (chkbox.checkedRadioButtonId == R.id.radioButton_address) typeName = "ByAddr"
            else typeName = "ByName"

            service.getplaceinfo(funcName, typeName, strName)
                .enqueue(object : Callback<ArrayList<PlaceInfo>> {
                    override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                        Log.d("실패", t.toString())
                    }

                    override fun onResponse(
                        call: Call<ArrayList<PlaceInfo>>,
                        response: Response<ArrayList<PlaceInfo>>
                    ) {
                        Log.d("성공", "입출력 성공")
                        var returndata = response.body()
                        if (returndata != null) {
                            val SearchAdapter =
                                SearchPlaceAdaptor(returndata!!, (activity as Activity))

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
                                val SearchAdapter = SearchPlaceAdaptor(place_list, tmp.context)
                                listView.adapter = SearchAdapter
                            }
                        }
                    }
                })
        }
        return tmp
    }
}

