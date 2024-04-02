package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import kotlinx.coroutines.flow.Flow

//This is teoreticly unnecesary for my version but we can asume that we can potentially move data to online server than this becomes important
interface RecipeRepositary {
        /**
         *Retrievie all recipies by strream
         */
        fun getAllRecipiesStream(): Flow<List<Recipe>>

        /**
         * Retrieve an Recipe from that matches with the [name].
         */
        fun getRecipeStream(name: String): Flow<Recipe?>





        /**
         * Retrieve an Recipe from  that matches with the [id].
         */
        fun getRecipeStream(id: Int): Flow<Recipe?>

        /**
         * Insert recipe in the data source
         */
        suspend fun insertRecipe(recipe: Recipe)

        /**
         * Delete recipe from the data source
         */
        suspend fun deleteRecipe(recipe: Recipe)

        /**
         * Update recipe in the data source
         */
        suspend fun updateRecipe(recipe: Recipe)


}