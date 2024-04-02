package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe:Recipe)
    @Update
    suspend fun update(recipe:Recipe)

    @Delete
    suspend fun delete(recipe:Recipe)

    @Query("SELECT * from recipe where name = :name limit 1")
    fun getRecipe(name:String) : Flow<Recipe?>
    @Query("SELECT * from recipe where id = :id")
    fun getRecipe(id:Int) : Flow<Recipe?>

    @Query("SELECT * from recipe ORDER BY name ASC")
    fun getAllRecipies(): Flow<List<Recipe>>

}