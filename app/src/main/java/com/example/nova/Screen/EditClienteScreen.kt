package com.example.nova.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nova.Model.Cliente
import com.example.nova.Repository.ClienteRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClienteScreen(
    navController: NavController,
    clienteId: Int,
    clienteRepository: ClienteRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var cliente by remember { mutableStateOf<Cliente?>(null) }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensajeEstado by remember { mutableStateOf("") }

    LaunchedEffect(clienteId) {
        cliente = clienteRepository.getClienteById(clienteId)
        cliente?.let {
            nombre = it.nombre
            correo = it.correo
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Cliente") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            cliente?.let {
                                val updatedCliente = it.copy(nombre = nombre, correo = correo)
                                clienteRepository.update(updatedCliente)
                                mensajeEstado = "Cliente actualizado exitosamente"
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = mensajeEstado, style = MaterialTheme.typography.bodySmall)

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
    )
}
