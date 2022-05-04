package com.example.capstone_design.Interfaceset

interface CommentModifyInterface{
    fun DeleteComment(CommentNum: String)
    fun EditComment(newContent : String, commentNum : String)
    fun ChangeScore(commentNum : String)
}