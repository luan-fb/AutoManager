package com.luanferreira.automanager.data.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luanferreira.automanager.data.model.Veiculo
import kotlinx.coroutines.flow.Flow

@Dao
interface VeiculoDao {

    @Query("SELECT * FROM veiculos WHERE usuarioId = :usuarioId")
    fun listarPorUsuario(usuarioId: String): Flow<List<Veiculo>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun salvar(veiculo: Veiculo)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun salvarTodos(veiculos: List<Veiculo>)

    @Query("DELETE FROM veiculos WHERE id = :id")
    suspend fun deletar(id: String)

    @Query("DELETE FROM veiculos")
    suspend fun deletarTodos()
}