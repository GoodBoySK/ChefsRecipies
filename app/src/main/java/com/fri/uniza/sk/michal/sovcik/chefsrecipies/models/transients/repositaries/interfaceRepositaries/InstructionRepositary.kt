package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import kotlinx.coroutines.flow.Flow

interface InstructionRepositary {
    suspend fun insert(item: Instruction) : Long
    suspend fun update(item: Instruction)
    suspend fun delete(item: Instruction)
    fun getInstructions(recipeId: Long) : Flow<List<Instruction>>
}