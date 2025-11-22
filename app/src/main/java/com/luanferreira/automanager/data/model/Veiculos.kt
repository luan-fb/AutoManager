package com.luanferreira.automanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veiculos")
data class Veiculo(
    @PrimaryKey val id: String = "",
    val usuarioId: String = "",
    val placa: String = "",
    val modelo: String = "",
    val fabricante: String = "",
    val ano: Int = 0,
    val valorFipe: Double = 0.0,
    val dataUltimaRevisao: Long = 0L,
    val dataProximaRevisao: Long = 0L
) {
    constructor() : this("", "", "", "", "", 0, 0.0, 0L, 0L)
}