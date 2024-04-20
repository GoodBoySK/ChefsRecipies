package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.IngredientsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.IngredientDao
import kotlinx.coroutines.flow.Flow

interface InstructionRepositary {
    suspend fun insert(item: Instruction) : Long
    suspend fun update(item: Instruction)
    suspend fun delete(item: Instruction)
    fun getInstructions(recipeId: Long) : List<Instruction>
}