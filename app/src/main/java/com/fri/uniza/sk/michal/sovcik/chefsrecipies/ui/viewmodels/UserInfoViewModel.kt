package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.App
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UserInfoViewModel(val pref:DataStore<Preferences>, val appContext: Context) : ViewModel() {


    var _userName = MutableStateFlow("Username")
    val userName = _userName.asStateFlow()
    init {
        viewModelScope.launch {
            pref.data.map {
                it[stringPreferencesKey("username")] ?: "UserName"
            }.collect{
                _userName.value = it
            }
        }
    }

    private var _profilePicture:MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    init {
        loadImage()
    }
    val profilePicture:StateFlow<Bitmap?>
        get() {
            loadImage()
            return _profilePicture.asStateFlow()

        }
    fun loadImage(){
        try {
            var file = File(appContext.filesDir,"profilePicture.png")
            file.createNewFile()
            val fs = FileInputStream(file)
            val bitmap = BitmapFactory.decodeStream(fs)
            fs.close()
            _profilePicture.value = bitmap

        }
        catch (e: IOException){
            Log.e("ProfilePicture","Cant load bitmap")
        }
    }
    init {
        loadPicture()
    }
    fun editUserName(name:String) {
        _userName.value = name
    }
    fun onDone(){
        viewModelScope.launch {
            pref.edit {
                it[stringPreferencesKey("username")] = userName.value;
            }
        }
    }
    fun saveProfilePicture(context: Context,bitmap: Bitmap) {
        try {
            var file = File(context.filesDir,"profilePicture.png")
            file.createNewFile()
            val fs = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fs)
            fs.close()
            _profilePicture.value = bitmap

        }
        catch (e: IOException){
            Log.e("ProfilePicture","Cant save bitmap")
        }
    }
    private fun loadPicture() {
        try {
            _profilePicture.value = BitmapFactory.decodeFile("profilePicture")
        }
        catch (e: IOException){
            Log.e("ProfilePicture","Cant save bitmap")
        }
    }
}

class UserInfoViewModelFactory(private val pref:DataStore<Preferences>, private val appContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(pref, appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}