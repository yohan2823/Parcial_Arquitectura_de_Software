package com.example.nova.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nova.Model.Producto

@Dao
interface ProductoDao {
    @Insert
    suspend fun insert(producto: Producto)

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)

    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<Producto>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getProductoById(id: Int): Producto?
}
