package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow

class RecipeDetailViewModel(val recipeRepositary: RecipeRepositary) : ViewModel() {


    public fun getRecipe(id:Int): Flow<Recipe?>
    {
        return recipeRepositary.getRecipeStream(id)

    }



}
class RecipeViewModelFactory(private val repository: RecipeRepositary) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}