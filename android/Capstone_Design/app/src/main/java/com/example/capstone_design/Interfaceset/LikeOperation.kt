package com.example.capstone_design.Interfaceset

import com.example.capstone_design.Dataset.CommentInfo
import com.example.capstone_design.Dataset.Success
import com.example.capstone_design.Fragmentset.CommentFragment
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// @ 솔빈 2022-02-26 토
// service(DB 입출력) 을 위한 인터페이스 선언
// GetCommentInfo -> 댓글 목록 요청(인자 => 게시글 번호)
// WriteComment -> 댓글 추가 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)
// DeleteComment -> 댓글 삭제 요청(인자 => 댓글 번호)
// EditComment   -> 댓글 수정 요청(인자 => 게시글 번호, 유저 코드, 유저 이름, 날짜, 내용)
interface LikeOperation{
    @GET("func={func}/NoticeNum={NoticeNum}")
    fun getLikeCnt(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String): Call<String>

    @GET("func={func}/NoticeNum={NoticeNum}/UserCode={UserCode}/Dates={Dates}")
    fun addLike(@Path("func") func : String, @Path("NoticeNum") NoticeNum : String, @Path("UserCode") UserCode:String , @Path("Dates") Dates:String): Call<String>
}
