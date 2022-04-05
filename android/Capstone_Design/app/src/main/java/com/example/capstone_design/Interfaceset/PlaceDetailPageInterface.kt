package com.example.capstone_design.Interfaceset

import android.graphics.Bitmap
import com.example.capstone_design.Dataset.PlaceInfo

interface PlaceDetailPageInterface {
    fun change(index : Int, placeinfo : PlaceInfo, bitmap : Bitmap?)
}