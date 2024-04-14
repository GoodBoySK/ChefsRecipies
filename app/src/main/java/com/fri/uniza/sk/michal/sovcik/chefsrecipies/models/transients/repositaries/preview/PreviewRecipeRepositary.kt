package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

public class PreviewRecipeRepositary : RecipeRepositary{
    private val list:MutableList<Recipe> = mutableListOf()
    override fun getAllRecipiesStream(): Flow<List<Recipe>> {
        return  flowOf(list)
    }

    override fun getRecipeStream(name: String): Flow<Recipe?> {
        return flowOf(list.single { recipe -> recipe.name == name})
    }

    override fun getRecipeStream(id: Int): Flow<Recipe?> {
        return flowOf(list.single { recipe -> recipe.id == id})

    }

    override fun getRecipeOfType(dishType: DishType): Flow<List<Recipe>> {
        return flowOf(list.filter { recipe -> recipe.dishType == dishType })
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        list.add(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        list.remove(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        list.set(list.indexOfFirst { _recipe -> _recipe.id == recipe.id },recipe)
    }
}