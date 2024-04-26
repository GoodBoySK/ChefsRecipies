package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels

import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag

data class SearchDetail(
    val tags:MutableList<Tag> = mutableListOf<Tag>(),
    val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>() ,
    val name:String = "",
    val search:String = "",
    val expanded:Boolean = false,
    val recomp:Boolean = false
){
}