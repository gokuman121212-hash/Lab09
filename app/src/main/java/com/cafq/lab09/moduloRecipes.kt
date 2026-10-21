package com.cafq.lab09

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenRecipes(navController: NavHostController, servicio: RecipeApiService) {
    val listaRecetas: SnapshotStateList<RecipeModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val respuesta = servicio.getRecipes()
        respuesta.recipes.forEach { listaRecetas.add(it) }
    }
    LazyColumn {
        items(listaRecetas) { receta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = receta.image,
                        contentDescription = receta.name,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = receta.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Cocina: ${receta.cuisine}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Dificultad: ${receta.difficulty}", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(
                        onClick = { navController.navigate("recipeVer/${receta.id}") }
                    ) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver receta")
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenRecipeDetail(navController: NavHostController, servicio: RecipeApiService, id: Int) {
    var receta by remember { mutableStateOf<RecipeModel?>(null) }
    LaunchedEffect(Unit) {
        receta = servicio.getRecipeById(id)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        receta?.let { r ->
            AsyncImage(
                model = r.image,
                contentDescription = r.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = r.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Valoración: ${r.rating} / 5.0", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ingredientes:", style = MaterialTheme.typography.titleSmall)
            Text(text = r.ingredients.joinToString(", "))
        }
    }
}
