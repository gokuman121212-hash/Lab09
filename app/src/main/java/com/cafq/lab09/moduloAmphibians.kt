package com.cafq.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ScreenAmphibians(servicio: AmphibianApiService) {
    val listaAnfibios: SnapshotStateList<AmphibianModel> = remember { mutableStateListOf() }
    var cargando by remember { mutableStateOf(true) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val resultado = servicio.getAmphibians()
            listaAnfibios.clear()
            resultado.forEach { listaAnfibios.add(it) }
            cargando = false
        } catch (e: Exception) {
            Log.e("AMPHIBIANS_ERROR", "Error al cargar anfibios: ${e.message}")
            errorMensaje = "No se pudieron cargar los datos de los anfibios."
            cargando = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMensaje != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = errorMensaje!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                items(listaAnfibios) { anfibio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${anfibio.name} (${anfibio.type})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = anfibio.imgSrc,
                                contentDescription = anfibio.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
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
    }
}
