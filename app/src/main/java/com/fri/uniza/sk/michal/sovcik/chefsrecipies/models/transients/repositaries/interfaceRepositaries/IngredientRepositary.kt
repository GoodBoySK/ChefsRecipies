package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepositary
{

    suspend fun insert(item: Ingredient) : Long
    suspend fun update(item: Ingredient)
    suspend fun delete(item: Ingredient)
    fun getIngredients(recipeId: Long) : Flow<List<Ingredient>>

    fun getIngredients(filter:String) : Flow<List<Ingredient>>



}