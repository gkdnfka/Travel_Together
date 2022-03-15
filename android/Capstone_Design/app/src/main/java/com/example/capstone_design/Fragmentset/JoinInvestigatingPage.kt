package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.MainActivity
import com.example.capstone_design.Dataset.JoinSuccess
import com.example.capstone_design.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


class JoinInvestigatingPage(val join_name : String, val join_id : String, val join_password : String, val gender : String, val join_address : String) : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.join_page_investigate, container, false)

        // 휴양	액티비티	쇼핑  역사적_장소   자연_경관	전시물_관람  공연_관람 성별
        val joinBtn = view.findViewById<Button>(R.id.join_page_investigate_join_btn)
        val backBtn = view.findViewById<ImageView>(R.id.join_page_investigate_back_btn)

        // 휴양	액티비티	쇼핑  역사적_장소   자연_경관	전시물_관람  공연_관람 성별
        //  Leisure Activity Shoping Culture Attraction exhibition Parade
        var LeisureSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Leisure)
        var LeisureText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Leisure_score)

        var ActivitySeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Activity)
        var ActivityText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Activity_score)

        var ShopingSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Shoping)
        var ShopingText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Shoping_score)

        var CultureSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Culture)
        var CultureText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Culture_score)

        var AttractionSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Attraction)
        var AttractionText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Attraction_score)

        var exhibitionSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_exhibition)
        var exhibitionText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_exhibition_score)

        var ParadeSeek = view.findViewById<SeekBar>(R.id.join_page_investigate_seekbar_Parade)
        var ParadeText = view.findViewById<TextView>(R.id.join_page_investigate_seekbar_Parade_score)


        LeisureSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                LeisureText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        ActivitySeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                ActivityText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        ShopingSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                ShopingText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        CultureSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                CultureText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        AttractionSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                AttractionText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        exhibitionSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                exhibitionText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        ParadeSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                ParadeText.text = "$p1" + "점"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })


        backBtn.setOnClickListener{
            (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
        }

        joinBtn.setOnClickListener {
            // @솔빈 2022-02-21 월
            // 회원가입을 시도하는 로직
            // 회원가입 성공여부는 서버단에서 처리하고, 반환값을 건네주는 방식
            // 빈칸이 하나라도 입력되어 있는 경우 인정하지 않음.

            // Leisure Activity Shoping Culture Attraction exhibition Parade
            var str = ""
            str += LeisureSeek.progress.toString()
            str += ActivitySeek.progress.toString()
            str += ShopingSeek.progress.toString()
            str += CultureSeek.progress.toString()
            str += AttractionSeek.progress.toString()
            str += exhibitionSeek.progress.toString()
            str += ParadeSeek.progress.toString()

            (activity as MainActivity).serviceForJoin.sendJoinInfo("Join", join_id, join_password, join_name, gender, join_address, str).enqueue(object: Callback<JoinSuccess> {
                override fun onFailure(call : Call<JoinSuccess>, t : Throwable){
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<JoinSuccess>, response: Response<JoinSuccess>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()

                    // @솔빈 2022-02-21 월
                    // 회원가입에 성공한 경우 success 객체의 변수 number 에  1이 반환되고
                    // 회원가입에 실패한 경우 success 객체의 변수 number 에  0보다 작은 값이 반환된다.
                    if(returndata!!.number == "1") {
                        (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.login_and_join_frameLayout, FirstPage()).commit()
                        Toast.makeText((activity as MainActivity), "회원가입 완료.", Toast.LENGTH_SHORT).show()
                    }
                    else if(returndata!!.number == "-1") Toast.makeText((activity as MainActivity), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }
}


