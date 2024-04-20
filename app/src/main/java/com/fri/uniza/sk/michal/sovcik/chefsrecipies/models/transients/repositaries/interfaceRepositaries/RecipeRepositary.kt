package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import kotlinx.coroutines.flow.Flow

//This is teoreticly unnecesary for my version but we can asume that we can potentially move data to online server than this becomes important
interface RecipeRepositary {
        /**
         *Retrievie all recipies by strream
         */
        fun getAllRecipiesStream(): Flow<List<Recipe>>



        fun getAllTags(id: Long) : List<Tag>


        /**
         * Retrieve an Recipe from that matches with the [name].
         */
        fun getRecipe(name: String): Recipe?



        fun getRecipeOfType(dishType: DishType): Flow<List<Recipe>>

        /**
         * Retrieve an Recipe from  that matches with the [id].
         */
        fun getRecipe(id: Long): Recipe?

        /**
         * Insert recipe in the data source
         */
        suspend fun insertRecipe(recipe: Recipe) : Long

        /**
         * Delete recipe from the data source
         */
        suspend fun deleteRecipe(recipe: Recipe)

        /**
         * Update recipe in the data source
         */
        suspend fun updateRecipe(recipe: Recipe)

        /**
         * Insert recipe in the data source
         */
        suspend fun insertTag(tag: Tag)

        /**
         * Delete recipe from the data source
         */
        suspend fun deleteTag(tag: Tag)

        /**
         * Update recipe in the data source
         */


}