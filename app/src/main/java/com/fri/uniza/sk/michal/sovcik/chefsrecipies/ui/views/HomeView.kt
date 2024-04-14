package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.*;
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode.Companion.Polygon
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.plus
import androidx.graphics.shapes.PointTransformer
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.graphics.shapes.transformed
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.AppDatabase
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.FilteredRecipes
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline.OfflineRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.preview.PreviewRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun HomeView(modifier: Modifier = Modifier,
             state: State<FilteredRecipes>
)
{
    val colorSchee = MaterialTheme.colorScheme
    val config = LocalConfiguration.current;

    android.graphics.Color().plus(android.graphics.Color.valueOf(0f,0f,0f,-0.5f))


    Box(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(colorSchee.primary.copy(alpha = 0.8f),220.dp.value, Offset(50f,250f))
            val l = config.screenWidthDp.dp
            drawCircle(colorSchee.primary.copy(alpha = 0.8f),220.dp.value, Offset(250f,100f))
            val polygon = RoundedPolygon(radius = 300.dp.value, numVertices = 4, centerX = size.width + 10.dp.value, centerY = 180.dp.value)


            var polygonTriangel = RoundedPolygon(radius = 200.dp.value, numVertices = 3, centerX = size.width/2 + 10.dp.value, centerY = 500.dp.value)
            if (size.width > 1500) polygonTriangel = RoundedPolygon(radius = 300.dp.value, numVertices = 3, centerX = size.width/2 + 10.dp.value, centerY = 400.dp.value)
            rotate(15f){
                drawPath(polygon.toPath().asComposePath(), color = colorSchee.secondary)
                drawPath(polygonTriangel.toPath().asComposePath(), color = colorSchee.tertiary)
            }

        }
        Column (){
            val textModifier = Modifier
            IntroHome(modifier = modifier)
            val modifierReusable = modifier.padding(top = 16.dp, end = 16.dp)
            val categories = listOf(
                Pair(state.value.soupRecipes,R.string.soup),
                Pair(state.value.mainDishRecipes,R.string.maindish),
                Pair(state.value.dezerRecipes,R.string.dezert),
                Pair(state.value.breakfastRecipes,R.string.breakfast))

            categories.forEach (){pair->
                Text(text = stringResource(pair.second), style = MaterialTheme.typography.titleLarge, modifier = Modifier)
                LazyRow (modifierReusable){
                    items(pair.first){
                        RecipeCard(recipe = it, modifier = modifierReusable,{})
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
@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, showSystemUi = true,
)
@PreviewScreenSizes
@Composable
fun HomeViewPreview(){
    val previewRecipeRepositary = PreviewRecipeRepositary()

    runBlocking {
        launch {
            previewRecipeRepositary.insertRecipe(Recipe(0,"Makronky",DishType.Dezert,30,15,4f,"Anicka",""))
            }
    }
    ChefsRecipiesTheme {
            val state = mutableStateOf(FilteredRecipes(mainDishRecipes = listOf(Recipe(0,"Main",DishType.MainDish,30,15,3f,"michal",""))))
        HomeView(modifier = Modifier,
                state = state)

    }
}