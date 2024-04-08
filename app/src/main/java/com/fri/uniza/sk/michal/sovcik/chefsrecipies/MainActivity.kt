package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
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
import androidx.test.core.app.ApplicationProvider
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.AppDatabase
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.Views
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.HomeView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.ImportView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.RecipeDetailView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.SearchView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.UserInfoView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)

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
    val db = AppDatabase.getDatabase(LocalContext.current)
    //this is temporary code TODO delete
    runBlocking {
        launch {
            db.recipeDao()
                .insert(Recipe(1,"Cokies",DishType.Dezert,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(2,"American Cokies",DishType.Dezert,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(3,"Cake",DishType.Dezert,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(4,"Chicek",DishType.MainDish,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(5,"Beef",DishType.MainDish,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(6,"Rice",DishType.MainDish,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(7,"TomatoSoup",DishType.Soup,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(8,"Bread",DishType.Breakfast,30,15,4f,"Michalito","" ))
          db.recipeDao()
                .insert(Recipe(9,"MIlk",DishType.Breakfast,30,15,4f,"Michalito","" ))
        }
    }
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
    val db = AppDatabase.getDatabase(LocalContext.current)
    ChefsRecipiesTheme {
        Navigation(modifier = Modifier)
    }
}