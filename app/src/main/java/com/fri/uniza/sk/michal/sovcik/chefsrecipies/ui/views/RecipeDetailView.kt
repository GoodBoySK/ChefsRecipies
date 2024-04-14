package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import org.jetbrains.annotations.Async

@Composable
fun RecipeDetailView(recipe: Recipe, modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(model = Uri.parse(recipe.picPath), contentDescription = recipe.name,modifier = Modifier.height(400.dp), contentScale = ContentScale.FillHeight)
        TabRow(selectedTabIndex = 0, modifier = Modifier.height(40.dp)) {
            Tab(selected = true, onClick = { /*TODO*/ },modifier = Modifier.height(40.dp)) {
                Icon(Icons.Filled.Image,contentDescription = "sda")
            }
            Tab(selected = false, onClick = { /*TODO*/ }) {

            }
            Tab(selected = false, onClick = { /*TODO*/ }) {

            }
        }
    }
}

@Preview(showSystemUi = true,
    showBackground = true )
@Composable
private fun RecipeDetailPreview() {
    ChefsRecipiesTheme {
        RecipeDetailView(recipe = Recipe(0,"Main", DishType.MainDish,30,15,3f,"michal",""))
    }
}
