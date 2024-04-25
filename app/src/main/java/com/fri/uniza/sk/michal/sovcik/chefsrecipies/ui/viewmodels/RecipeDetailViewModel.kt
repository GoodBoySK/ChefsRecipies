package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.DetailViewState
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.StopwatchService
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.IngredientDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.InstructionDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.RecipeDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.toUI
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.InstructionRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RecipeDetailViewModel(val recipeRepositary: RecipeRepositary, val ingredientRepositary: IngredientRepositary, val instructionRepositary: InstructionRepositary,private val navController: NavController,private var id: Long,val context: Context) : ViewModel() {

    private var _uiState = MutableStateFlow(DetailViewState(false,false,"",0))
    val uiState = _uiState.asStateFlow()
    init {
        if (id == 0.toLong())
        {
            runBlocking {
                val newid =  recipeRepositary.insertRecipe(Recipe())
                recipeRepositary.updateRecipe(Recipe(id = newid, name = "NewRecipe$newid"))
                id = newid;
                _uiState.value = _uiState.value.copy(isEditable = true)
            }

        }
    }

    val recipeFlow = recipeRepositary.getRecipe(id)
    //Get data from database -> cast them to new object so new instance is created -> load it
    private var _recipeState = MutableStateFlow(RecipeDetail())
    val recipeState:StateFlow<RecipeDetail> = _recipeState.asStateFlow()
    init {
        viewModelScope.launch {
            recipeFlow.collect {
                _recipeState.value = it.toUI()
            }
        }
    }


    private val ingredientFlow = ingredientRepositary.getIngredients(id)
    //Get data from database -> cast them to new object so new instance is created -> load it
    private var _ingredientsState = MutableStateFlow(emptyList<IngredientDetail>())
    val ingredientsState:StateFlow<List<IngredientDetail>> = _ingredientsState.asStateFlow()
    init {
        viewModelScope.launch {
            ingredientFlow.collect{
                _ingredientsState.value = it.map {ingredient -> ingredient.toUI() }
            }
        }
    }


    val instructionFlow = instructionRepositary.getInstructions(id)
    //Get data from database -> cast them to new object so new instance is created -> load it
    private var _instructionState = MutableStateFlow(emptyList<InstructionDetail>())
    val instructionState:StateFlow<List<InstructionDetail>> = _instructionState.asStateFlow()
    init {
        viewModelScope.launch {
            instructionFlow.collect{list ->
                _instructionState.value = list.map { it.toUI() }

            }
        }
    }


    private val tagflow = recipeRepositary.getAllTags(id)
    //Get data from database -> cast them to new object so new instance is created -> load it
    private var _tagsState = MutableStateFlow(emptyList<Tag>())
    val tagState:StateFlow<List<Tag>> = _tagsState.asStateFlow()
    init {
        viewModelScope.launch {
            tagflow.collect {list ->
                _tagsState.value = list
            }
        }
    }





    fun editRecipe(recipe: RecipeDetail) {

        if (uiState.value.isEditable)
            _recipeState.value = recipe
    }
    fun saveRecipe() {
        //This will work bcs if there is existing entry it will get updated bcs of on conflictstrategy set in every dao
        viewModelScope.launch() {
            recipeRepositary.updateRecipe(_recipeState.value.toData())


            //_recipeState.value = a
            _ingredientsState.value.forEach {
                ingredientRepositary.insert(it.toData())
            }
            _instructionState.value.forEach {
                instructionRepositary.insert(it.toData())
            }
            _tagsState.value.forEach {
                recipeRepositary.insertTag(it)
            }
        }

    }
    fun updateUIState(newUIState: DetailViewState) {
        _uiState.value = newUIState
    }
    fun navigateBack() {
        navController.popBackStack()
    }
    fun addTag(tag: Tag) {
        var list = tagState.value.toMutableList()
        list.add(tag)
        _tagsState.value = list.toList()
        _uiState.value = _uiState.value.copy(newTagText = "")
    }
    fun removeTag(tag: Tag) {
        var list = tagState.value.toMutableList()
        list.remove(tag)
        _tagsState.value = list.toList()

    }
    fun recalculateIngredients(newPortions: Int) {
        val data = recipeState.value.toData()
        val p = newPortions.toFloat() / data.portions
        _recipeState.value = recipeState.value.copy(portions = newPortions.toString())
        _ingredientsState.value = _ingredientsState.value.map {
            it.copy(amount = (Math.round(it.amount.toFloat() * p * 1000) / 1000f).toString())
        }
    }
    fun editIngredients(ingredient: IngredientDetail, index:Int){
        if (uiState.value.isEditable) {
            var list = ingredientsState.value.toMutableList()
            list.set(index,ingredient)
            _ingredientsState.value = list.toList()
        }
    }
    fun addIngredients(ingredient: IngredientDetail) {
        if (uiState.value.isEditable) {
                var newIngre = ingredient.copy( recipeId = recipeState.value.id)
                var list = ingredientsState.value.toMutableList()
                list.add(newIngre)
                _ingredientsState.value = list.toList()

        }
    }
    fun removeIngredients(ingredient: IngredientDetail) {
        if (uiState.value.isEditable) {
            var list = ingredientsState.value.toMutableList()
            list.remove(ingredient)
            _ingredientsState.value = list.toList()
        }
    }
    fun addInstruction(instruction: InstructionDetail) {
        if (uiState.value.isEditable) {
                var newInstruction = instruction.copy(recipeId = recipeState.value.id, orderNum = instructionState.value.size + 1)
                var list = instructionState.value.toMutableList()
                list.add(newInstruction)
                _instructionState.value = list.toList()
        }
    }
    fun editInstruction(instruction: InstructionDetail,index: Int){
        if (uiState.value.isEditable) {
            var list = instructionState.value.toMutableList()
            list.set(index,instruction)
            _instructionState.value = list.toList()
        }
    }
    fun removeInstruction(instruction: InstructionDetail) {
        if (uiState.value.isEditable) {
            var list = instructionState.value.toMutableList()
            list.remove(instruction)
            list.forEachIndexed {index, instructionDetail ->
                list.set(index, instructionDetail.copy(orderNum = index))
            }
            _instructionState.value = list.toList()
        }
    }

    private var serviceInternt = Intent()
    private var totalSeconds = 1;
    var _percState: MutableStateFlow<Float> = MutableStateFlow(0f)
    var percState: StateFlow<Float> = _percState.asStateFlow()
    var timerIngredienceIndex = MutableStateFlow(-1);
    var isTimerActive = MutableStateFlow(false)
    var minutes = 0
    val connection = object : ServiceConnection
    {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.LocalBinder

            viewModelScope.launch {
                binder.getService().minutes.collect{
                    minutes = it
                    _percState.value = (totalSeconds - it)/totalSeconds.toFloat()

                }
            }
            isTimerActive.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    fun startNewTimer(timeMinutes:Int, index:Int) {
        serviceInternt = Intent(StopwatchService.Actions.START.toString(), Uri.EMPTY,context,StopwatchService::class.java)
        serviceInternt.putExtra(StopwatchService.TIMEKEY,timeMinutes)
        totalSeconds = timeMinutes;
        context.startService(serviceInternt)
        context.bindService(serviceInternt,connection,Context.BIND_AUTO_CREATE)
        timerIngredienceIndex.value = index
    }
    fun stopTimer() {
        context.unbindService(connection)
        Intent(StopwatchService.Actions.STOP.toString(), Uri.EMPTY,context,StopwatchService::class.java).also {
            context.startService(it)
        }
        totalSeconds = 0
        timerIngredienceIndex.value = -1
        _percState.value = 0f
        isTimerActive.value = false
    }
}
class RecipeViewModelFactory(private val repository: RecipeRepositary, private val ingredientRepositary: IngredientRepositary,val instructionRepositary: InstructionRepositary ,private val navController: NavController,private val recipeId: Long,val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository,ingredientRepositary,instructionRepositary,navController,recipeId, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}