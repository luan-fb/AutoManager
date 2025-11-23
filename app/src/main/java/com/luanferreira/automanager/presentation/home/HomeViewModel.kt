package com.luanferreira.automanager.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luanferreira.automanager.data.model.Veiculo
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import com.luanferreira.automanager.domain.repository.VeiculoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val veiculos: List<Veiculo>) : HomeUiState()
    object Empty : HomeUiState()
    data class Error(val msg: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val veiculoRepository: VeiculoRepository,
    private val authRepository: AutenticacaoRepository
) : ViewModel() {

    // O stateIn transforma o Flow (fluxo contínuo) em StateFlow (estado observável p/ Compose)
    val uiState: StateFlow<HomeUiState> = veiculoRepository.listarVeiculos()
        .map { lista ->
            if (lista.isEmpty()) HomeUiState.Empty else HomeUiState.Success(lista)
        }
        .catch { erro ->
            emit(HomeUiState.Error(erro.message ?: "Erro ao carregar veículos"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantém vivo por 5s após sair da tela (rotação)
            initialValue = HomeUiState.Loading
        )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            veiculoRepository.limparSessao()
        }
    }
}