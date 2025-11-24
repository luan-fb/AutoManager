package com.luanferreira.automanager.domain.repository

import kotlinx.coroutines.flow.Flow

interface AutenticacaoRepository {
    val usuarioAtual: Flow<String?>
    fun login(email: String, senha: String): Flow<Result<Boolean>>
    fun cadastrar(nome: String, email: String, senha: String): Flow<Result<Boolean>>
    suspend fun logout()
}