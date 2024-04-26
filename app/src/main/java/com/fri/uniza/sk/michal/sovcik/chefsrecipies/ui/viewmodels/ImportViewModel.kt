package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImportViewModel : ViewModel() {
    private var _fieldText = MutableStateFlow("")
    val fieldText = _fieldText.asStateFlow()

    fun textChanged(text:String){
        _fieldText.value = text;
    }
}