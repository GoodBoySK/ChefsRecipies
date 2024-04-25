package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.InstructionDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.InstructionRepositary
import kotlinx.coroutines.flow.Flow

class OfflineInstructionRepositary(val instructionDao: InstructionDao) : InstructionRepositary {
    override suspend fun insert(item: Instruction) : Long{
        return instructionDao.insert(item)
    }

    override suspend fun update(item: Instruction) {
        instructionDao.update(item)
    }

    override suspend fun delete(item: Instruction) {
        instructionDao.delete(item)
    }

    override fun getInstructions(recipeId: Long): Flow<List<Instruction>> {
        return instructionDao.getInstructions(recipeId)
    }
}