package com.example.nova.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nova.Model.Cliente
import com.example.nova.Model.Producto
import com.example.nova.Model.Venta
import com.example.nova.Repository.ClienteRepository
import com.example.nova.Repository.ProductoRepository
import com.example.nova.Repository.VentaRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AddVentaScreen(
    navController: NavController,
    productoRepository: ProductoRepository,
    clienteRepository: ClienteRepository,
    ventaRepository: VentaRepository
) {
    val scope = rememberCoroutineScope()

    val productos = remember { mutableStateOf(listOf<Producto>()) }
    val clientes = remember { mutableStateOf(listOf<Cliente>()) }

    LaunchedEffect(Unit) {
        productos.value = productoRepository.getAll()
        clientes.value = clienteRepository.getAll()
    }

    var selectedProducto by remember { mutableStateOf(productos.value.firstOrNull()) }
    var selectedCliente by remember { mutableStateOf(clientes.value.firstOrNull()) }
    var cantidad by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("Venta pendiente") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Nueva Venta", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector(
            label = "Seleccione Producto",
            items = productos.value.map { it.nombre },
            selectedItem = selectedProducto?.nombre ?: "",
            onItemSelected = { nombre ->
                selectedProducto = productos.value.find { it.nombre == nombre }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownSelector(
            label = "Seleccione Cliente",
            items = clientes.value.map { it.nombre },
            selectedItem = selectedCliente?.nombre ?: "",
            onItemSelected = { nombre ->
                selectedCliente = clientes.value.find { it.nombre == nombre }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    if (selectedProducto != null && selectedCliente != null && cantidad.isNotEmpty()) {
                        ventaRepository.insert(
                            Venta(
                                id = 0,
                                productoId = selectedProducto!!.id,
                                clienteId = selectedCliente!!.id,
                                cantidad = cantidad.toInt(),
                                fecha = fecha
                            )
                        )
                        resultado = "Venta agregada con éxito"
                    } else {
                        resultado = "Seleccione todos los campos y asegúrese de ingresar la cantidad"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = resultado, fontSize = 20.sp)

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

@Composable
fun DropdownSelector(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Expand dropdown"
                    )
                }
            }
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    text = { Text(item) }
                )
            }
        }
    }
}
