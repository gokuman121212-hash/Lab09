package com.cafq.lab09

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
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
    val listaAutos: SnapshotStateList<RecipeModel> = remember { mutableStateListOf() }
    var textoBusqueda by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val respuesta = servicio.getRecipes()
        respuesta.recipes.forEach { listaAutos.add(it) }
    }

    val autosFiltrados = listaAutos.filter {
        it.name.contains(textoBusqueda, ignoreCase = true) ||
        it.cuisine.contains(textoBusqueda, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Buscador para el catálogo automotriz
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar auto, marca o categoría...") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar Auto") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        LazyColumn {
            items(autosFiltrados) { auto ->
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
                            model = auto.image,
                            contentDescription = auto.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(95.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = auto.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Badge del tipo/estilo de vehículo
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = auto.cuisine,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Puntuación / Garantía
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${auto.rating} / 5.0 (Garantía de fábrica)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        IconButton(
                            onClick = { navController.navigate("recipeVer/${auto.id}") }
                        ) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver vehículo")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenRecipeDetail(navController: NavHostController, servicio: RecipeApiService, id: Int) {
    var auto by remember { mutableStateOf<RecipeModel?>(null) }

    LaunchedEffect(Unit) {
        auto = servicio.getRecipeById(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        auto?.let { a ->
            AsyncImage(
                model = a.image,
                contentDescription = a.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = a.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Categoría: ${a.cuisine}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "⭐ ${a.rating}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(text = "Equipamiento y Características:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            a.ingredients.forEach { item ->
                Text(text = "• $item", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Especificaciones Técnicas:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            a.instructions.forEachIndexed { index, paso ->
                Text(text = "${index + 1}. $paso", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
