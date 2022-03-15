package com.example.capstone_design.Fragmentset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Adapterset.CommunityPostDetailCommentAdaptor
import com.example.capstone_design.Dataset.CommentInfo
import com.example.capstone_design.R
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.SimpleDateFormat

interface CommentModifyInterface{
    fun DeleteComment(CommentNum: String)
    fun EditComment(newContent : String, commentNum : String)
}

// @솔빈 2022-02-22 (목)
// CommentFragment -> 게시글에 달린 댓글을 출력하는 프래그먼트
class CommentFragment(Activity : Activity) : Fragment()
{
    var commentInfoList = ArrayList<CommentInfo>()
    var mactivity = Activity
    // @ 솔빈 2022-02-26 토
    // serviceForComment -> 댓글에 대한 DB 입출력을 위한 모든 함수들을 담고있는 인터페이스 객체
    val serviceForComment = mactivity.retrofit.create(CommentInterface::class.java)

    lateinit var commentRecycler : RecyclerView
    lateinit var edittxt : EditText
    var editFlag = 0
    var editCommentNum = "0"

    // @솔빈 2022-02-26 토
    // DB 통신 실패 성공 여부 판단을 위한 dataclass
    data class Success(
        @SerializedName("number")
        var number : String
    )

    // @ 솔빈 2022-02-26 토
    // service(DB 입출력) 을 위한 인터페이스 선언
    // GetCommentInfo -> 댓글 목록 요청(인자 => 게시글 번호)
    // WriteComment -> 댓글 추가 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)
    // DeleteComment -> 댓글 삭제 요청(인자 => 댓글 번호)
    // EditComment   -> 댓글 수정 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)
    interface CommentInterface{
        @GET("func={func}/number={number}")
        fun getCommentInfo(@Path("func") func : String, @Path("number") number : String): Call<ArrayList<CommentInfo>>

        @GET("func={func}/NoticeNum={NoticeNum}/UserCode={UserCode}/UserName={UserName}/Dates={Dates}/Content={Content}")
        fun writecomment(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String, @Path("UserCode") UserCode : String, @Path("UserName") UserName : String, @Path("Dates") Dates : String, @Path("Content") Content : String): Call<Success>

        @GET("func={func}/CommentNum={CommentNum}")
        fun deletecomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String): Call<Success>

        @GET("func={func}/CommentNum={CommentNum}/Dates={Dates}/Content={Content}")
        fun editcomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String, @Path("Dates") Dates : String, @Path("Content") Content : String): Call<Success>
    }


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
    }

    // @솔빈 2022-02-25 금
    // 해당 게시글에 속하는 댓글들을 데이터베이스에서 불러와서 commentInfoList에 저장하고
    // adaptor을 붙여주는 동작을 수행하는 함수
    fun LoadCommentList(){
        serviceForComment.getCommentInfo("SearchComment", (activity as Activity).SelectedPostInfo.number.toString()).enqueue(object:
            Callback<ArrayList<CommentInfo>> {
            override fun onFailure(call: Call<ArrayList<CommentInfo>>, t: Throwable) {
                Log.d("실패", t.toString())
            }

            override fun onResponse(call: Call<ArrayList<CommentInfo>>, response: Response<ArrayList<CommentInfo>>) {
                Log.d("성공", "DB 입출력 성공")
                var returndata = response.body()
                commentInfoList = returndata!!
                commentRecycler.adapter = CommunityPostDetailCommentAdaptor(commentInfoList, (activity as Activity), (activity as Activity).USER_CODE, commentModifyInterface)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var view = inflater.inflate(R.layout.post_detail_comment_item, container, false)
        var btn = view.findViewById<Button>(R.id.post_detail_comment_add_button)
        edittxt = view.findViewById<EditText>(R.id.post_detail_comment_edit)

        // @솔빈 2022-02-25 금
        // commentRecyler 초기화 한 다음
        // LoadCommentList() 호출
        commentRecycler = view.findViewById<RecyclerView>(R.id.post_detail_comment_recycler)
        LoadCommentList()

        btn.setOnClickListener {
            val currentTime : Long = System.currentTimeMillis()
            val timeformat = SimpleDateFormat("yyyy-MM-dd")
            var nowtime = timeformat.format(currentTime).toString()

            if(editFlag == 0){
                // @솔빈 2022-02-25 금
                // 댓글 작성 DB 입출력 로직 구현
                serviceForComment.writecomment("WriteComment", (activity as Activity).SelectedPostInfo.number, (activity as Activity).USER_CODE, (activity as Activity).USER_NAME, nowtime, edittxt.text.toString()).enqueue(object:
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
            else{
                serviceForComment.editcomment("EditComment", editCommentNum,  nowtime, edittxt.text.toString()).enqueue(object :
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
        return view
    }
}