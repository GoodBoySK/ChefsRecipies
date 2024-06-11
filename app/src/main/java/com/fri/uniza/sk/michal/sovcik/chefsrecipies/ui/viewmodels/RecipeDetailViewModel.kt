package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


class RecipeDetailViewModel(val recipeRepositary: RecipeRepositary, val ingredientRepositary: IngredientRepositary, val instructionRepositary: InstructionRepositary, private val navController: NavController, private var id: Long, val pref: DataStore<Preferences>, val context: Context) : ViewModel() {

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
    val recipePersistant = recipeRepositary.getRecipe(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Recipe())
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
                _instructionState.value = list.map {
                    var uiItem = it.toUI()
                    val alreadyInList = instructionState.value.getOrNull(instructionState.value.indexOfFirst {item -> item.id == uiItem.id  })
                    if (alreadyInList != null)
                        uiItem = alreadyInList

                    uiItem

                }

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
        renameFolder(recipePersistant.value.name, recipeState.value.name)
        viewModelScope.launch() {
            val copyIngredienece = _ingredientsState.value.toList()
            val copyInstruction = _instructionState.value.toList()
            val copyTag = _tagsState.value.toList()

            //_recipeState.value = a
            copyIngredienece.forEachIndexed { _, it->
                ingredientRepositary.insert(it.toData())
            }
            copyInstruction.forEachIndexed { _, it ->
                instructionRepositary.insert(it.toData())
            }
            copyTag.forEachIndexed { _, it ->
                recipeRepositary.insertTag(it)
            }
            _ingredientsState.value = emptyList()
            _instructionState.value = emptyList()
            _tagsState.value = emptyList()
            recipeRepositary.updateRecipe(_recipeState.value.toData())

        }

    }
    fun share(context: Context){
        var text = """
            ${recipeState.value.name}
            author: ${recipeState.value.autor}
            
            Description:
            ${recipeState.value.description}

            Ingredients:
            ${ingredientsState.value.forEach {
            "${it.name} ${it.amount} ${it.measuremnts}\n" }
            }
            Instructions
            ${instructionState.value.forEach { 
                "${it.orderNum }. ${it.text}\n ${if (it.toData().stopTime != 0f) it.stopTime else ""} ${if (it.toData().temperature != 0)it.temperature else ""}\n"
             
            
        }}
            
        """.trimIndent()


        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "plaint/text"
            putExtra(Intent.EXTRA_TEXT,text)
        }
        val choser = Intent.createChooser(shareIntent,null)
        context.startActivity(choser)
    }
    fun updateUIState(newUIState: DetailViewState) {
        _uiState.value = newUIState
    }
    fun navigateBack() {
        navController.popBackStack()
        viewModelScope.launch {
            recipeFlow.first {
                if (it.name != recipeState.value.name)
                {
                    renameFolder(recipeState.value.name, it.name)
                }

                true
            }
        }
    }
    fun addTag(tag: Tag) {
        var list = tagState.value.toMutableList()
        list.add(tag.copy(recipeId = recipeState.value.id))
        _tagsState.value = list.toList()
        _uiState.value = _uiState.value.copy(newTagText = "")
    }
    fun removeTag(tag: Tag) {
        var list = tagState.value.toMutableList()
        list.remove(tag)
        _tagsState.value = list.toList()
        viewModelScope.launch{
            recipeRepositary.deleteTag(tag)
        }

    }
    fun recalculateIngredients(newPortions: Int) {
        val data = recipeState.value.toData()
        val p = newPortions.toFloat() / data.portions
        _recipeState.value = recipeState.value.copy(portions = newPortions.toString())
        _ingredientsState.value = _ingredientsState.value.map {
            it.copy(amount = (Math.round(it.amount.toFloat() * p * 100) / 100f).toString())
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
            viewModelScope.launch{
                ingredientRepositary.delete(ingredient.toData())
            }
        }
    }
    fun addInstruction(instruction: InstructionDetail) {
        if (uiState.value.isEditable) {
            var newInstruction = instruction.copy(recipeId = recipeState.value.id, orderNum = instructionState.value.size + 1)
            viewModelScope.launch{
                val newId = instructionRepositary.insert(newInstruction.toData())
                newInstruction = newInstruction.copy(id = newId);
                var list = instructionState.value.toMutableList()

                list.add(newInstruction)
                _instructionState.value = list.toList()

            }



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
                list.set(index, instructionDetail.copy(orderNum = index + 1))
            }
            _instructionState.value = list.toList()
            viewModelScope.launch {
                val file = File(context.filesDir, recipeState.value.name + "/" + instruction.id + ".png")
                file.delete()
                instructionRepositary.delete(instruction.toData())
            }
        }
    }
    fun renameFolder(path: String, newName: String)
    {
        var file = File(context.filesDir.path,path)
        var newFile = File(context.filesDir.path,newName)
        file.renameTo(newFile)
    }

    fun deleteFile(path:String)
    {

        var file = File(path)
        file.delete()
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
                binder.getService().seconds.collect{
                    minutes = it / 60
                    _percState.value = (totalSeconds - it)/totalSeconds.toFloat()
                    if (totalSeconds == it) {
                        isTimerActive.value = false
                    }

                }
            }
            isTimerActive.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    fun startNewTimer(timeMinutes:Float, index:Int) {
        serviceInternt = Intent(StopwatchService.Actions.START.toString(), Uri.EMPTY,context,StopwatchService::class.java)
        serviceInternt.putExtra(StopwatchService.TIMEKEY,(timeMinutes * 60).toInt())
        totalSeconds = (timeMinutes * 60).toInt()
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
class RecipeViewModelFactory(private val repository: RecipeRepositary, private val ingredientRepositary: IngredientRepositary,val instructionRepositary: InstructionRepositary ,private val navController: NavController,private val recipeId: Long,val context: Context, private val pref:DataStore<Preferences>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository,ingredientRepositary,instructionRepositary,navController,recipeId, pref,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}