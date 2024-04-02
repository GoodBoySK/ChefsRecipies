package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.IngredientsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.InstructionsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface InstructionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Instruction)
    @Update
    suspend fun update(item: Instruction)
    @Delete
    suspend fun delete(item: Instruction)
    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    fun getInstructions(recipeId: Int) : Flow<List<IngredientsOfRecipe>>
}