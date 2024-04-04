package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe

data class FilteredRecipes(
    val mainDishRecipes: List<Recipe> = emptyList(),
    val soupRecipes:List<Recipe> = emptyList(),
    val dezerRecipes:List<Recipe> = emptyList(),
    val breakfastRecipes:List<Recipe> = emptyList())
