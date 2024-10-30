package com.example.nova.DAO



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nova.Model.Venta

@Dao
interface VentaDao {
    @Insert
    suspend fun insert(venta: Venta)

    @Update
    suspend fun update(venta: Venta)

    @Delete
    suspend fun delete(venta: Venta)

    @Query("SELECT * FROM ventas")
    suspend fun getAll(): List<Venta>

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun getVentaById(id: Int): Venta?
}