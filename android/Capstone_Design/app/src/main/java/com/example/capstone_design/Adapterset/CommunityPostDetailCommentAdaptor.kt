package com.example.capstone_design.Adapterset

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.CommentInfo
import com.example.capstone_design.Fragmentset.CommentModifyInterface
import com.example.capstone_design.R


class CommunityPostDetailCommentAdaptor(private val items: ArrayList<CommentInfo>, context : Context, userCode : String, commentModifyInterface : CommentModifyInterface) : RecyclerView.Adapter<CommunityPostDetailCommentAdaptor.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context
    var muserCode = userCode
    var mcommentModifyInterface = commentModifyInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.post_detail_comment_comment_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nametext.text = items[position].name
        holder.contenttext.text = items[position].content
        holder.datetext.text = items[position].dates

        // 자기 자신의 댓글이 아닐 경우, 수정 및 삭제 불가능 하도록
        if(items[position].user_number != muserCode)
            holder.morebtn.visibility = View.GONE
        else{
            // 리스트 형태의 다이얼로그
            var colorArray: Array<String> = arrayOf("삭제", "수정") // 리스트에 들어갈 Array
            holder.morebtn.setOnClickListener {
                val builder = AlertDialog.Builder(contexts)
                builder.setItems(colorArray,
                                DialogInterface.OnClickListener { dialog, which ->
                                    // 여기서 인자 'which'는 배열의 position을 나타냅니다.
                                    when(which){
                                        0 -> mcommentModifyInterface.DeleteComment(items[position].number)
                                        1 -> {
                                            mcommentModifyInterface.EditComment(holder.contenttext.text.toString(), items[position].number)
                                        }
                                    }
                                })
                // 다이얼로그를 띄워주기
                builder.show()
            }

        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var nametext: TextView = view.findViewById<TextView>(R.id.post_detail_comment_comment_item_name)
        var contenttext : TextView = view.findViewById<TextView>(R.id.post_detail_comment_comment_item_content)
        var morebtn : ImageView = view.findViewById<ImageView>(R.id.post_detail_comment_comment_item_more)
        var datetext = view.findViewById<TextView>(R.id.post_detail_comment_comment_item_date)
    }
}