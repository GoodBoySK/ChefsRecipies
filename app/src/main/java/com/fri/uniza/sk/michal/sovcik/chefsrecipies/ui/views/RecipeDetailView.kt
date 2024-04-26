@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.RecipeDetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.DetailViewState
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.IngredientDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.InstructionDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.UIModels.RecipeDetail
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewIngredientsRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewInstructuinRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.RecipeViewModelFactory

enum class DetailViewParts {
    Desctiption,
    Ingredients,
    Instructions
}

fun makeText(text:String,pos:String ):AnnotatedString {
    val sb = AnnotatedString.Builder()
    sb.append(text)
    sb.withStyle(ParagraphStyle(TextAlign.Right))
    {
        append(pos)
    }
    return sb.toAnnotatedString()
}
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailView(modifier: Modifier = Modifier, viewModel: RecipeDetailViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    val recipe by viewModel.recipeState.collectAsState()
    val ingredients by viewModel.ingredientsState.collectAsState()
    val instructions by viewModel.instructionState.collectAsState()
    val tags by viewModel.tagState.collectAsState()

    val context = LocalContext.current;
    Box {
        //Back FloatingButtpn
        Column (
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f))
        {
            FloatingActionButton(modifier = Modifier
                .padding(10.dp),
                onClick = { viewModel.navigateBack() },
                shape = RoundedCornerShape(100),
                containerColor = Color(240,240,240,240) ,
                contentColor = Color(240,240,240,240)) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color.DarkGray)
            }

        }
        //Save button
        if (uiState.isEditable) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .padding(5.dp)
            ) {
                FloatingActionButton(modifier = Modifier
                    .padding(10.dp, 4.dp),
                    onClick = {
                        viewModel.saveRecipe()
                        viewModel.updateUIState(uiState.copy(isEditable = false))

                              },
                    shape = RoundedCornerShape(100),
                    containerColor = Color(240, 240, 240, 240),
                    contentColor = Color(240, 240, 240, 240)
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp, 0.dp),
                        text = "Save",
                        color = Color.Black
                    )
                }
            }
        }
        //More floating button
        Column (
            modifier = Modifier
                .align(Alignment.TopEnd)

                .zIndex(1f)
                .padding(10.dp))
        {
            FloatingActionButton(modifier = Modifier
                ,onClick = {viewModel.updateUIState(uiState.copy(menuShown = !uiState.menuShown)) },shape = RoundedCornerShape(100), containerColor = Color(240,240,240,240) ,contentColor = Color(240,240,240,240)) {
                Icon(Icons.Filled.MoreHoriz, contentDescription = "Back", tint = Color.DarkGray)
            }
            DropdownMenu(modifier = Modifier.zIndex(1f),expanded = uiState.menuShown, onDismissRequest = {viewModel.updateUIState(uiState.copy(menuShown = false))  }) {
                DropdownMenuItem(enabled = true, modifier = Modifier, text = { Text(text = "Share", color = Color.Black) }, onClick = {
                    viewModel.updateUIState(uiState.copy(menuShown = false))
                    viewModel.share(context)
                 })
                DropdownMenuItem(text = { Text(text = "Edit On/Off") }, onClick = {
                    viewModel.updateUIState(uiState.copy(isEditable = !uiState.isEditable,menuShown = false))
                })
            }
        }
        //Else
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(240, 240, 240, 250))
        ) {
            //This is Image
            item {
                ImageWithChose(
                    LocalContext.current.contentResolver,
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        bitmap =  recipe.bitmap,
                    onImgChose =
                    {
                        if (it != null){

                            var newRecipe = recipe.copy(bitmap = it)
                            viewModel.editRecipe(newRecipe)
                        }
                    },
                    isActive = uiState.isEditable
                )
            }
            //This is tabs in midle of screen
            stickyHeader {
                TabRow(
                    selectedTabIndex = uiState.selectedTabIndex, modifier = Modifier
                        .height(40.dp)
                ) {
                    Tab(selected = true, onClick =
                    {
                        navController.popBackStack()
                        navController.navigate(DetailViewParts.Desctiption.name)
                        viewModel.updateUIState(uiState.copy(selectedTabIndex = 0))
                    }, modifier = Modifier.height(40.dp)) {
                        Icon(Icons.Filled.Image, contentDescription = "Description")
                    }
                    Tab(selected = false, onClick = {
                        navController.popBackStack()
                        navController.navigate(DetailViewParts.Ingredients.name)
                        viewModel.updateUIState(uiState.copy(selectedTabIndex = 1))
                    }) {
                        Icon(Icons.Filled.Fastfood, contentDescription = "Ingredients")
                    }
                    Tab(selected = false, onClick = {
                        navController.popBackStack()
                        navController.navigate(DetailViewParts.Instructions.name)
                        viewModel.updateUIState(uiState.copy(selectedTabIndex = 2))

                    }) {
                        Icon(Icons.Filled.AllInclusive, contentDescription = "Ingredients")
                    }
                }
            }
            //NavHost
            item {
                NavHost(navController = navController, startDestination = DetailViewParts.Desctiption.name, modifier = Modifier)
            {
                composable(route = DetailViewParts.Desctiption.name)
                {
                    DetailViewDetail(recipe, viewModel, uiState, tags)
                }
                composable(route = DetailViewParts.Ingredients.name)
                {

                    DetailViewIngredient(
                        recipe,
                        viewModel,
                        uiState,
                        ingredients
                    )

                }
                composable(route = DetailViewParts.Instructions.name)
                {
                    Column {
                        instructions.forEachIndexed { index, instruction ->
                            InstructionBox(
                                instruction,
                                viewModel,
                                index,
                                uiState,
                                context,

                            )
                        }
                        Button(onClick = { viewModel.addInstruction(InstructionDetail()) }) {
                            Text(text = "Add instruction...", fontStyle = FontStyle.Italic)
                        }
                    }


                }
            }
            }
            //In

        }
    }

}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun InstructionBox(
    instruction: InstructionDetail,
    viewModel: RecipeDetailViewModel,
    index: Int,
    uiState: DetailViewState,
    context: Context,

) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    )
    {
        Row {
            Text(text = instruction.orderNum.toString(), modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                value = instruction.text,
                onValueChange = {
                    viewModel.editInstruction(instruction.copy(text = it), index)
                },
                enabled = uiState.isEditable,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )

            {
                val isTimerActive by viewModel.isTimerActive.collectAsState()

                NumberTextField(number = if(!isTimerActive)instruction.stopTime else viewModel.minutes.toString(), onValueChange = {
                    viewModel.editInstruction(instruction.copy(stopTime = it), index)
                },
                    enabled = uiState.isEditable,
                    prefix = { Icon(Icons.Filled.Timelapse, contentDescription = "Clock") },
                    suffix = { Text(text = stringResource(R.string.timeUnitShortcut)) },
                    modifier = Modifier.clickable {
                        if (!uiState.isEditable) {
                            //TODO start timer
                            if (isTimerActive) viewModel.stopTimer()
                            else viewModel.startNewTimer(
                                instruction.toData().stopTime.toInt(),

                                index
                            )

                        }
                    }
                )
                LinearProgressIndicator(progress = if (index == viewModel.timerIngredienceIndex.collectAsState().value) viewModel.percState.collectAsState().value else 0f)
            }
            NumberTextField(
                onValueChange = {
                                viewModel.editInstruction(instruction.copy(temperature = it),index)
                },
                number = instruction.temperature.toString(),
                enabled = uiState.isEditable,
                prefix = {
                    Icon(
                        Icons.Filled.DeviceThermostat,
                        contentDescription = "Thermometer"
                    )
                },
                suffix = { Text(text = stringResource(R.string.themeratureUnits)) },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
            )


        }
        if (!(!uiState.isEditable && (instruction.bitmap == null || instruction.bitmap.byteCount == 0))) {
            ImageWithChose(
                contentResolver = LocalContext.current.contentResolver,
                bitmap = instruction.bitmap,
                onImgChose = {
                    if (it != null)
                        viewModel.editInstruction(instruction.copy(bitmap = it), index)
                },
                isActive = uiState.isEditable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(20.dp, 10.dp)
                    .background(
                        Color.LightGray
                    )

            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailViewIngredient(
    recipe: RecipeDetail,
    viewModel: RecipeDetailViewModel,
    uiState: DetailViewState,
    ingredients: List<IngredientDetail>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        //First line
        Row {
            TextField(
                value = recipe.time.toString(),
                onValueChange = {},
                enabled = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                suffix = {
                    Text(text = "min")
                }
            )
            TextField(value = recipe.portions.toString(),
                onValueChange = {},
                enabled = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                suffix = {
                    Text(text = "portions")
                }

            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Column {
                    IconButton(
                        onClick =
                        {
                            viewModel.recalculateIngredients(recipe.portions.toInt() + 1)
                        },
                        modifier = Modifier,
                        enabled = !uiState.isEditable
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "add")
                    }
                    IconButton(
                        onClick =
                        {
                            viewModel.recalculateIngredients(recipe.portions.toInt() - 1)
                        },
                        modifier = Modifier,
                        enabled = !uiState.isEditable
                    ) {
                        Icon(Icons.Filled.Remove, contentDescription = "subtr")
                    }
                }
            }

        }

        ingredients.forEachIndexed { index, ingredient ->
            Row(modifier = Modifier.padding(5.dp, 0.dp)) {
                TextField(
                    value = ingredient.name,
                    onValueChange = {
                        viewModel.editIngredients(ingredient.copy(name = it), index)
                    },
                    modifier = Modifier
                        .weight(2.8f)
                        .padding(2.dp, 5.dp),
                    singleLine = true,
                    enabled = uiState.isEditable
                )
                DecimalTextField(
                    number = ingredient.amount,
                    onValueChange = {
                        viewModel.editIngredients(ingredient.copy(amount = it), index)
                    },
                    modifier = Modifier
                        .weight(2f)
                        .padding(2.dp, 5.dp),
                    enabled = uiState.isEditable
                )
                TextField(
                    value = ingredient.measuremnts,
                    onValueChange = {
                        viewModel.editIngredients(ingredient.copy(measuremnts = it), index)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp, 5.dp),
                    singleLine = true,
                    enabled = uiState.isEditable
                )
                if (uiState.isEditable) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        IconButton(
                            onClick = { viewModel.removeIngredients(ingredient) },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                }
            }
        }
        if (uiState.isEditable) {
            Button(onClick = { viewModel.addIngredients(IngredientDetail()) }) {
                Text(text = "Add ingredient...", fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
private fun DetailViewDetail(
    recipe: RecipeDetail,
    viewModel: RecipeDetailViewModel,
    uiState: DetailViewState,
    tags: List<Tag>
) {
    Column {

        //Recipe name textfield
        TextField(
            value = recipe.name,
            onValueChange = { viewModel.editRecipe(recipe.copy(name = it)) },
            textStyle = MaterialTheme.typography.headlineLarge,
            enabled = uiState.isEditable,
            placeholder = {
                Text(
                    text = stringResource(R.string.recipenameplaceholder)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        )
        //Time and portions
        Row(modifier = Modifier.padding(10.dp, 0.dp)) {
            val modifier = Modifier
                .weight(1f)
                .padding(3.dp)
            NumberTextField(number = recipe.time,
                enabled = uiState.isEditable,
                suffix = { Text(text = "min") },
                modifier = modifier,
                onValueChange = {
                    viewModel.editRecipe(recipe.copy(time = it))
                }
            )
            NumberTextField(number = recipe.portions,
                enabled = uiState.isEditable,
                suffix = { Text(text = "dishes") },
                modifier = modifier,
                onValueChange = {
                    viewModel.editRecipe(recipe.copy(portions = it))
                }
            )

        }

        //DishType + Rating
        Row(modifier = Modifier.padding(10.dp, 0.dp))
        {
            ExposedDropdownMenuBox(
                expanded = uiState.dishTypeShowed, onExpandedChange = {
                    viewModel.updateUIState(uiState.copy(dishTypeShowed = it))
                }, modifier = Modifier
                    .weight(1f)
                    .padding(3.dp)
            )
            {
                TextField(
                    value = recipe.dishType.name, onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Dish type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.dishTypeShowed)
                    },
                    modifier = Modifier.menuAnchor()

                )
                ExposedDropdownMenu(
                    expanded = uiState.dishTypeShowed,
                    onDismissRequest = { viewModel.updateUIState(uiState.copy(dishTypeShowed = false)) }) {
                    DishType.values().forEach {
                        DropdownMenuItem(text = { Text(it.name) },
                            onClick = {
                                viewModel.editRecipe(recipe.copy(dishType = it))
                                viewModel.updateUIState(uiState.copy(dishTypeShowed = false))
                            }
                        )
                    }
                }

            }

            DecimalTextField(number = recipe.rating,
                enabled = uiState.isEditable,
                suffix = {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "start",
                        tint = Color.Yellow
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(3.dp),
                onValueChange = {
                    viewModel.editRecipe(recipe.copy(rating = it))
                }
            )
        }
        //Tags
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            tags.forEach {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(RoundedCornerShape(100))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Row(modifier = Modifier.padding(10.dp, 2.dp, 15.dp, 2.dp)) {
                        Text(text = it.tag, maxLines = 1, color = Color.White,
                            modifier = Modifier.padding(5.dp,0.dp))
                        if (uiState.isEditable) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                Icons.Filled.Close,
                                modifier = Modifier.clickable {
                                    viewModel.removeTag(it)
                                },
                                contentDescription = "close",
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
            if (uiState.isEditable) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(RoundedCornerShape(100))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                ) {
                    Row(modifier = Modifier.padding(10.dp, 2.dp, 5.dp, 2.dp)) {
                        BasicTextField(
                            value = uiState.newTagText,
                            onValueChange = {
                                viewModel.updateUIState(uiState.copy(newTagText = it))
                            },
                            singleLine = true,
                            keyboardActions = KeyboardActions(onAny = {
                                viewModel.addTag(Tag(recipeId = recipe.id,tag = uiState.newTagText))
                            })
                        ) {
                            if (uiState.newTagText == "") {
                                Text(text = "AddText...", color = Color.White)
                            } else {
                                Text(text = uiState.newTagText, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            Icons.Filled.Add,
                            modifier = Modifier.clickable {
                                viewModel.addTag(Tag(recipeId = recipe.id,tag = uiState.newTagText))
                            },
                            contentDescription = "close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        //Description
        OutlinedTextField(
            value = recipe.description,
            onValueChange = { viewModel.editRecipe(recipe.copy(description = it)) },
            modifier = Modifier
                .fillMaxSize()
                .height(500.dp),
            placeholder = { Text(text = "Recipe description...", color = Color.Black) },
            enabled = uiState.isEditable
        )

    }
}

@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.P)
@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun RecipeDetailPreview() {
    val repo = PreviewRecipeRepositary()
    val repoIng = PreviewIngredientsRepositary()
    val repoIns = PreviewInstructuinRepositary()


    val viewModel = viewModel<RecipeDetailViewModel>(
        factory = RecipeViewModelFactory(
            repository = repo,repoIng,repoIns, NavController(LocalContext.current),1, LocalContext.current
        )
    )
    viewModel.updateUIState(DetailViewState(isEditable = true,menuShown = false,"",1))
    ChefsRecipiesTheme {
        RecipeDetailView(
            viewModel = viewModel


        )
    }
}
