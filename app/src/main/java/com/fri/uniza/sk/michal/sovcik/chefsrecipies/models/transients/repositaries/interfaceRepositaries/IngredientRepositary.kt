package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.InstructionsOfRecipe
import kotlinx.coroutines.flow.Flow

interface IngredientRepositary
{

    suspend fun insert(item: Ingredient) : Long
    suspend fun update(item: Ingredient)
    suspend fun delete(item: Ingredient)
    fun getIngredients(recipeId: Long) : Flow<List<Ingredient>>

}