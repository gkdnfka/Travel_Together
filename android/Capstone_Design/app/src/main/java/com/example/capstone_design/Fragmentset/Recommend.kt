package com.example.capstone_design.Fragmentset
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Adapterset.RecommendAdaptor
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.LoginSuccess
import com.example.capstone_design.Dataset.PlaceInfo
import com.example.capstone_design.Dataset.TasteInfo
import com.example.capstone_design.Interfaceset.LoadUserTaste
import com.example.capstone_design.Interfaceset.RecommendPlace
import com.example.capstone_design.Interfaceset.SendJoinInfo
import com.example.capstone_design.R
import com.example.capstone_design.Util.FavoriteAddManager
import com.example.capstone_design.ml.TtDatamodel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/*
    date  : 2022-04-05 ~
    worker: 김우람
    note  : recommend fragment 제작.
    모델에 사용자 데이터 입력 후 모델이 추천해주는 카테고리 여행지 리스트업.
    기본적으로 해당 fragment는 SearchPlace와 비슷한 모습을 보이게된다.
 */
class Recommend : Fragment()
{
    var dataArray = floatArrayOf(0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.recommend, container, false)
        var textviews = view.findViewById<TextView>(R.id.TestText)
        var mActivity = (activity as Activity)
        val serviceForTaste = mActivity.retrofit.create(LoadUserTaste::class.java)
        var resStr: String = String() //결과값 저장

        var model = TtDatamodel.newInstance(view.context)

        serviceForTaste.loadUserTaste("LoadTaste", mActivity.USER_CODE).enqueue(object: Callback<TasteInfo> {
            override fun onFailure(call : Call<TasteInfo>, t : Throwable){
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<TasteInfo>, response: Response<TasteInfo>) {
                var returndata = response.body()
                var string = ""
                for (i in 0 until dataArray.size-2){
                    dataArray[i] = returndata!!.taste[i].toFloat()/5.0f
                }
                dataArray[7] = if(returndata!!.gender == "남성") 1.0f else 0.0f
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 8), DataType.FLOAT32)
                inputFeature.loadArray(dataArray)
                val output = model.process(inputFeature)
                    .outputFeature0AsTensorBuffer

                model.close()

                resStr = if(output.floatArray[0] > 0.55) "1" else "0"
                textviews.text = "사용자의 여행지 타입: " + resStr
            }
        }) // model 처리
        /*
           date  : 2022-04-13
           worker: 김우람
           note  : recommend recyclerView 구성
           현재는 임시 테이블인 test를 이용해 category를 통해 여행지 목록 서치.
           이후에는 model output을 기반으로 해당하는 카테고리의 여행지만 띄워줄 예정.

        */
        val recommend_spot_view = view.findViewById<RecyclerView>(R.id.Recommend_Spot_View)
        recommend_spot_view.layoutManager = LinearLayoutManager(view.context)

        val serviceRecommend = mActivity.retrofit.create(RecommendPlace::class.java)
        val funcName = "RecommendPlace"

        var catName = "Leisure"
        //이후 DB 이전 완전히 끝나면 model output type에 따라 부여예정


        serviceRecommend.recommendplace(funcName, catName)
            .enqueue(object : Callback<ArrayList<PlaceInfo>>{
                override fun onFailure(call: Call<ArrayList<PlaceInfo>>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<PlaceInfo>>,
                    response: Response<ArrayList<PlaceInfo>>
                ) {
                    Log.d("성공", "추천 카테고리 여행지 불러오기")
                    var returnData = response.body()

                    if(returnData != null){


                        var manager = LinearLayoutManager(
                            (activity as Activity),
                            LinearLayoutManager.VERTICAL,
                            false
                        )

                        var recView =
                            view.findViewById<RecyclerView>(R.id.Recommend_Spot_View)

                        recView.apply {
                            layoutManager = manager
                        }
                        var place_list:ArrayList<PlaceInfo> = ArrayList()
                        place_list = response.body()!!
                        if(place_list != null) {
                            val recommendAdaptor = RecommendAdaptor(place_list, view.context)
                            recView.adapter = recommendAdaptor
                        }
                    }
                }
        })


        return view
    }
}

