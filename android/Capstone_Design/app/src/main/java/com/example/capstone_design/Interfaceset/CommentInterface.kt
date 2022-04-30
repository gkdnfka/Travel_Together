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
interface CommentInterface{
    @GET("func={func}/number={number}/CommentType={CommentType}")
    fun getCommentInfo(@Path("func") func : String, @Path("number") number : String, @Path("CommentType") CommentType:String): Call<ArrayList<CommentInfo>>

    @GET("func={func}/number={number}/CommentType={CommentType}")
    fun getCommentCount(@Path("func") func : String, @Path("number") number : String, @Path("CommentType") CommentType:String): Call<String>

    @GET("func={func}/UniqueNum={UniqueNum}/UserCode={UserCode}/UserName={UserName}/Dates={Dates}/Content={Content}/CommentType={CommentType}/Rating={Rating}")
    fun writecomment(@Path("func") func : String, @Path("UniqueNum") NoticeNum : String, @Path("UserCode") UserCode : String, @Path("UserName") UserName : String, @Path("Dates") Dates : String, @Path("Content") Content : String, @Path("CommentType") CommentType:String, @Path("Rating") Rating:String): Call<Success>

    @GET("func={func}/CommentNum={CommentNum}")
    fun deletecomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String ): Call<Success>

    @GET("func={func}/CommentNum={CommentNum}/Dates={Dates}/Content={Content}/Rating={Rating}")
    fun editcomment(@Path("func") func : String, @Path("CommentNum") CommentNum : String, @Path("Dates") Dates : String, @Path("Content") Content : String, @Path("Rating") Rating: String): Call<Success>
}
