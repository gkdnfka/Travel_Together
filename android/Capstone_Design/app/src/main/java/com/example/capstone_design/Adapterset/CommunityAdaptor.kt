package com.example.capstone_design.Adapterset
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.PostInfo
import com.example.capstone_design.Interfaceset.SetSeletedPostInfo
import com.example.capstone_design.R


class CommunityAdaptor(val context: Context,private val PostList: ArrayList<PostInfo>, var implemented: SetSeletedPostInfo) :
    RecyclerView.Adapter<CommunityAdaptor.ViewHolder>() {
    override fun getItemCount(): Int =  PostList.size

    class ViewHolder(view :View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.post_item_username)
        val content = view.findViewById<TextView>(R.id.post_item_content_preview)
        val title =  view.findViewById<TextView>(R.id.post_item_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_item,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = PostList[position].username
        holder.content.text = PostList[position].content
        holder.title.text = PostList[position].title
        holder.itemView.setOnClickListener {
            implemented.setSelectedPostInfo("ComminityPostDetail", PostList[position])
        }
    }


}