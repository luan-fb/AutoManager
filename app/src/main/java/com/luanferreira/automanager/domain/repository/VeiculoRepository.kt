package com.luanferreira.automanager.domain.repository

import com.luanferreira.automanager.data.model.Veiculo
import kotlinx.coroutines.flow.Flow

interface VeiculoRepository {
    fun listarVeiculos(): Flow<List<Veiculo>>
    suspend fun salvarVeiculo(veiculo: Veiculo): Result<Unit>
    suspend fun sincronizarVeiculos()
    fun limparSessao()

    suspend fun deletarVeiculo(veiculoId: String): Result<Unit>
}