package com.example.capstone_design.Fragmentset


import android.app.Dialog
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityPostDetailCommentAdaptor
import com.example.capstone_design.Adapterset.Place_Detail_Comment_Adaptor
import com.example.capstone_design.Dataset.CommentInfo
import com.example.capstone_design.Dataset.Success
import com.example.capstone_design.Interfaceset.CommentInterface
import com.example.capstone_design.Interfaceset.CommentModifyInterface
import com.example.capstone_design.R
import com.example.capstone_design.Util.PublicRetrofit
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.round
import java.text.SimpleDateFormat

class Place_Detail_Fragment : Fragment()
{
    var commentInfoList = ArrayList<CommentInfo>()
    // @ 솔빈 2022-02-26 토
    // serviceForComment -> 댓글에 대한 DB 입출력을 위한 모든 함수들을 담고있는 인터페이스 객체
    val serviceForComment = PublicRetrofit.retrofit.create(CommentInterface::class.java)

    lateinit var commentRecycler : RecyclerView
    lateinit var edittxt : EditText
    var editFlag = 0
    var editCommentNum = "0"
    var avgRating = 0.0f
    var ratingList = arrayOf(0,0,0,0,0,0)
    var commentCnt = 0

    lateinit var commentCntText : TextView
    lateinit var commentAvgText : TextView
    lateinit var commentPeopleCntText : TextView
    lateinit var commentRatingbar : RatingBar

    lateinit var rating1 : ProgressBar
    lateinit var rating2 : ProgressBar
    lateinit var rating3 : ProgressBar
    lateinit var rating4 : ProgressBar
    lateinit var rating5 : ProgressBar


    // @ 솔빈 2022-02-28 일
    // deleteCommentInterface -> 댓글 삭제를 위한 인터페이스 객체 선언
    // editCommentInterface   -> 댓글 수정을 위한 인터페이스 객체 선언
    var commentModifyInterface = object : CommentModifyInterface {
        override fun DeleteComment(CommentNum : String) {
            serviceForComment.deletecomment("DeleteComment", CommentNum).enqueue(object:
                Callback<Success> {
                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    Log.d("성공", "DB 입출력 성공")
                    var returndata = response.body()
                    LoadCommentList()
                }
            })
        }

        val currentTime : Long = System.currentTimeMillis()
        val timeformat = SimpleDateFormat("yyyy-MM-dd")
        var nowtime = timeformat.format(currentTime).toString()

        override fun EditComment(newContent : String, commentNum : String) {
            editFlag = 1
            edittxt.setText(newContent)
            editCommentNum = commentNum
        }

        override fun ChangeScore(commentNum : String) {
            editCommentNum = commentNum

            var dialog = Dialog((activity as Activity))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.comment_evaluation_dialog_item)

            var okaybutton = dialog.findViewById<TextView>(R.id.comment_evaluation_dialog_okaybtn)
            var cancelbutton = dialog.findViewById<TextView>(R.id.comment_evaluation_dialog_cancelbtn)
            var ratingBar = dialog.findViewById<RatingBar>(R.id.comment_evaluation_dialog_rating)

            okaybutton.setOnClickListener {
                var Rating = ratingBar.rating.toInt().toString()
                dialog.dismiss()
                EditCommentFunc(nowtime, Rating)
            }

            cancelbutton.setOnClickListener {
                dialog.dismiss()
            }

            ratingBar.setOnRatingBarChangeListener{ ratingBar: RatingBar, fl: Float, b: Boolean ->
                if(ratingBar.rating == 0.0f){
                    ratingBar.rating = 1f
                }
            }

            dialog.show()
        }
    }

    // @솔빈 2022-02-25 금
    // 해당 게시글에 속하는 댓글들을 데이터베이스에서 불러와서 commentInfoList에 저장하고
    // adaptor을 붙여주는 동작을 수행하는 함수
    fun LoadCommentList(){
        serviceForComment.getCommentInfo("SearchComment", (activity as Activity).SelectedPlace.num, "Place").enqueue(object:
            Callback<ArrayList<CommentInfo>> {
            override fun onFailure(call: Call<ArrayList<CommentInfo>>, t: Throwable) {
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<CommentInfo>>, response: Response<ArrayList<CommentInfo>>) {
                Log.d("성공", "DB 입출력 성공")
                var returndata = response.body()
                commentInfoList = returndata!!
                avgRating = 0.0f
                commentCnt = commentInfoList.size
                for (i in 0 until 6) ratingList[i] = 0

                for (i in 0 until commentCnt) {
                    avgRating += commentInfoList[i].rating.toFloat()
                    ratingList[commentInfoList[i].rating.toInt()]++
                }

                avgRating /= commentCnt.toFloat()
                avgRating = (round(avgRating * 10).toFloat()/10)

                commentCntText.text = commentCnt.toString() + " 개의 댓글"
                commentAvgText.text = avgRating.toString() + " 점"
                commentPeopleCntText.text = commentCnt.toString() + " 명의 평가"
                commentRatingbar.rating = avgRating

                for (i in 0 until 6){
                    Log.d("ratingList", "i : "+ i.toString() + " / " +ratingList[i].toString())
                }

                rating1.setProgress((round(100f*ratingList[1].toFloat()/commentCnt.toFloat())))
                rating1.secondaryProgress = ((round(100f*ratingList[1].toFloat()/commentCnt.toFloat())))
                rating2.setProgress((round(100f*ratingList[2].toFloat()/commentCnt.toFloat())))
                rating2.secondaryProgress = ((round(100f*ratingList[2].toFloat()/commentCnt.toFloat())))
                rating3.setProgress((round(100f*ratingList[3].toFloat()/commentCnt.toFloat())))
                rating3.secondaryProgress = ((round(100f*ratingList[3].toFloat()/commentCnt.toFloat())))
                rating4.setProgress((round(100f*ratingList[4].toFloat()/commentCnt.toFloat())))
                rating4.secondaryProgress = ((round(100f*ratingList[4].toFloat()/commentCnt.toFloat())))
                rating5.setProgress((round(100f*ratingList[5].toFloat()/commentCnt.toFloat())))
                rating5.secondaryProgress = ((round(100f*ratingList[5].toFloat()/commentCnt.toFloat())))

                commentRecycler.adapter = Place_Detail_Comment_Adaptor(commentInfoList, (activity as Activity), (activity as Activity).USER_CODE, commentModifyInterface)
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.place_detail_fragment, container, false)

        // @솔빈 2022-03-22 (화)
        // 뷰 객체 및 액티비티 객체 초기화 파트
        var mActivity = (activity as Activity)

        var placeImage = view.findViewById<ImageView>(R.id.place_detail_fragment_image)
        var placeName = view.findViewById<TextView>(R.id.place_detail_fragment_place_name)
        var placeAddr = view.findViewById<TextView>(R.id.place_detail_fragment_place_addr)
        var placePhone = view.findViewById<TextView>(R.id.place_detail_fragment_phone)
        var placeWeb = view.findViewById<TextView>(R.id.place_detail_fragment_website)
        var openHourLinear = view.findViewById<LinearLayout>(R.id.place_detail_fragment_open_hour_linear_layout)
        var openHourInfoString = mActivity.SelectedPlace.openhour
        var backbutton = view.findViewById<ImageView>(R.id.place_detail_back_button)

        commentCntText = view.findViewById<TextView>(R.id.place_detail_fragment_comment_cnt)
        commentAvgText = view.findViewById<TextView>(R.id.place_detail_fragment_evaluation_avg_score)
        commentPeopleCntText = view.findViewById<TextView>(R.id.place_detail_fragment_evaluation_count)
        commentRatingbar = view.findViewById<RatingBar>(R.id.place_detail_comment_item_score)
        rating1 = view.findViewById<ProgressBar>(R.id.place_detail_score_1)
        rating2 = view.findViewById<ProgressBar>(R.id.place_detail_score_2)
        rating3 = view.findViewById<ProgressBar>(R.id.place_detail_score_3)
        rating4 = view.findViewById<ProgressBar>(R.id.place_detail_score_4)
        rating5 = view.findViewById<ProgressBar>(R.id.place_detail_score_5)

        edittxt = view.findViewById<EditText>(R.id.place_detail_fragment_comment_text)

        backbutton.setOnClickListener {
            mActivity.onBackPressed()
            //mActivity.changeFragment(11)
        }

        if(mActivity.SelectedBitmap != null) placeImage.setImageBitmap(mActivity.SelectedBitmap)
        placeName.setText(mActivity.SelectedPlace.name)
        placeAddr.setText(mActivity.SelectedPlace.address)
        placePhone.setText(mActivity.SelectedPlace.phonenumber)
        placeWeb.setText(mActivity.SelectedPlace.website)

        // setOpenHourData 호출하여 오픈 시간 파싱
        setOpenHourData(openHourLinear, openHourInfoString)

        commentRecycler = view.findViewById<RecyclerView>(R.id.place_detail_fragment_recycler)
        commentRecycler.layoutManager = LinearLayoutManager(view.context)
        // 댓글 리스트 호출
        LoadCommentList()

        var btn = view.findViewById<Button>(R.id.place_detail_fragment_comment_send_btn)
        btn.setOnClickListener {
            var flag = 0
            var Rating = ""
            val currentTime : Long = System.currentTimeMillis()
            val timeformat = SimpleDateFormat("yyyy-MM-dd")
            var nowtime = timeformat.format(currentTime).toString()

            var dialog = Dialog((activity as Activity))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.comment_evaluation_dialog_item)

            var okaybutton = dialog.findViewById<TextView>(R.id.comment_evaluation_dialog_okaybtn)
            var cancelbutton = dialog.findViewById<TextView>(R.id.comment_evaluation_dialog_cancelbtn)
            var ratingBar = dialog.findViewById<RatingBar>(R.id.comment_evaluation_dialog_rating)

            okaybutton.setOnClickListener {
                flag = 1
                Rating = ratingBar.rating.toFloat().toInt().toString()
                Toast.makeText((activity as Activity), "레이팅 : " + Rating, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                if(editFlag == 0) WriteCommentFunc(nowtime, Rating)
                else EditCommentFunc(nowtime, Rating)
            }

            cancelbutton.setOnClickListener {
                flag = 0
                dialog.dismiss()
            }

            dialog.show()
        }

        return view
    }

    // 오픈 시간을 파싱 해서, openHourLinearlayout에 textview 들을 동적으로 생성해서 저장하는 함수
    fun setOpenHourData(LinearLay : LinearLayout, openHourInfoString : String){
        var idx = 0
        var string = ""

        while(idx < openHourInfoString.length){
            if(openHourInfoString[idx] == '/') {
                var newTextView = TextView(context)
                newTextView.setText(string)
                LinearLay.addView(newTextView)
                string = ""
            }
            else string += openHourInfoString[idx]
            idx++
        }
    }

    fun WriteCommentFunc(nowtime : String, Rating : String){
        // @솔빈 2022-02-25 금
        // 댓글 작성 DB 입출력 로직 구현
        serviceForComment.writecomment("WriteComment", (activity as Activity).SelectedPlace.num, (activity as Activity).USER_CODE, (activity as Activity).USER_NAME, nowtime, edittxt.text.toString(), "Place", Rating).enqueue(object:
            Callback<Success> {
            override fun onFailure(call : Call<Success>, t : Throwable){
                Toast.makeText((activity as Activity),"네트워크 문제로 댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                var returndata = response.body()
                Toast.makeText((activity as Activity),"댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                LoadCommentList()
            }
        })
    }

    fun EditCommentFunc(nowtime : String, Rating: String){
        serviceForComment.editcomment("EditComment", editCommentNum,  nowtime, edittxt.text.toString(), Rating).enqueue(object :
            Callback<Success> {
            override fun onFailure(call: Call<Success>, t: Throwable) {
                Log.d("실패", t.toString())
                Toast.makeText((activity as Activity),"네트워크 문제로 댓글이 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                editFlag = 0
            }

            override fun onResponse(call: Call<Success>, response: Response<Success>) {
                Log.d("성공", "DB 입출력 성공")
                Toast.makeText((activity as Activity),"댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                var returndata = response.body()
                LoadCommentList()
                editFlag = 0
            }
        })
    }

}