package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Instruction(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val orderNum:Int,
    val text:String,
    val temperature:Int,
    val stopTime:Float,//mins
    val recipeId: Long,
    val bitmap: Bitmap?
)

data class InstructionsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Instruction>
)