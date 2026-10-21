package com.cafq.lab09

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
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val retrofitProducts = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioProducts = retrofitProducts.create(ProductApiService::class.java)

    val retrofitAmphibians = Retrofit.Builder()
        .baseUrl("https://amphibians-api.free.beeceptor.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicioAmphibians = retrofitAmphibians.create(AmphibianApiService::class.java)

    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { pv -> Contenido(pv, navController, servicioPosts, servicioProducts, servicioAmphibians) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = { Text(text = "JSONPlaceHolder Access", color = Color.White, fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }

    NavigationBar(containerColor = Color.LightGray) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Posts") },
            label = { Text("Inicio") },
            selected = pestañaSeleccionada == 0,
            onClick = {
                pestañaSeleccionada = 0
                navController.navigate("posts")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = "Catálogo Autos") },
            label = { Text("Autos") },
            selected = pestañaSeleccionada == 1,
            onClick = {
                pestañaSeleccionada = 1
                navController.navigate("autos")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Face, contentDescription = "Anfibios") },
            label = { Text("Anfibios") },
            selected = pestañaSeleccionada == 2,
            onClick = {
                pestañaSeleccionada = 2
                navController.navigate("amphibians")
            }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicioPosts: PostApiService,
    servicioProducts: ProductApiService,
    servicioAmphibians: AmphibianApiService
) {
    Box(modifier = Modifier.fillMaxSize().padding(pv)) {
        NavHost(navController = navController, startDestination = "posts") {
            composable("posts") { ScreenPosts(navController, servicioPosts) }
            composable("postsVer/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ScreenPost(navController, servicioPosts, it.arguments!!.getInt("id"))
            }
            composable("autos") { ScreenAutos(navController, servicioProducts) }
            composable("autoVer/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ScreenAutoDetail(navController, servicioProducts, it.arguments!!.getInt("id"))
            }
            composable("amphibians") { ScreenAmphibians(servicioAmphibians) }
        }
    }
}
