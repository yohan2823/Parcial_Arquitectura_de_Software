package com.example.nova.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nova.Model.Venta
import com.example.nova.Repository.VentaRepository
import com.example.nova.Repository.ProductoRepository
import com.example.nova.Repository.ClienteRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaScreen(
    navController: NavController,
    ventaRepository: VentaRepository,
    productoRepository: ProductoRepository,
    clienteRepository: ClienteRepository
) {
    val ventas = remember { mutableStateListOf<Venta>() }
    val coroutineScope = rememberCoroutineScope()
    val productos = remember { mutableMapOf<Int, String>() }
    val clientes = remember { mutableMapOf<Int, String>() }
    var ventaToDelete by remember { mutableStateOf<Venta?>(null) }

    LaunchedEffect(Unit) {
        ventas.clear()
        ventas.addAll(ventaRepository.getAll())

        productos.putAll(productoRepository.getAll().associate { it.id to it.nombre })
        clientes.putAll(clienteRepository.getAll().associate { it.id to it.nombre })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ventas") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddVentaScreen") },
                modifier = Modifier.padding(bottom = 70.dp)
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Lista de Ventas", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(ventas) { venta ->
                    VentaItem(
                        venta = venta,
                        productoNombre = productos[venta.productoId] ?: "Desconocido",
                        clienteNombre = clientes[venta.clienteId] ?: "Desconocido",
                        onEditClick = { selectedVenta ->
                            navController.navigate("editVenta/${selectedVenta.id}")
                        },
                        onDeleteClick = { selectedVenta ->
                            ventaToDelete = selectedVenta
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Regresar")
                }
                Spacer(modifier = Modifier.width(8.dp))

            }
        }

        if (ventaToDelete != null) {
            AlertDialog(
                onDismissRequest = { ventaToDelete = null },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de que desea eliminar esta venta?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                ventaToDelete?.let { venta ->
                                    ventaRepository.delete(venta)
                                    ventas.remove(venta)
                                }
                                ventaToDelete = null
                            }
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { ventaToDelete = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun VentaItem(
    venta: Venta,
    productoNombre: String,
    clienteNombre: String,
    onEditClick: (Venta) -> Unit,
    onDeleteClick: (Venta) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Producto: $productoNombre", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Cliente: $clienteNombre", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Cantidad: ${venta.cantidad}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Fecha: ${venta.fecha}", style = MaterialTheme.typography.bodyMedium)
            }

            Row {
                IconButton(onClick = { onEditClick(venta) }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar Venta")
                }
                IconButton(
                    onClick = { onDeleteClick(venta) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar Venta")
                }
            }
        }
    }
}
