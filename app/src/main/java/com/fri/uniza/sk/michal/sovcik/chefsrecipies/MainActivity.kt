@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.fri.uniza.sk.michal.sovcik.chefsrecipies

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.AppDatabase
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.Views
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline.OfflineIngredinetRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline.OfflineInstructionRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.repositaries.offline.OfflineRecipeRepositary
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.HomeViewModelFactory
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.RecipeDetailViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.RecipeViewModelFactory
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.SearchViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.SearchViewModelFactory
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.UserInfoViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.UserInfoViewModelFactory
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.HomeView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.ImportView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.RecipeDetailView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.SearchView
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views.UserInfoView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.CAMERA),
                0)
        }


        setContent {
            ChefsRecipiesTheme {
                // A surface container using the 'background' color from the theme


                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation((this.application as App).dataStorePreferences)
                }
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Navigation(dataStorePreferences: DataStore<Preferences>) {
    val navControler = rememberNavController()
    val primaryColor = MaterialTheme.colorScheme.primary





    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
        NavHost(navController = navControler,
            startDestination = Views.HOME.name,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
            composable(route = Views.HOME.name) {
                val context = LocalContext
                val viewModel =  viewModel<HomeViewModel>(
                    factory = HomeViewModelFactory(
                        OfflineRecipeRepositary(
                            AppDatabase.getDatabase(LocalContext.current)
                                .recipeDao(),
                            AppDatabase.getDatabase(LocalContext.current).tagDao(),
                            AppDatabase.getDatabase(LocalContext.current).ingredientDao(),
                            AppDatabase.getDatabase(LocalContext.current).instructionDao()
                        )
                    )
                )
                HomeView(navController = navControler, viewModel =viewModel)
            }
            composable(route = Views.SEARCH.name) {
                val context = LocalContext
                val viewModel = viewModel<SearchViewModel>(
                    factory = SearchViewModelFactory(
                        repository =  OfflineRecipeRepositary(AppDatabase.getDatabase(
                            LocalContext.current).recipeDao(),AppDatabase.getDatabase(
                            LocalContext.current).tagDao(),
                            AppDatabase.getDatabase(LocalContext.current).ingredientDao(),
                            AppDatabase.getDatabase(LocalContext.current).instructionDao()
                        ),
                        OfflineIngredinetRepositary(AppDatabase.getDatabase(LocalContext.current).ingredientDao()))
                )
                SearchView(viewModel = viewModel, goToDetail =  {
                    navControler.navigate(Views.RECIPEDETAIL.name + "/${it.id}")
                })
            }
            composable(route = Views.RECIPEDETAIL.name + "/{recipeId}", arguments = listOf(
                navArgument(name = "recipeId"){
                    type = NavType.LongType
                    nullable = false
                })) {
                val context = LocalContext.current
                val offlineRecipeRepo = OfflineRecipeRepositary(
                    AppDatabase.getDatabase(LocalContext.current).recipeDao(),
                    AppDatabase.getDatabase(LocalContext.current).tagDao(),
                    AppDatabase.getDatabase(LocalContext.current).ingredientDao(),
                    AppDatabase.getDatabase(LocalContext.current).instructionDao()
                )
                val offlineInstructionRepo = OfflineInstructionRepositary(AppDatabase.getDatabase(
                    LocalContext.current).instructionDao())
                val offlineIngredientRepo = OfflineIngredinetRepositary(AppDatabase.getDatabase(
                    LocalContext.current).ingredientDao())


                val viewModel = viewModel<RecipeDetailViewModel>(
                    factory = RecipeViewModelFactory(
                        offlineRecipeRepo,
                        offlineIngredientRepo,
                        offlineInstructionRepo,
                        navControler,
                    it.arguments?.getLong("recipeId") ?: 0,
                        context = context,
                        pref = dataStorePreferences))



                RecipeDetailView(
                     viewModel = viewModel)
            }
            composable(route = Views.IMPORT.name) {
                ImportView()
            }
            composable(route = Views.USERINFO.name) {
                val viewModel = viewModel<UserInfoViewModel>(factory = UserInfoViewModelFactory(dataStorePreferences, appContext = LocalContext.current))
                UserInfoView(viewModel = viewModel)
            }
        }
        NavigationBar(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp), containerColor = primaryColor) {


            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.HOME.name) }, icon = { Icon(Icons.Outlined.Home, contentDescription = "home") },modifier = Modifier.fillMaxSize())
            NavigationBarItem(selected = false, onClick = { navControler.navigate(Views.SEARCH.name) }, icon = { Icon(Icons.Outlined.Search, contentDescription = "search")})
                Button(
                    onClick = {

                        navControler.navigate(Views.RECIPEDETAIL.name + "/0")
                              },
                    shape = CircleShape,
                    modifier = Modifier
                        .wrapContentSize(unbounded = true, align = Alignment.TopCenter)
                        .size(70.dp)
                        .zIndex(1f)
                        .drawWithContent {
                            drawContent()
                        }  ,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),

                ) {
                    Icon(Icons.Filled.AddBox, contentDescription = "import", tint = primaryColor,modifier = Modifier.size(50.dp))
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

@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavPreview() {
    val dataStorePreferences: DataStore<Preferences> = remember{
        object : DataStore<Preferences>{
            override val data: Flow<Preferences>
                get() = flowOf()

            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
                TODO("Not yet implemented")
            }

        }

    }
    val db = AppDatabase.getDatabase(LocalContext.current)
    ChefsRecipiesTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Navigation(dataStorePreferences)
        }
    }
}