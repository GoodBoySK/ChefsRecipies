package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
                    HomeView()
                }
            }
        }
    }
}
@Composable
fun Navigation(modifier: Modifier) {
    val navControler = rememberNavController()
    NavHost(navController = navControler, startDestination = Views.HOME.name, modifier = modifier) {
        composable(route = Views.HOME.name) {
            val context = LocalContext;
            HomeView()
        }
        composable(route = Views.SEARCH.name) {
            val context = LocalContext;
            SearchView()
        }
        composable(route = Views.RECIPEDETAIL.name + "/{recipeId}", arguments = listOf(
            navArgument(name = "recipeId"){
            type = NavType.IntType
            nullable = false
        })) {
            val context = LocalContext;
            RecipeDetailView(it.arguments?.getInt("recipeId") ?: -1)
        }
        composable(route = Views.IMPORT.name) {
            val context = LocalContext;
            ImportView()
        }
        composable(route = Views.USERINFO.name) {
            val context = LocalContext;
            UserInfoView()
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
        Greeting("Android")
    }
}