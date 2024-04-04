package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.Views
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.HomeView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.ImportView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.RecipeDetailView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.SearchView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.UserInfoView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChefsRecipiesTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(modifier = Modifier)
                }
            }
        }
    }
}
@Composable
fun Navigation(modifier: Modifier) {
    val navControler = rememberNavController()
    val primaryColor = MaterialTheme.colorScheme.primary;

    Column (modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
        NavHost(navController = navControler, startDestination = Views.HOME.name, modifier = Modifier.weight(1f).fillMaxWidth().zIndex(0f)) {
            composable(route = Views.HOME.name) {
                val context = LocalContext;
                HomeView(modifier)
            }
            composable(route = Views.SEARCH.name) {
                val context = LocalContext;
                SearchView(modifier)
            }
            composable(route = Views.RECIPEDETAIL.name + "/{recipeId}", arguments = listOf(
                navArgument(name = "recipeId"){
                    type = NavType.IntType
                    nullable = false
                })) {
                val context = LocalContext;
                RecipeDetailView(it.arguments?.getInt("recipeId") ?: -1,modifier)
            }
            composable(route = Views.IMPORT.name) {
                val context = LocalContext;
                ImportView(modifier)
            }
            composable(route = Views.USERINFO.name) {
                val context = LocalContext;
                UserInfoView(modifier)
            }
        }
        NavigationBar(modifier = Modifier.zIndex(1f).padding(top = 30.dp).graphicsLayer { clip = false }) {


            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.HOME.name) }, icon = { Icon(Icons.Outlined.Home, contentDescription = "home") },modifier = modifier.fillMaxSize())
            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.SEARCH.name) }, icon = { Icon(Icons.Outlined.Search, contentDescription = "search")})
                Button(
                    onClick = {},
                    shape = CircleShape,
                    modifier = modifier.wrapContentSize(unbounded = true, align = Alignment.TopCenter).size(55.dp)
                       .zIndex(1f).drawWithContent {
                            drawContent()
                        }  ,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "import")
                }
            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.IMPORT.name) }, icon = { Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "import")})
            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.USERINFO.name) }, icon = { Icon(Icons.Outlined.Person, contentDescription = "user_info")})
        }
    }


}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NavPreview() {
    ChefsRecipiesTheme {
        Navigation(modifier = Modifier)
    }
}