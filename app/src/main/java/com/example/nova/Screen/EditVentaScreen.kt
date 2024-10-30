package com.example.nova.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nova.Model.Venta
import com.example.nova.Repository.VentaRepository
import com.example.nova.Repository.ProductoRepository
import com.example.nova.Repository.ClienteRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVentaScreen(
    navController: NavController,
    ventaId: Int,
    ventaRepository: VentaRepository,
    productoRepository: ProductoRepository,
    clienteRepository: ClienteRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var venta by remember { mutableStateOf<Venta?>(null) }
    var productoNombre by remember { mutableStateOf("") }
    var clienteNombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    LaunchedEffect(ventaId) {
        venta = ventaRepository.getVentaById(ventaId)
        venta?.let {
            productoNombre = productoRepository.getProductoById(it.productoId)?.nombre ?: "Desconocido"
            clienteNombre = clienteRepository.getClienteById(it.clienteId)?.nombre ?: "Desconocido"
            cantidad = it.cantidad.toString()
            fecha = it.fecha
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Venta") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Producto: $productoNombre", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Cliente: $clienteNombre", style = MaterialTheme.typography.bodyLarge)

            TextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        venta?.let {
                            val updatedVenta = it.copy(
                                cantidad = cantidad.toInt(),
                                fecha = fecha
                            )
                            ventaRepository.update(updatedVenta)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
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
                Button(
                    onClick = { navController.navigate("HomeScreen") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Inicio")
                }
            }
        }
    }
}
