package com.example.nova.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    tableName = "ventas",
    foreignKeys = [
        ForeignKey(entity = Cliente::class, parentColumns = ["id"], childColumns = ["clienteId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Producto::class, parentColumns = ["id"], childColumns = ["productoId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Venta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val productoId: Int,
    val cantidad: Int,
    val fecha: String
)
