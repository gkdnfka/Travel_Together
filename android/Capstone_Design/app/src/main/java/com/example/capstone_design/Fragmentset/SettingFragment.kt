package com.example.capstone_design.Fragmentset

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone_design.Activityset.Activity
import com.example.capstone_design.Dataset.ImageInfo
import com.example.capstone_design.Dataset.ImageInfoForLoad
import com.example.capstone_design.Interfaceset.LoadImage
import com.example.capstone_design.Interfaceset.UploadImage
import com.example.capstone_design.R
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.io.*


class SettingFragment : Fragment()
{
    data class success(
        @SerializedName("number")
        var number: String
    )




    lateinit var profileImage : ImageView
    lateinit var loadImage : ImageView
    lateinit var bitmapfile : Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.setting_fragment, container, false)
        profileImage = view.findViewById<ImageView>(R.id.setting_fragment_image)
        loadImage = view.findViewById<ImageView>(R.id.setting_fragment_image2)

        var btn = view.findViewById<Button>(R.id.setting_fragment_btn)
        var sendbtn = view.findViewById<Button>(R.id.setting_fragment_sendbtn)
        var loadbtn = view.findViewById<Button>(R.id.setting_fragment_loadbtn)

        val service = (activity as Activity).retrofit.create(UploadImage::class.java)
        val service_for_load = (activity as Activity).retrofit.create(LoadImage::class.java)


        btn.setOnClickListener {
            openGallery()
        }

        sendbtn.setOnClickListener {
            if(bitmapfile != null) {
                try {
                    val filesDir: File = (activity as Activity).getFilesDir()
                    val file = File(filesDir, (activity as Activity).USER_CODE.toString() + "_" + "Profile" + ".jpg")
                    val bos = ByteArrayOutputStream()
                    bitmapfile.compress(Bitmap.CompressFormat.PNG, 0, bos)
                    val bitmapdata: ByteArray = bos.toByteArray()

                    val fos = FileOutputStream(file)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()

                    val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
                    val name = RequestBody.create(MediaType.parse("text/plain"), "upload")

                    service.uploadImage(body, name).enqueue(object : Callback<success?> {
                        override fun onResponse(call: Call<success?>, response: Response<success?>) {

                        }
                        override fun onFailure(call: Call<success?>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        loadbtn.setOnClickListener {
            var tmp = ImageInfoForLoad((activity as Activity).USER_CODE.toString(), "ProfileImages")
            service_for_load.loadImage(tmp).enqueue(object : Callback<ImageInfo?> {
                override fun onResponse(call: Call<ImageInfo?>, response: Response<ImageInfo?>) {
                    Log.d("ImgLoading", "이미지 출력 성공")
                    var returndata = response.body()
                    var byteArry = returndata?.data
                    var tbitmap = byteArry?.let { it1 -> BitmapFactory.decodeByteArray( byteArry, 0, it1.size) }
                    loadImage.setImageBitmap(tbitmap)
                }
                override fun onFailure(call: Call<ImageInfo?>, t: Throwable) {
                    Log.d("ImgLoading", "이미지 출력 실패")
                    t.printStackTrace()
                }
            })
        }

        return view
    }

    private fun openGallery(){
        var intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 1)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                var clipData = data?.clipData
                var currentImageUrl : Uri? = data?.data

                try{
                    val tempUri: Uri
                    tempUri = clipData!!.getItemAt(0).uri
                    val bitmap = MediaStore.Images.Media.getBitmap((activity as Activity).contentResolver, currentImageUrl)
                    bitmapfile = bitmap
                    profileImage.setImageBitmap(bitmap)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}