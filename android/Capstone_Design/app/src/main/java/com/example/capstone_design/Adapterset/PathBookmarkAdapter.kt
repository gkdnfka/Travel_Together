import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Bookmark
import com.example.capstone_design.Interfaceset.changeDay
import com.example.capstone_design.R
import com.example.capstone_design.Util.FavoriteAddManager
import com.example.capstone_design.Util.parseFavorite

class PathBookmarkAdapter(private val items: ArrayList<String>, context : Context, val interfaces : Bookmark.pathbookmarkinterface) : RecyclerView.Adapter<PathBookmarkAdapter.ViewHolder>() {
    override fun getItemCount(): Int = items.size
    var contexts = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathBookmarkAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(contexts).inflate(R.layout.path_bookmark_adapter_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: PathBookmarkAdapter.ViewHolder, position: Int) {
        var arry = parseFavorite("pathName" + items[position])
        var texts = ""
        for (i in 0 until arry.size){
            texts += arry[i]
            if(i != arry.size-1) texts += " -> "
        }
        holder.pathtext.text = texts
        holder.numbertext.text =  "경로 "+ (position.toInt() + 1).toString()

        holder.itemView.setOnClickListener {
            interfaces.change(items[position])
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var numbertext: TextView = view.findViewById<TextView>(R.id.path_bookmark_adapter_item_pathnumber)
        var pathtext: TextView = view.findViewById<TextView>(R.id.path_bookmark_adapter_item_pathtext)
    }
}