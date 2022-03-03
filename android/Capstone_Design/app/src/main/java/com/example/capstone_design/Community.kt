package com.example.capstone_design
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.*
=======
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
>>>>>>> write_UI
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
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

// @솔빈 2022-01-26 수
// 어답터와 통신하기 위한 인터페이스
interface SetSeletedPostInfo{
    fun setSelectedPostInfo(fragmentName : String, element : PostInfo)
}


class Community : Fragment()
{
<<<<<<< HEAD
    val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.105:8080/")
=======
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var tmp = inflater.inflate(R.layout.community, container, false)
        val listView = tmp.findViewById<ListView>(R.id.postListView)
        val rebutton = tmp.findViewById<Button>(R.id.Refresh_community)
        val post_button = tmp.findViewById<Button>(R.id.post_write)
        val mActivity = activity as Activity

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.101:8080/")
>>>>>>> write_UI
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val service = retrofit.create(GetPostInfo::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        var view = inflater.inflate(R.layout.community, container, false)
        val listView = view.findViewById<ListView>(R.id.postListView)
        val rebutton = view.findViewById<ImageView>(R.id.Refresh_community)

        rebutton.setOnClickListener {
            val funcName = "SearchPost"
            var typeName = "default"

            // @솔빈 2022-01-26 (수)
            // 레트로핏의 서비스 객체를 활용해서, 인터페이스의 함수인 getpostinfo를 호출.
            // enqueue 메소드의 인자로 콜백 함수 하나를 선언해서 삽입
            // 해당 함수는 onResponse 함수와 onFailure 함수를 오버라이드 해야함
            // onResponse 에 응답 성공시 response 객체가 인자로 전달됨.
            // onResponse 내의 response.body()를 통해 ArrayList<PostInfo> 최종적으로 형태로 데이터 전달받을 수 있음!

            service.getpostinfo(funcName, typeName).enqueue(object: Callback<ArrayList<PostInfo>> {
                override fun onFailure(call : Call<ArrayList<PostInfo>>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<ArrayList<PostInfo>>, response: Response<ArrayList<PostInfo>>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    if(returndata != null){
                        // @솔빈 : 리스너 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
                        var Implemented = object : SetSeletedPostInfo {
                            override fun setSelectedPostInfo(fragmentName: String, element: PostInfo) {
                                (activity as Activity)!!.SetSelectedPostInfo(element)
                                (activity as Activity)!!.ChangeFragment(fragmentName, CommunityPostDetail(), R.id.MainFrameLayout)
                            }
                        }
                        val communityadaptor = CommunityAdaptor(view.context, returndata!!, Implemented)
                        listView.adapter = communityadaptor
                    }
                }
            })
        }
<<<<<<< HEAD
        rebutton.performClick()
        return view
=======
        post_button.setOnClickListener {
            mActivity.reset_value()
            mActivity.changeFragment(2)
        }
        return tmp
>>>>>>> write_UI
    }
}

