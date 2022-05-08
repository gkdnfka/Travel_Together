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
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Recommend : Fragment()
{
    var nameArray = arrayOf("휴양", "액티비티", "쇼핑", "역사적 장소",   "자연 경관",	"전시물 관람",  "공연 관람", "성별")
    var dataArray = arrayOf(0,0,0,0,0,0,0,0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.recommend, container, false)
        var textviews = view.findViewById<TextView>(R.id.RecommendLayout)
        var mactivity = (activity as Activity)
        val serviceForTaste = mactivity.retrofit.create(LoadUserTaste::class.java)

        serviceForTaste.loadUserTaste("LoadTaste", mactivity.USER_CODE).enqueue(object: Callback<TasteInfo> {
            override fun onFailure(call : Call<TasteInfo>, t : Throwable){
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<TasteInfo>, response: Response<TasteInfo>) {
                var returndata = response.body()
                var string = ""
                for (i in 0 until nameArray.size-1){
                    string +=  nameArray[i] + "의 점수 : "+ returndata!!.taste[i] + "\n"
                    dataArray[i] = returndata!!.taste[i].toInt()
                }
                dataArray[7] = 0
                string += "성별 값 : " + dataArray[7]
                textviews.text = string
            }
        })

        return view
    }
}

