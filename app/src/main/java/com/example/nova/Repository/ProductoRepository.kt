package com.example.nova.Repository

import com.example.nova.DAO.ProductoDao
import com.example.nova.Model.Producto

class ProductoRepository(private val productoDao: ProductoDao) {
    suspend fun insert(producto: Producto) = productoDao.insert(producto)

    suspend fun updateProducto(producto: Producto) = productoDao.update(producto)

    suspend fun delete(producto: Producto) = productoDao.delete(producto)

    suspend fun getAll(): List<Producto> = productoDao.getAll()

    suspend fun getProductoById(id: Int): Producto? = productoDao.getProductoById(id)
}
