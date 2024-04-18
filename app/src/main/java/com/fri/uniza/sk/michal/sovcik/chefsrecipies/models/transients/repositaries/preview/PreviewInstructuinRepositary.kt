package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.IngredientsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.InstructionRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewInstructuinRepositary : InstructionRepositary {
    private val list:MutableList<Instruction> = mutableListOf()

    override suspend fun insert(item: Instruction) {
        list.add(item)
    }

    override suspend fun update(item: Instruction) {
        list.set(list.indexOf(list.first () {  it.id == item.id }),item)
    }

    override suspend fun delete(item: Instruction) {
        list.remove(item)
    }

    override fun getInstructions(recipeId: Int): Flow<List<Instruction>> {
        return flowOf(list.filter { it.recipeId == recipeId });
    }
}