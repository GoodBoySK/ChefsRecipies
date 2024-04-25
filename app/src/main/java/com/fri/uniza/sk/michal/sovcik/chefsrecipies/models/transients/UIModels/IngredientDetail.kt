package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient

data class IngredientDetail(
    val id:Long = 0,
    val name:String = "",
    val amount: String = "",
    val measuremnts: String = "",
    val recipeId: Long = 0
) {
    fun toData():Ingredient = Ingredient(
        id = id,
        name = name,
        amount = amount.toFloatOrNull() ?: 0.0f,
        measuremnts = measuremnts,
        recipeId = recipeId
    )
}
fun Ingredient.toUI() : IngredientDetail = IngredientDetail(
    id = id,
    name = name,
    amount = amount.toString(),
    measuremnts = measuremnts,
    recipeId = recipeId
)