package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels

import android.graphics.Bitmap
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction

data class InstructionDetail(
    val id:Long = 0,
    val orderNum:Int = 0,
    val text:String = "",
    val temperature:String = "",
    val stopTime:String = "",//mins
    val recipeId: Long = 0,
    val bitmap: Bitmap? = null
){
    fun toData():Instruction = Instruction(
        id = id,
        orderNum = orderNum,
        text = text,
        temperature = temperature.toIntOrNull() ?: 0,
        stopTime = stopTime.toFloatOrNull() ?: 0.0f,
        recipeId = recipeId,
        bitmap = bitmap
    )
}
fun Instruction.toUI():InstructionDetail = InstructionDetail(
    id = id,
    orderNum = orderNum,
    text = text,
    temperature = temperature.toString(),
    stopTime = stopTime.toString(),
    recipeId = recipeId,
    bitmap = bitmap
)