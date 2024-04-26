package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.IngredientDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import kotlinx.coroutines.flow.Flow

class OfflineIngredinetRepositary(val ingredientDao: IngredientDao) : IngredientRepositary {
    override suspend fun insert(item: Ingredient) : Long {
        return ingredientDao.insert(item)
    }

    override suspend fun update(item: Ingredient) {
        ingredientDao.update(item)
    }

    override suspend fun delete(item: Ingredient) {
        ingredientDao.delete(item)
    }

    override fun getIngredients(recipeId: Long): Flow<List<Ingredient>> {
        return ingredientDao.getIngrendience(recipeId)
    }

    override fun getIngredients(filter: String): Flow<List<Ingredient>> {
        return ingredientDao.getIngrendience("%$filter%")
    }
}