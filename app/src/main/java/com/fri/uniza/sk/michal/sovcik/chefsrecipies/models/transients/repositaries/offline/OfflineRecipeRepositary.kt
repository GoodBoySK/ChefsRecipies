package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.RecipeDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.TagDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow

class OfflineRecipeRepositary(private val recipeDao:RecipeDao, private val tagDao: TagDao) : RecipeRepositary {
    override fun getAllRecipiesStream(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipies()
    }

    override fun getAllTags(id:Long): Flow<List<Tag>> {
        return tagDao.getTags(id)
    }

    override fun getRecipe(name: String): Flow<Recipe> {
        return recipeDao.getRecipe(name)
    }

    override fun getRecipe(id: Long): Flow<Recipe> {
        return recipeDao.getRecipe(id)
    }

    override fun getRecipeOfType(dishType: DishType): Flow<List<Recipe>> {
        return recipeDao.getAllRecipies(dishType)
    }

    override suspend fun insertRecipe(recipe: Recipe) : Long{
        return recipeDao.insert(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    override suspend fun insertTag(tag: Tag){
        return tagDao.insert(tag)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.delete(tag)
    }


}