package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Adapterset.CommunityAdaptor
import com.example.capstone_design.Adapterset.TagSelectAdaptor
import com.example.capstone_design.Dataset.*
import com.example.capstone_design.Interfaceset.*
import com.example.capstone_design.R
import com.example.capstone_design.Util.FavoriteAddManager
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@우람 2022-05-10
//게시글 추천 페이지 제작.
class Recommend : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.recommend, container, false)
        var mActivity = (activity as Activity)
        val retrofit = mActivity.retrofit

        var user_tag = view.findViewById<RecyclerView>(R.id.user_tag_view)
        var recommend_post_view = view.findViewById<RecyclerView>(R.id.recommend_post_view)

        var back_btn = view.findViewById<ImageView>(R.id.recommend_back_btn)
        var setting_btn = view.findViewById<ImageView>(R.id.user_tag_setting)

        var intro_text = view.findViewById<TextView>(R.id.recommend_text)
        intro_text.text = mActivity.USER_NAME + " 님에게 추천하는 게시물 입니다."
        back_btn.setOnClickListener {
            mActivity.changeFragment(3)
        }
        setting_btn.setOnClickListener {
            //user tag setting page
            mActivity.changeFragment(15)
        }

        //user taste tag 불러오기
        val loadTasteFuncName = "GetLabelFromUser"
        val serviceLoadTaste = retrofit.create(GetLabelFromUser::class.java)
        var taste = String()


        serviceLoadTaste.loadUserTaste(loadTasteFuncName, mActivity.USER_CODE)
            .enqueue(object : Callback<ArrayList<TagLabelSet>> {
                override fun onFailure(call: Call<ArrayList<TagLabelSet>>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<TagLabelSet>>,
                    response: Response<ArrayList<TagLabelSet>>
                ) {
                    Log.d("성공", "Tag Load")
                    var returnData = response.body()
                    if (returnData != null) {
                        val TagViewAdapter =
                            TagSelectAdaptor(mActivity, returnData!!)

                        TagViewAdapter.setItemClickListener(object :
                            TagSelectAdaptor.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {
                                return
                            }
                        })
                        var manager = LinearLayoutManager(
                            mActivity, LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        user_tag.apply { layoutManager = manager }
                        user_tag.adapter = TagViewAdapter
                    }

                }
            })
        //user taste 분석후 게시물 load하기
        val loadPostWithTaste = "SearchPost"
        val typename = "ByPostName"
        val postLoadService = retrofit.create(GetPostInfo::class.java)

        postLoadService.getpostinfo(loadPostWithTaste, typename, "")
            .enqueue(object : Callback<ArrayList<PostInfo>> {
                override fun onFailure(call: Call<ArrayList<PostInfo>>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<PostInfo>>,
                    response: Response<ArrayList<PostInfo>>
                ) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    if (returndata != null) {
                        // @솔빈 : 리스너 인터페이스를 구현하여 객체로 만들고, 어답터의 인자로 넘겨준다.
                        var Implemented = object : SetSeletedPostInfo {
                            override fun setSelectedPostInfo(
                                fragmentName: String,
                                element: PostInfo
                            ) {
                                (activity as Activity)!!.SelectedPostDone = 0
                                (activity as Activity)!!.SetSelectedPostInfo(element)
                                (activity as Activity)!!.changeFragment(11)
                            }
                        }
                        val communityadaptor = CommunityAdaptor(
                            view.context,
                            returndata!!,
                            Implemented,
                            mActivity.USER_CODE
                        )
                        recommend_post_view.setHasFixedSize(true)
                        recommend_post_view.adapter = communityadaptor
                        recommend_post_view.layoutManager = LinearLayoutManager(view.context)
                    }
                }

            })
        return view
    }
}

