package com.example.capstone_design
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface GetPostInfo {
    @GET("func={func}/type={type}/")
    fun getpostinfo(@Path("func") func : String, @Path("type") type : String): Call<ArrayList<PostInfo>>
}


class Community : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var tmp = inflater.inflate(R.layout.community, container, false)
        val listView = tmp.findViewById<ListView>(R.id.postListView)
        val rebutton = tmp.findViewById<Button>(R.id.Refresh_community)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GetPostInfo::class.java)

        rebutton.setOnClickListener{
            val funcName = "SearchPost"
            var typeName = "default"

            service.getpostinfo(funcName, typeName).enqueue(object: Callback<ArrayList<PostInfo>> {
                override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                    Log.d("성공", "입출력 성공")
                    var returndata = response.body()
                    if(returndata != null){
                        val communityadaptor = CommunityAdaptor(tmp.context, returndata!!)
                        listView.adapter = communityadaptor
                    }
                }
            })
        }
        return tmp
    }
}

