package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.SearchDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.InstructionRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(val recipeRepositary: RecipeRepositary, val ingredientRepositary: IngredientRepositary) : ViewModel(){
    private var _uiState = MutableStateFlow(SearchDetail())
    val uiState = _uiState.asStateFlow()
    var tags = recipeRepositary.getAllTagsLike("").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        emptyList())
        var ingredient = ingredientRepositary.getIngredients("").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
            emptyList())
    var recipes = recipeRepositary.getAllRecipiesStream().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        emptyList())
    fun editState(newState:SearchDetail ) {

        _uiState.value = newState
    }
    fun editTextFilter(text:String){
        _uiState.value = uiState.value.copy(search = text)
        tags = recipeRepositary.getAllTagsLike(text).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
            emptyList())
        ingredient = ingredientRepositary.getIngredients(text).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
            emptyList())
    }
    fun addIngredientFilter(ingredient: Ingredient)
    {
        uiState.value.ingredients.add(ingredient)
        updateRecipe()
    }
    private fun updateRecipe()
    {
        recipes = recipeRepositary.getAllRecipies(uiState.value.tags.map { it.tag },uiState.value.ingredients.map { it.name }).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
            emptyList())
        _uiState.value = uiState.value.copy(search = "", expanded = false, recomp = !uiState.value.recomp)

    }
    fun removeIngredientFilter(ingredient: Ingredient)
    {
        uiState.value.ingredients.remove(ingredient)
        updateRecipe()
    }
    fun addTagFilter(tag: Tag)
    {
        uiState.value.tags.add(tag)
        updateRecipe()
    }
    fun removeTagFilter(tag: Tag)
    {
        uiState.value.tags.remove(tag)
        updateRecipe()
    }

}
class SearchViewModelFactory(private val repository: RecipeRepositary, private val ingredientRepositary: IngredientRepositary) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository,ingredientRepositary) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}