package com.cafq.lab09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenRecipes(navController: NavHostController, servicio: RecipeApiService) {
    val listaRecetas: SnapshotStateList<RecipeModel> = remember { mutableStateListOf() }
    var textoBusqueda by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val respuesta = servicio.getRecipes()
        respuesta.recipes.forEach { listaRecetas.add(it) }
    }

    val recetasFiltradas = listaRecetas.filter {
        it.name.contains(textoBusqueda, ignoreCase = true) ||
        it.cuisine.contains(textoBusqueda, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Barra de búsqueda personalizada para el menú del restaurante
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar platillo o cocina...") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        LazyColumn {
            items(recetasFiltradas) { receta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = receta.image,
                            contentDescription = receta.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = receta.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Chip de categoría / cocina
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = receta.cuisine,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Rating de estrellas
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${receta.rating} / 5.0 (${receta.difficulty})",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = r.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Cocina: ${r.cuisine}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "⭐ ${r.rating}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(text = "Ingredientes del Platillo:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            r.ingredients.forEach { ingrediente ->
                Text(text = "• $ingrediente", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Pasos de Preparación:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            r.instructions.forEachIndexed { index, paso ->
                Text(text = "${index + 1}. $paso", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
