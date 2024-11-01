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
import com.example.nova.Model.Cliente
import com.example.nova.Repository.ClienteRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(navController: NavController, clienteRepository: ClienteRepository) {
    val clientes = remember { mutableStateListOf<Cliente>() }
    val coroutineScope = rememberCoroutineScope()
    var clienteToDelete by remember { mutableStateOf<Cliente?>(null) }


    LaunchedEffect(Unit) {
        clientes.clear()
        clientes.addAll(clienteRepository.getAll())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddClienteScreen") },
                modifier = Modifier.padding(bottom = 80.dp)
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
            Text(
                text = "Lista de Clientes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(clientes) { cliente ->
                    ClienteItem(
                        cliente = cliente,
                        onEditClick = { selectedCliente ->
                            navController.navigate("editCliente/${selectedCliente.id}")
                        },
                        onDeleteClick = { selectedCliente ->
                            clienteToDelete = selectedCliente
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar")
            }
        }

        if (clienteToDelete != null) {
            AlertDialog(
                onDismissRequest = { clienteToDelete = null },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de que desea eliminar este cliente?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                clienteToDelete?.let { cliente ->
                                    clienteRepository.delete(cliente)
                                    clientes.remove(cliente)
                                }
                                clienteToDelete = null
                            }
                        }
                    ) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { clienteToDelete = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ClienteItem(
    cliente: Cliente,
    onEditClick: (Cliente) -> Unit,
    onDeleteClick: (Cliente) -> Unit
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
                Text(text = cliente.nombre, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Correo: ${cliente.correo}", style = MaterialTheme.typography.bodyMedium)
            }

            // Botones de Editar y Eliminar
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onEditClick(cliente) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar Cliente",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { onDeleteClick(cliente) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar Cliente",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
