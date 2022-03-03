package com.example.capstone_design
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.function.Function

class Post_Plan_Detail_Adapter(val context: Context, val PlaceList: MutableList<PlaceInfo>, val updateListInterface: update_list_interface):
    RecyclerView.Adapter<Post_Plan_Detail_Adapter.ViewHolder>(),
    ItemTouchHelperCallback.OnItemMoveListener {
    class ViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.detail_item_list, parent, false)) {
        val detail_item_name: TextView = itemView.findViewById(R.id.detail_item_name)
        val drag_image: ImageView = itemView.findViewById(R.id.drag_image)
    }

    private lateinit var dragListener: OnStartDragListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Post_Plan_Detail_Adapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(com.example.capstone_design.R.layout.detail_item_list, null)
        return ViewHolder(parent)
    }
    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return PlaceList.size
    }
    // (3) View에 내용 입력
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        PlaceList[position].let {
            with(holder) {
               detail_item_name.text = it.name
                drag_image.setOnTouchListener { view, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    return@setOnTouchListener false
                }
            }
        }
    }
    // (4) 레이아웃 내 View 연결

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
    fun startDrag(listener: OnStartDragListener) {
        this.dragListener = listener
    }
    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(PlaceList, fromPosition, toPosition)
        updateListInterface.UpdateList()
        notifyItemMoved(fromPosition, toPosition)
    }
    override fun onItemSwiped(position: Int) {
        PlaceList.removeAt(position)
        updateListInterface.UpdateList()
        notifyItemRemoved(position)
    }

}