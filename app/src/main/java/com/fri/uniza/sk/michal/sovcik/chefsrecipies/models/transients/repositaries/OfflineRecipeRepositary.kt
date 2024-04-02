package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.RecipeDao
import kotlinx.coroutines.flow.Flow

class OfflineRecipeRepositary(private val recipeDao:RecipeDao) : RecipeRepositary {
    override fun getAllRecipiesStream(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipies();
    }

    override fun getRecipeStream(name: String): Flow<Recipe?> {
        return recipeDao.getRecipe(name)
    }

    override fun getRecipeStream(id: Int): Flow<Recipe?> {
        return recipeDao.getRecipe(id)
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.update(recipe)
    }


}