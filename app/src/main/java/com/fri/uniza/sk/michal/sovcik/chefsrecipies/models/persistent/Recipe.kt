package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation


enum class DishType{
    MainDish,
    Breakfast,
    Dezert,
    Soup
}

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val name:String = "",
    val dishType: DishType = DishType.Soup,
    val time:Int = 0,
    val portions:Int = 0,
    val rating: Float = 0f,
    val autor: String = "",
    val description: String = "",
    val bitmap: Bitmap? = null
    )


@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Long,
    val tag:String
)
data class TagsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Tag>
)



