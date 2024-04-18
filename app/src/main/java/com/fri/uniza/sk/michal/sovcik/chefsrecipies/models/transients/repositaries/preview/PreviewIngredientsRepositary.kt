package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.InstructionsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewIngredientsRepositary : IngredientRepositary {
    private val list:MutableList<Ingredient> = mutableListOf()
    override suspend fun insert(item: Ingredient) {
        list.add(item)
    }

    override suspend fun update(item: Ingredient) {
        list.set(list.indexOf(list.first () {  it.id == item.id }),item)
    }

    override suspend fun delete(item: Ingredient) {
        list.remove(item)
    }

    override fun getIngredients(recipeId: Int): Flow<List<Ingredient>> {
        return flowOf(list.filter { it.recipeId == recipeId });
    }

}