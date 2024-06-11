package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewIngredientsRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.SearchViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.SearchViewModelFactory

@ExperimentalLayoutApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(modifier: Modifier = Modifier, viewModel: SearchViewModel, goToDetail:(recipe:Recipe)->Unit)
{
    val uiState by viewModel.uiState.collectAsState()
    val recipes by viewModel.recipes.collectAsState()

    Column {
        ExposedDropdownMenuBox(
            expanded = uiState.expanded, onExpandedChange =
            {
                viewModel.editState(uiState.copy(expanded = it))
            }, modifier = Modifier
                .fillMaxWidth()
        )
        {
            TextField(
                value = uiState.search,
                onValueChange = {
                    viewModel.editTextFilter(it)
                },
                placeholder = { Text(stringResource(R.string.search_placeholder), fontStyle = FontStyle.Italic) },
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.editState(uiState.copy(expanded = it.isFocused))
                    }
                    .shadow(3.dp, shape = RoundedCornerShape(100))
                    .padding(5.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                    , shape = RoundedCornerShape(100),
                singleLine = true

            )
            ExposedDropdownMenu(
                expanded = uiState.expanded,
                onDismissRequest = {
                    //viewModel.editState(uiState.copy(expanded = false))
                })
            {
                viewModel.tags.collectAsState().value.forEach {
                    DropdownMenuItem(
                        text = { Text(text = "#" + it.tag) },
                        trailingIcon = {
                            Text(
                                text = "#tag",
                                fontStyle = FontStyle.Italic,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = Color.Gray
                            )
                        },
                        onClick = {
                            viewModel.addTagFilter(it)
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    )
                }
                viewModel.ingredient.collectAsState().value.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        trailingIcon = {
                            Text(
                                text = "#ingredient",
                                fontStyle = FontStyle.Italic,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = Color.Gray
                            )
                        },
                        onClick = {
                            viewModel.addIngredientFilter(it)
                        },
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.5f
                            )
                        )
                    )
                }

            }


        }
        FlowRow {
            uiState.tags.forEach {
                Row(modifier = Modifier
                    .padding(10.dp, 2.dp, 10.dp, 2.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(100)
                    )) {
                    Text(
                        text = it.tag,color = Color.White, modifier = Modifier.padding(5.dp,0.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        Icons.Filled.Close,
                        modifier = Modifier.clickable {
                            viewModel.removeTagFilter(it)

                        },
                        contentDescription = "close",
                        tint = Color.White
                    )
                }
            }
            uiState.ingredients.forEach {
                Row(modifier = Modifier
                    .padding(10.dp, 2.dp, 10.dp, 2.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(100)
                    ) ) {
                    Text(
                        text = it.name,color = Color.White, modifier = Modifier.padding(5.dp,0.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        Icons.Filled.Close,
                        modifier = Modifier.clickable {
                            viewModel.removeIngredientFilter(it)

                        },
                        contentDescription = "close",
                        tint = Color.White
                    )
                }
            }
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
            items(items = recipes) {
                RecipeCard(recipe = it, modifier = Modifier.width(250.dp), onClick = {
                    goToDetail(it)
                })
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun SearchViewPreview() {
val recipeRepositary = PreviewRecipeRepositary()
val ingredientRepositary = PreviewIngredientsRepositary()
    val viewModel = viewModel<SearchViewModel>(
        factory = SearchViewModelFactory(recipeRepositary,ingredientRepositary)
    )
    ChefsRecipiesTheme {
        SearchView(viewModel = viewModel, goToDetail =  {

        })
    }
}