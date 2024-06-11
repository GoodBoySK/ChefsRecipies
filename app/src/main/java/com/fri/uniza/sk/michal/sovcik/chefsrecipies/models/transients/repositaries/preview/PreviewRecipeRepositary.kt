package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewRecipeRepositary : RecipeRepositary{
    private val list:MutableList<Recipe> = mutableListOf()
    private val tagList:MutableList<Tag> = mutableListOf()
    init {
        list.add(Recipe(1,"Cookies",DishType.Dezert,30,15,4.5f,"Michal","This cookies will blow you mind!!!!"))
        tagList.add(Tag(1,1, "Sugar"))
        tagList.add(Tag(2,1, "Milk"))
        tagList.add(Tag(3,1, "Sweat"))
        tagList.add(Tag(4,1, "American"))
    }
    override fun getAllRecipiesStream(): Flow<List<Recipe>> {
        return  flowOf(list)
    }

    override fun getAllTags(id: Long): Flow<List<Tag>> {
        return flowOf(tagList.toList())
    }

    override fun getAllTagsLike(string: String): Flow<List<Tag>> {
        return flowOf(
            tagList.filter {
                it.tag.contains(string)
            }
        )
    }

    override fun getAllRecipies(
        tags: List<String>,
        ingredients: List<String>
    ): Flow<List<Recipe>> {
        return flowOf(list)
    }

    override fun getRecipe(name: String): Flow<Recipe> {
        return flowOf(list.filter {it.name == name}.first())
    }

    override fun getRecipe(id: Long): Flow<Recipe> {
        return flowOf(list.filter {it.id == id}.first())
    }

    override fun getRecipeOfType(dishType: DishType): Flow<List<Recipe>> {
        return flowOf(list.filter { recipe -> recipe.dishType == dishType })
    }

    override suspend fun insertRecipe(recipe: Recipe) : Long{
        list.add(recipe)
        return recipe.id
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        list.remove(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        list.set(list.indexOfFirst { _recipe -> _recipe.id == recipe.id },recipe)
    }

    override suspend fun insertTag(tag: Tag)  {
        tagList.add(tag)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagList.remove(tag)
    }

}