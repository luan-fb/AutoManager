package com.luanferreira.automanager.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val msg: String) : AuthUiState()
}

@HiltViewModel
class AutenticacaoViewModel @Inject constructor(
    private val repository: AutenticacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            _uiState.value = AuthUiState.Error("Preencha email e senha")
            return
        }
        executarAuth { repository.login(email, senha) }
    }

    fun cadastrar(nome: String, email: String, senha: String, confirmarSenha: String) {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            _uiState.value = AuthUiState.Error("Preencha todos os campos")
            return
        }
        if (senha != confirmarSenha) {
            _uiState.value = AuthUiState.Error("As senhas não conferem")
            return
        }
        if (senha.length < 6) {
            _uiState.value = AuthUiState.Error("A senha deve ter no mínimo 6 caracteres")
            return
        }
        executarAuth { repository.cadastrar(nome, email, senha) }
    }

    private fun executarAuth(block: () -> kotlinx.coroutines.flow.Flow<Result<Boolean>>) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            block().collect { result ->
                result.onSuccess {
                    _uiState.value = AuthUiState.Success
                }.onFailure { erro ->
                    _uiState.value = AuthUiState.Error(traduzirErro(erro.message))
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun traduzirErro(msg: String?): String {
        return when {
            msg == null -> "Erro desconhecido"
            msg.contains("incorrect") -> "Email ou senha inválidos"
            msg.contains("email") -> "O endereço de e-mail está mal formatado."
            msg.contains("network") -> "Sem conexão com a internet"
            else -> msg
        }
    }
}