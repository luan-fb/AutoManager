// Arquivo: app/src/main/java/com/luanferreira/automanager/domain/repository/AutenticacaoRepository.kt
package com.luanferreira.automanager.domain.repository

import kotlinx.coroutines.flow.Flow

interface AutenticacaoRepository {
    val usuarioAtual: Flow<String?>
    fun login(email: String, senha: String): Flow<Result<Boolean>>
    fun cadastrar(email: String, senha: String): Flow<Result<Boolean>>
    suspend fun logout()
}