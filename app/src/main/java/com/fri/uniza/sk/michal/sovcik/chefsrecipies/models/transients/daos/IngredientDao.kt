package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Ingredient) : Long
    @Update
    suspend fun update(item: Ingredient)
    @Delete
    suspend fun delete(item: Ingredient)
    @Transaction
    @Query("SELECT * FROM Ingredient WHERE recipeId = :recipeId")
    fun getIngrendience(recipeId: Long) : Flow<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM Ingredient WHERE name COLLATE NOCASE LIKE :filter GROUP BY name")
    fun getIngrendience(filter:String) : Flow<List<Ingredient>>

}