package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier, onClick: () -> Unit,height: Dp = 250.dp,width:Dp = 250.dp) {
    Card(
        modifier = modifier
            .height(height)
            .width(width)
            .padding(10.dp),
        onClick = onClick
    ){
       // Image(painter = , contentDescription = )
        Column {
            AsyncImage(model = Uri.parse(recipe.picPath), contentDescription = "RecipePicture", modifier = Modifier.weight(1f))
            Text(text = recipe.name, fontSize = 20.sp)
            Text(text = recipe.autor, fontStyle = FontStyle.Italic)
        }

    }
}

@Preview
@Composable
private fun CardPreview() {
    ChefsRecipiesTheme {
        RecipeCard(Recipe(1,"American Cookies",DishType.MainDish,30,15,4.25f,"Michalito", ""), Modifier,{})
    }
}