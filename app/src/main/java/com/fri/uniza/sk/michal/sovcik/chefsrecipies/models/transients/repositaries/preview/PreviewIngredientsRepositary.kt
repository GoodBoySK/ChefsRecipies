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
    init {
        list.add(Ingredient(0,"Flour",400f,"gssssss",0))
        list.add(Ingredient(1,"Egg",3f,"ks",0))
        list.add(Ingredient(2,"Sugar",200f,"g",0))
    }
    override suspend fun insert(item: Ingredient) : Long{
        list.add(item)
        return item.id
    }

    override suspend fun update(item: Ingredient) {
        list.set(list.indexOf(list.first {  it.id == item.id }),item)
    }

    override suspend fun delete(item: Ingredient) {
        list.remove(item)
    }

    override fun getIngredients(recipeId: Long): List<Ingredient> {
        return list.filter { it.recipeId == recipeId }
    }

}