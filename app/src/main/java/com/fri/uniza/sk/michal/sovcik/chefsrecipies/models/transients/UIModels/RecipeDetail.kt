package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels

import android.graphics.Bitmap
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe

data class RecipeDetail(
    val id:Long = 0,
    val name:String = "",
    val dishType: DishType = DishType.Soup,
    val time:String = "",
    val portions:String = "",
    val rating: String = "",
    val autor: String = "",
    val description: String = "",

){
    fun toData() : Recipe = Recipe(
        id = id,
        name = name,
        dishType = dishType,
        time = time.toIntOrNull() ?: 0,
        portions = portions.toIntOrNull() ?: 0,
        rating = rating.toFloatOrNull() ?: 0.0f,
        autor = autor,
        description = description,


    )

}

fun Recipe.toUI():RecipeDetail = RecipeDetail(
    id = id,
    name = name,
    dishType = dishType,
    time = time.toString(),
    portions = portions.toString(),
    rating = rating.toString(),
    autor = autor,
    description = description,

)