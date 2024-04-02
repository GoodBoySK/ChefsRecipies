package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.InstructionsOfRecipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.IngredientDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import kotlinx.coroutines.flow.Flow

class OfflineIngredientRepositary(val ingredientDao: IngredientDao) : IngredientRepositary {
    override suspend fun insert(item: Ingredient) {
        ingredientDao.insert(item)
    }

    override suspend fun update(item: Ingredient) {
        ingredientDao.update(item)
    }

    override suspend fun delete(item: Ingredient) {
        ingredientDao.delete(item)
    }

    override fun getInstructions(recipeId: Int): Flow<List<InstructionsOfRecipe>> {
       return ingredientDao.getInstructions(recipeId)
    }
}