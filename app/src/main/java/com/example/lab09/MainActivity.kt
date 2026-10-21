package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgPrincipal9()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgPrincipal9() {
    val retrofitPosts = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioPosts = retrofitPosts.create(PostApiService::class.java)

    val retrofitRecipes = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioRecipes = retrofitRecipes.create(RecipeApiService::class.java)

    val retrofitAmphibians = Retrofit.Builder()
        .baseUrl("https://android-tasks-api.fitnessview.workers.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioAmphibians = retrofitAmphibians.create(AmphibianApiService::class.java)

    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { pv -> Contenido(pv, navController, servicioPosts, servicioRecipes, servicioAmphibians) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Lab 09 - Consumo de APIs", color = Color.White, fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(containerColor = Color.LightGray) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Posts") },
            label = { Text("Posts") },
            selected = true,
            onClick = { navController.navigate("posts") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.List, contentDescription = "Recetas") },
            label = { Text("Recetas") },
            selected = false,
            onClick = { navController.navigate("recipes") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Face, contentDescription = "Anfibios") },
            label = { Text("Anfibios") },
            selected = false,
            onClick = { navController.navigate("amphibians") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicioPosts: PostApiService,
    servicioRecipes: RecipeApiService,
    servicioAmphibians: AmphibianApiService
) {
    Box(modifier = Modifier.fillMaxSize().padding(pv)) {
        NavHost(navController = navController, startDestination = "posts") {
            composable("posts") { ScreenPosts(navController, servicioPosts) }
            composable("postsVer/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ScreenPost(navController, servicioPosts, it.arguments!!.getInt("id"))
            }
            composable("recipes") { ScreenRecipes(navController, servicioRecipes) }
            composable("recipeVer/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ScreenRecipeDetail(navController, servicioRecipes, it.arguments!!.getInt("id"))
            }
            composable("amphibians") { ScreenAmphibians(servicioAmphibians) }
        }
    }
}
