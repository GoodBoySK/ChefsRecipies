package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.DetailViewState
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.IngredientRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.InstructionRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.interfaceRepositaries.RecipeRepositary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RecipeDetailViewModel(val recipeRepositary: RecipeRepositary, val ingredientRepositary: IngredientRepositary, val instructionRepositary: InstructionRepositary,private val navController: NavController,private val id: Long) : ViewModel() {

    private var _recipeState:MutableStateFlow<Recipe> = MutableStateFlow(Recipe())
    var recipeState:StateFlow<Recipe> = _recipeState.asStateFlow()
    private  var _ingredientsState:MutableStateFlow<List<Ingredient>> = MutableStateFlow(emptyList())
     var ingredientsState:StateFlow<List<Ingredient>> = _ingredientsState.asStateFlow()
    private  var _instructionState:MutableStateFlow<List<Instruction>> = MutableStateFlow(emptyList())
     var instructionState:StateFlow<List<Instruction>>  = _instructionState.asStateFlow()
    private  var _tagsState:MutableStateFlow<List<Tag>> = MutableStateFlow(emptyList())
     var tagState:StateFlow<List<Tag>> = _tagsState.asStateFlow()

    private var _uiState = MutableStateFlow(DetailViewState(true,false,"",0))
    val uiState = _uiState.asStateFlow()
    init {
        if (id == (0).toLong()) {

        } else{
            viewModelScope.launch(Dispatchers.IO) {
                _recipeState = MutableStateFlow(recipeRepositary.getRecipe(id) ?: Recipe())
                recipeState = _recipeState.asStateFlow()

                _ingredientsState = MutableStateFlow(ingredientRepositary.getIngredients(id))
                ingredientsState = _ingredientsState.asStateFlow()

                _instructionState = MutableStateFlow(instructionRepositary.getInstructions(id))
                instructionState = _instructionState.asStateFlow()

                _tagsState = MutableStateFlow(recipeRepositary.getAllTags(id))
                tagState = _tagsState.asStateFlow()
                _uiState.value = _uiState.value.copy(false)

            }
        }

    }

    fun editRecipe(recipe: Recipe) {

        if (uiState.value.isEditable)
            _recipeState.value = recipe
    }
    fun saveRecipe() {
        if (recipeState.value.id == (0).toLong()) {
            viewModelScope.launch(Dispatchers.IO) {
                val newId = recipeRepositary.insertRecipe(recipeState.value)
                _recipeState = MutableStateFlow(recipeRepositary.getRecipe(newId) ?: Recipe())
                recipeState = _recipeState.asStateFlow()


            }
        }
        else {
            viewModelScope.launch {
                recipeRepositary.updateRecipe(_recipeState.value)

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
        viewModelScope.launch {
            recipeRepositary.insertTag(tag)
        }
    }
    fun removeTag(tag: Tag) {
        var list = tagState.value.toMutableList()
        list.remove(tag)
        _tagsState.value = list.toList()
        viewModelScope.launch {
            recipeRepositary.deleteTag(tag)
        }

    }
    fun recalculateIngredients(newPortions: Int) {
        val p = newPortions.toFloat() / recipeState.value.portions
        _recipeState.value = recipeState.value.copy(portions = newPortions)
        _ingredientsState.value = _ingredientsState.value.map {
            it.copy(amount = Math.round(it.amount * p * 1000) / 1000f)
        }
    }
    fun editIngredients(ingredient: Ingredient, index:Int){
        if (uiState.value.isEditable) {
            var list = ingredientsState.value.toMutableList()
            list.set(index,ingredient)
            _ingredientsState.value = list.toList()
            viewModelScope.launch {
                ingredientRepositary.update(ingredient)
            }
        }
    }
    fun addIngredients(ingredient: Ingredient) {
        if (uiState.value.isEditable) {
            viewModelScope.launch {
                val newId = ingredientRepositary.insert(ingredient)
                var newIngre = ingredient.copy(id = id, recipeId = recipeState.value.id)
                var list = ingredientsState.value.toMutableList()
                list.add(newIngre)
                _ingredientsState.value = list.toList()

            }
        }
    }
    fun removeIngredients(ingredient: Ingredient) {
        if (uiState.value.isEditable) {
            var list = ingredientsState.value.toMutableList()
            list.remove(ingredient)
            _ingredientsState.value = list.toList()
            viewModelScope.launch {
                ingredientRepositary.delete(ingredient)
            }
        }
    }
    fun addInstruction(instruction: Instruction) {
        if (uiState.value.isEditable) {
            viewModelScope.launch {
                val newId = instructionRepositary.insert(instruction)
                var newInstruction = instruction.copy(id = id, recipeId = recipeState.value.id)
                var list = instructionState.value.toMutableList()
                list.add(newInstruction)
                _instructionState.value = list.toList()

            }
        }
    }
    fun editInstruction(instruction: Instruction,index: Int){
        if (uiState.value.isEditable) {
            var list = instructionState.value.toMutableList()
            list.set(index,instruction)
            _instructionState.value = list.toList()
            viewModelScope.launch {
                instructionRepositary.update(instruction)
            }
        }
    }
    fun removeInstruction(instruction: Instruction) {
        if (uiState.value.isEditable) {
            var list = instructionState.value.toMutableList()
            list.remove(instruction)
            _instructionState.value = list.toList()
            viewModelScope.launch {
                instructionRepositary.delete(instruction)
            }
        }
    }
}
class RecipeViewModelFactory(private val repository: RecipeRepositary, private val ingredientRepositary: IngredientRepositary,val instructionRepositary: InstructionRepositary ,private val navController: NavController,private val recipeId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(repository,ingredientRepositary,instructionRepositary,navController,recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}