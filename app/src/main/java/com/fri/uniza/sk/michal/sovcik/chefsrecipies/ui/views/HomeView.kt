package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.*;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.plus
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(modifier: Modifier)
{
    val primarycolor = MaterialTheme.colorScheme.primary
    android.graphics.Color().plus(android.graphics.Color.valueOf(0f,0f,0f,-0.5f))
    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        Canvas(modifier = modifier) {
            drawCircle(primarycolor,220.dp.value, Offset(50f,220f))
        }
        Column {
            IntroHome(modifier = modifier)
            Text(text = "MainDishes")
            Row {
                val listDishes = emptyList<Recipe>()
                listDishes.forEach {
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
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    ChefsRecipiesTheme {
        HomeView(modifier = Modifier)
    }
}