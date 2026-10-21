package com.example.lab09

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ScreenAmphibians(servicio: AmphibianApiService) {
    val listaAnfibios: SnapshotStateList<AmphibianModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val resultado = servicio.getAmphibians()
        resultado.forEach { listaAnfibios.add(it) }
    }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(listaAnfibios) { anfibio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${anfibio.name} (${anfibio.type})",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = anfibio.imgSrc,
                        contentDescription = anfibio.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = anfibio.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
