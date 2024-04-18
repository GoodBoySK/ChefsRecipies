package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.InstructionsOfRecipe
import kotlinx.coroutines.flow.Flow

interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Ingredient)
    @Update
    suspend fun update(item: Ingredient)
    @Delete
    suspend fun delete(item: Ingredient)
    @Transaction
    @Query("SELECT * FROM Ingredient WHERE recipeId = :recipeId")
    fun getIngrendience(recipeId: Int) : Flow<List<Ingredient>>

}