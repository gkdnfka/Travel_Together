package com.example.capstone_design.Fragmentset
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.Dataset.LoginSuccess
import com.example.capstone_design.Dataset.TasteInfo
import com.example.capstone_design.Interfaceset.LoadUserTaste
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
        var textviews = view.findViewById<TextView>(R.id.RecommendLayout)
        var mactivity = (activity as Activity)
        val serviceForTaste = mactivity.retrofit.create(LoadUserTaste::class.java)


        val model = TtDatamodel.newInstance(view.context)

        serviceForTaste.loadUserTaste("LoadTaste", mactivity.USER_CODE).enqueue(object: Callback<TasteInfo> {
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
                var catArr = arrayOf("Rest", "Nature", "Lagacy", "Activity", "Shopping", "Perfomance", "Exhibition")
                var resStr: String = String()
                var outputApplyArray = IntArray(7)

                var isAllZero: Boolean = true

                for((i, out) in output.floatArray.withIndex()) {
                    outputApplyArray[i] = if (out > 0.55f) 1 else 0

                    if(isAllZero && outputApplyArray[i] != 0) {
                        isAllZero = false
                    }

                    if(outputApplyArray[i] == 1) {
                        resStr += catArr[i] + '/'
                    }
                }
                if(isAllZero) {
                    val maxIdx = output.floatArray.indices.maxByOrNull { output.floatArray[it] } ?: -1
                    resStr += catArr[maxIdx]
                }


                string += "성별 값 : " + dataArray[7] + "\n" + resStr
                textviews.text = string
            }
        })

        return view
    }
}

