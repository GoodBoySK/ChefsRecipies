package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
    import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.FilteredRecipes
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(val recipeRepositary: RecipeRepositary) : ViewModel() {
    private val _recipesFilter:StateFlow<FilteredRecipes> = MutableStateFlow(FilteredRecipes())
    private val _maindishFlowRecipes: StateFlow<List<Recipe>>
    private val _dezerFlowRecipes: StateFlow<List<Recipe>>
    private val _soupFlowRecipes: StateFlow<List<Recipe>>
    private val _breakfastFlowRecipes: StateFlow<List<Recipe>>

    init {
        _maindishFlowRecipes = recipeRepositary.getRecipeOfType(DishType.MainDish).stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(100), emptyList())
        _dezerFlowRecipes = recipeRepositary.getRecipeOfType(DishType.Dezert).stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(100), emptyList())
        _soupFlowRecipes = recipeRepositary.getRecipeOfType(DishType.Soup).stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(100), emptyList())
        _breakfastFlowRecipes = recipeRepositary.getRecipeOfType(DishType.Breakfast).stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(100), emptyList())




    }
    val recipesFilter = combine(_recipesFilter,_maindishFlowRecipes,_dezerFlowRecipes,_soupFlowRecipes,_breakfastFlowRecipes) {
            filteredRecipes, maindish, dezret, soup, breakfast ->
        filteredRecipes.copy(mainDishRecipes = maindish, dezerRecipes = dezret, soupRecipes = soup, breakfastRecipes = breakfast)

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(100),FilteredRecipes())


    fun getAllRecipeOfType(dishType: DishType): Flow<List<Recipe>> {
        val recipies = recipeRepositary.getRecipeOfType(dishType)
        return recipies
    }

}

class HomeViewModelFactory(private val repository: RecipeRepositary) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}