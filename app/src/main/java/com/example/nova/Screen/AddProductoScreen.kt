package com.example.nova.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nova.Model.Producto
import com.example.nova.Repository.ProductoRepository
import kotlinx.coroutines.launch

@Composable
fun AddProductoScreen(navController: NavController, productoRepository: ProductoRepository) {
    // Variables para almacenar los valores ingresados
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("Producto pendiente de agregar") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Agregar Producto", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    if (nombre.isNotEmpty() && precio.isNotEmpty() && stock.isNotEmpty()) {
                        try {
                            val producto = Producto(
                                nombre = nombre,
                                precio = precio.toDouble(),
                                stock = stock.toInt()
                            )
                            productoRepository.insert(producto)
                            resultado = "Producto agregado con éxito"
                        } catch (e: Exception) {
                            resultado = "Error al agregar producto: ${e.message}"
                        }
                    } else {
                        resultado = "Por favor, complete todos los campos"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = resultado, style = MaterialTheme.typography.bodyMedium)

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
