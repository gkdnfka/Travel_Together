package com.example.capstone_design.Adapterset
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_design.Dataset.*
import com.example.capstone_design.Interfaceset.*
import com.example.capstone_design.R
import com.example.capstone_design.Util.*
import com.example.capstone_design.Util.PublicRetrofit.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class CommunityAdaptor(
    val context: Context,
    private val PostList: ArrayList<PostInfo>,
    var implemented: SetSeletedPostInfo,
    val userCode: String
) :
    RecyclerView.Adapter<CommunityAdaptor.ViewHolder>() {
    override fun getItemCount(): Int =  PostList.size

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.post_item_username)
        val content = view.findViewById<TextView>(R.id.post_item_content_preview)
        val title =  view.findViewById<TextView>(R.id.post_item_title)
        val image = view.findViewById<ImageView>(R.id.post_item_image)
        val date =  view.findViewById<TextView>(R.id.post_item_time)
        val scrollimg = view.findViewById<LinearLayout>(R.id.post_item_image_preview)
        val commentCount = view.findViewById<TextView>(R.id.post_item_comment_cnt)
        val bookmark = view.findViewById<ImageView>(R.id.post_item_bookmark)
        val likebtn = view.findViewById<ImageView>(R.id.post_item_like_button)
        val likenum = view.findViewById<TextView>(R.id.post_item_like_number)
        val tagLinear = view.findViewById<LinearLayout>(R.id.post_item_tag_Linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = PostList[position].username
        holder.content.text = PostList[position].content
        holder.title.text = PostList[position].title
        holder.date.text = PostList[position].dates
        holder.tagLinear.removeAllViews()

        GetBookmarkImage("FavoritePostList", holder.bookmark, PostList[position].number)
        holder.bookmark.setOnClickListener {
            addFavorite("FavoritePostList", PostList[position].number)
            GetBookmarkImage("FavoritePostList", holder.bookmark, PostList[position].number)
        }

        var service = PublicRetrofit.retrofit.create(LoadImage::class.java)
        var tmp = ImageInfoForLoad(PostList[position].usercode, "ProfileImages")
        service.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
            override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                Log.d("ImgLoadingObj", "이미지 출력 성공")
                var returndata = response.body()
                var byteArry = returndata?.data
                var tbitmap = BitmapFactory.decodeByteArray(byteArry, 0, byteArry!!.size)
                holder.image.setImageBitmap(tbitmap)
            }

            override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                Log.d("ImgLoadingObj", "이미지 출력 실패")
                t.printStackTrace()
            }
        })

        var commentservice = PublicRetrofit.retrofit.create(CommentInterface::class.java)
        commentservice.getCommentCount("CountComment", PostList[position].number, "Post").enqueue(
            object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    var returndata = response.body()
                    holder.commentCount.text = returndata
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Log.d("ImgLoadingObj", "이미지 출력 실패")
                    t.printStackTrace()
                }
            })

        var likeservice = PublicRetrofit.retrofit.create(LikeOperation::class.java)
        getLike(holder.likenum, PostList[position].number)
        holder.likebtn.setOnClickListener {
            val currentTime : Long = System.currentTimeMillis()
            val timeformat = SimpleDateFormat("yyyy-MM-dd")
            var nowtime = timeformat.format(currentTime).toString()
            likeservice.addLike("AddLike", PostList[position].number, userCode, nowtime).enqueue(
                object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        var retvalue = response.body()
                        if (retvalue == "1") holder.likenum.text =
                            ((holder.likenum.text).toString().toInt() + 1).toString()
                        else holder.likenum.text =
                            ((holder.likenum.text).toString().toInt() - 1).toString()
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        var data = parseCourse(PostList[position].course)
        holder.scrollimg.removeAllViews()

        var cnt = 0
        for (i in 0 until data.size){
            for (j in 0 until data[i].size){
                var newimage : ImageView = ImageView(context)
                cnt += 1
                if(cnt >= 4) break
                var tmp = ImageInfoForLoad(data[i][j], "PlaceImages")
                service.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
                    override fun onResponse(
                        call: Call<ImageInfo?>,
                        response: Response<ImageInfo?>
                    ) {
                        Log.d("ImgLoadingObj", "이미지 출력 성공")
                        var returndata = response.body()
                        var byteArry = returndata?.data
                        var tbitmap = BitmapFactory.decodeByteArray(byteArry, 0, byteArry!!.size)


                        var newimage = ImageView(context)
                        newimage.setImageBitmap(tbitmap)

                        val lp = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        lp.setMargins(0, 5, 5, 0)
                        newimage.layoutParams = lp
                        newimage.scaleType = ImageView.ScaleType.FIT_XY

                        holder.scrollimg.addView(newimage)
                    }

                    override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                        Log.d("ImgLoadingObj", "이미지 출력 실패")
                        t.printStackTrace()
                    }
                })
            }
        }

        var str : String = ""
        if(PostList[position].tags == null) str = "100,200,152,142,123"
        else str = PostList[position].tags

        var parsedTAG = str.split(",")

        var queryString = " WHERE "
        for (i in 0 until parsedTAG.size){
            queryString += " num=" + parsedTAG[i]
            if(i != parsedTAG.size-1) queryString += " or "
        }

        val serviceForTagDict = retrofit.create(GetTagDict::class.java)
        serviceForTagDict.getTagDict("GetTagDict", "load", queryString)
            .enqueue(object : Callback<ArrayList<TagDictSet>> {
                override fun onFailure(call: Call<ArrayList<TagDictSet>>, t: Throwable) {
                    Log.d("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<ArrayList<TagDictSet>>,
                    response: Response<ArrayList<TagDictSet>>
                ) {

                    var returndata = response.body()
                    if(returndata != null){
                        Log.d("태그", parsedTAG.toString())
                        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        if(returndata.size != 0)
                            for (i in 0 until returndata!!.size){
                                var view = inflater.inflate(R.layout.tag_item, holder.tagLinear, false)
                                view.findViewById<TextView>(R.id.tag_item_text).text =  "#"+ returndata!![i].name
                                holder.tagLinear.addView(view)
                            }
                    }
                }
            })

        holder.itemView.setOnClickListener {
            implemented.setSelectedPostInfo("ComminityPostDetail", PostList[position])
        }
    }
}

private fun parseCourse(course: String) : ArrayList<ArrayList<String>> {
    var course = course
    var PlaceList = ArrayList<ArrayList<String>>()

    var tmpStr = ""
    var tmpArryList = ArrayList<String>()
    // 문자열 파싱 로직
    for (i in 0 until course.length){
        if(course[i] != '/' && course[i] != ',') tmpStr += course[i]
        else{
            tmpArryList.add(tmpStr)
            tmpStr = ""
            if(course[i] == '/') {
                PlaceList.add(tmpArryList)
                tmpArryList = ArrayList<String>()
            }
        }

        if(i == course.length-1){
            tmpArryList.add(tmpStr)
            PlaceList.add(tmpArryList)
            tmpArryList = ArrayList<String>()
        }
    }

    return PlaceList
}

private fun getLike(textview: TextView, NoticeNum: String){
    var likeservice = PublicRetrofit.retrofit.create(LikeOperation::class.java)
    likeservice.getLikeCnt("GetLikeCnt", NoticeNum).enqueue(object : Callback<String?> {
        override fun onResponse(call: Call<String?>, response: Response<String?>) {
            var returndata = response.body()
            textview.text = returndata
        }

        override fun onFailure(call: Call<String?>, t: Throwable) {
            t.printStackTrace()
        }
    })
}