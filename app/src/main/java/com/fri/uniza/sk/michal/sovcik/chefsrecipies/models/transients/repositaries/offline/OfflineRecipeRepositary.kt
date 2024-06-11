package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.IngredientDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.InstructionDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.RecipeDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.TagDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach

class OfflineRecipeRepositary(private val recipeDao:RecipeDao, private val tagDao: TagDao, private  val ingredientDao: IngredientDao, private val instructionDao: InstructionDao) : RecipeRepositary {
    override fun getAllRecipiesStream(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipies()
    }

    override fun getAllTags(id:Long): Flow<List<Tag>> {
        return tagDao.getTags(id)
    }

    override fun getAllTagsLike(filter: String): Flow<List<Tag>> {
        return tagDao.getAllTags("%$filter%")
    }

    override fun getAllRecipies(
        tags: List<String>,
        ingredients: List<String>
    ): Flow<List<Recipe>> {
        if (tags.isEmpty() && ingredients.isEmpty()) return recipeDao.getAllRecipies()
        if(tags.isEmpty() && ingredients.isNotEmpty()) return recipeDao.getAllRecipiesIngredients(ingredients = ingredients)
        if(tags.isNotEmpty() && ingredients.isEmpty()) return recipeDao.getAllRecipiesTags(tags = tags)

        return recipeDao.getAllRecipies(tags,ingredients)
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

        tagDao.getTags(recipe.id).first{
            it.forEach {
                tagDao.delete(it)
            }
            true
        }
        ingredientDao.getIngrendience(recipe.id).first{
            it.forEach {
                ingredientDao.delete(it)
            }
            true
        }
        instructionDao.getInstructions(recipe.id).first{
            it.forEach {
                instructionDao.delete(it)
            }
            true
        }
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