package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.*;
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.plus
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.AppDatabase
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline.OfflineRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModelFactory

@Composable
fun HomeView(modifier: Modifier)
{
    val primarycolor = MaterialTheme.colorScheme.primary



    android.graphics.Color().plus(android.graphics.Color.valueOf(0f,0f,0f,-0.5f))
    val homeViewModel = viewModel<HomeViewModel>(factory = HomeViewModelFactory(OfflineRecipeRepositary(AppDatabase.Companion.getDatabase(
        LocalContext.current).recipeDao())))

    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        Canvas(modifier = modifier) {
            drawCircle(primarycolor,220.dp.value, Offset(50f,220f))
        }
        val state = homeViewModel.recipesFilter.collectAsState()
        Column (){
            IntroHome(modifier = modifier)
            Text(text = stringResource(R.string.breakfast))
            LazyRow {
                items(state.value.breakfastRecipes){
                        RecipeCard(recipe = it, modifier = modifier) {
                    }
                }
            }

            Text(text = stringResource(R.string.maindish))
            LazyRow {
                items(state.value.mainDishRecipes){
                    RecipeCard(recipe = it, modifier = modifier) {
                    }
                }
            }

            Text(text = stringResource(R.string.soup))
            LazyRow {
                items(state.value.soupRecipes){
                    RecipeCard(recipe = it, modifier = modifier) {
                    }
                }
            }

            Text(text = stringResource(R.string.dezert))
            LazyRow {
                items(state.value.dezerRecipes){
                    RecipeCard(recipe = it, modifier = modifier) {
                    }
                }
            }


        }
    }
}

@Composable
fun IntroHome(modifier: Modifier) {
    Box(modifier = modifier
        .height(300.dp)
        .fillMaxWidth()
        .background(Color.Transparent)){
        Text(text = "Top Rated", modifier = modifier
            .rotate(-5f)
            .align(Alignment.Center)
            , style = TextStyle(
                shadow = Shadow(
                    Color.Gray,
                    Offset(5f,5f),
                    blurRadius = 3f
                )
            )
            , fontSize = 50.sp)
    }
}
@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    ChefsRecipiesTheme {
        HomeView(modifier = Modifier)
    }
}