package com.example.nova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nova.Screen.*
import com.example.nova.Repository.ClienteRepository
import com.example.nova.Repository.ProductoRepository
import com.example.nova.Repository.VentaRepository
import com.example.nova.Database.AppDatabase
import com.example.nova.ui.theme.NovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val productoDao = database.productoDao()
        val clienteDao = database.clienteDao()
        val ventaDao = database.ventaDao()

        val productoRepository = ProductoRepository(productoDao)
        val clienteRepository = ClienteRepository(clienteDao)
        val ventaRepository = VentaRepository(ventaDao)

        setContent {
            NovaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "HomeScreen") {
                    composable("HomeScreen") {
                        HomeScreen(navController = navController)
                    }
                    composable("ProductoScreen") {
                        ProductoScreen(
                            navController = navController,
                            productoRepository = productoRepository
                        )
                    }
                    composable("ClienteScreen") {
                        ClienteScreen(
                            navController = navController,
                            clienteRepository = clienteRepository
                        )
                    }
                    composable("VentaScreen") {
                        VentaScreen(
                            navController = navController,
                            ventaRepository = ventaRepository,
                            productoRepository = productoRepository,
                            clienteRepository = clienteRepository
                        )
                    }
                    composable("AddVentaScreen") {
                        AddVentaScreen(
                            navController = navController,
                            ventaRepository = ventaRepository,
                            productoRepository = productoRepository,
                            clienteRepository = clienteRepository
                        )
                    }
                    composable("AddClienteScreen") {
                        AddClienteScreen(
                            navController = navController,
                            clienteRepository = clienteRepository
                        )
                    }
                    composable("addProducto") {
                        AddProductoScreen(navController, productoRepository)
                    }
                    composable("editProducto/{id}") { backStackEntry ->
                        val productoId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
                        if (productoId != -1) {
                            EditProductoScreen(navController, productoId, productoRepository)
                        } else {
                            navController.popBackStack()
                        }
                    }
                    composable("editCliente/{id}") { backStackEntry ->
                        val clienteId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
                        if (clienteId != -1) {
                            EditClienteScreen(navController, clienteId, clienteRepository)
                        } else {
                            navController.popBackStack()
                        }
                    }
                    composable("editVenta/{id}") { backStackEntry ->
                        val ventaId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
                        if (ventaId != -1) {
                            EditVentaScreen(
                                navController = navController,
                                ventaId = ventaId,
                                ventaRepository = ventaRepository,
                                productoRepository = productoRepository,
                                clienteRepository = clienteRepository
                            )
                        } else {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
