package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients

data class DetailViewState(var isEditable: Boolean = false, var menuShown:Boolean = false, val newTagText:String = "",val selectedTabIndex:Int = 0,val dishTypeShowed:Boolean = false )
