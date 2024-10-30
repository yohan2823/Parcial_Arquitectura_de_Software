package com.example.nova.Repository

import com.example.nova.DAO.ClienteDao
import com.example.nova.Model.Cliente

class ClienteRepository(private val clienteDao: ClienteDao) {
    suspend fun insert(cliente: Cliente) = clienteDao.insert(cliente)
    suspend fun update(cliente: Cliente) = clienteDao.update(cliente)
    suspend fun delete(cliente: Cliente) = clienteDao.delete(cliente)
    suspend fun getAll(): List<Cliente> = clienteDao.getAll()

    suspend fun getClienteById(id: Int): Cliente? = clienteDao.getClienteById(id)
}
