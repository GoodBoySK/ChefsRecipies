package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?) : ByteArray
    {
        val outputStrem = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,100,outputStrem)
        return outputStrem.toByteArray()
    }
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?) : Bitmap?{
        if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        }
        return null;
    }
}