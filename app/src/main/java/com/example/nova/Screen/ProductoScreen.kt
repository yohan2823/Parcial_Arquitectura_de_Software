package com.example.nova.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nova.Model.Producto
import com.example.nova.Repository.ProductoRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(navController: NavController, productoRepository: ProductoRepository) {
    val productos = remember { mutableStateListOf<Producto>() }
    val coroutineScope = rememberCoroutineScope()
    var productoToDelete by remember { mutableStateOf<Producto?>(null) }

    LaunchedEffect(Unit) {
        productos.clear()
        productos.addAll(productoRepository.getAll())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos de Tecnología") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addProducto") },
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
            // Lista de productos existentes
            Text("Lista de Productos", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 16.dp))

            LazyColumn {
                items(productos) { producto ->
                    ProductoItem(
                        producto = producto,
                        onEditClick = { selectedProducto ->
                            navController.navigate("editProducto/${selectedProducto.id}")
                        },
                        onDeleteClick = { selectedProducto ->
                            productoToDelete = selectedProducto
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
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

        if (productoToDelete != null) {
            AlertDialog(
                onDismissRequest = { productoToDelete = null },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de que desea eliminar este producto?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                productoToDelete?.let { producto ->
                                    productoRepository.delete(producto)
                                    productos.remove(producto)
                                }
                                productoToDelete = null
                            }
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { productoToDelete = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    onEditClick: (Producto) -> Unit,
    onDeleteClick: (Producto) -> Unit
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = producto.nombre, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Precio: ${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Stock: ${producto.stock}", style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onEditClick(producto) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar Producto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { onDeleteClick(producto) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar Producto",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
