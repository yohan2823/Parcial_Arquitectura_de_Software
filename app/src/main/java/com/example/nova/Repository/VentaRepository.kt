package com.example.nova.Repository

import com.example.nova.DAO.VentaDao
import com.example.nova.Model.Venta

class VentaRepository(private val ventaDao: VentaDao) {
    suspend fun insert(venta: Venta) = ventaDao.insert(venta)
    suspend fun update(venta: Venta) = ventaDao.update(venta)
    suspend fun delete(venta: Venta) = ventaDao.delete(venta)
    suspend fun getAll(): List<Venta> = ventaDao.getAll()

    suspend fun getVentaById(id: Int): Venta? = ventaDao.getVentaById(id)
}
