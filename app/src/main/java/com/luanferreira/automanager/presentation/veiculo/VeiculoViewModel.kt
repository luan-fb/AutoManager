package com.luanferreira.automanager.presentation.veiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luanferreira.automanager.data.model.Veiculo
import com.luanferreira.automanager.domain.repository.VeiculoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VeiculoUiState {
    object Idle : VeiculoUiState()
    object Loading : VeiculoUiState()
    object Success : VeiculoUiState()
    data class Error(val msg: String) : VeiculoUiState()
}

data class VeiculoFormState(
    val placa: String = "",
    val modelo: String = "",
    val fabricante: String = "",
    val ano: String = "",
    val valorFipe: String = "",
    val ultimaRevisao: Long? = null,
    val proximaRevisao: Long? = null
)

@HiltViewModel
class VeiculoViewModel @Inject constructor(
    private val repository: VeiculoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VeiculoUiState>(VeiculoUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(VeiculoFormState())
    val formState = _formState.asStateFlow()

    fun atualizarFormulario(novoEstado: VeiculoFormState) {
        _formState.value = novoEstado
    }

    fun consumirEvento() {
        _uiState.value = VeiculoUiState.Idle
    }

    fun salvarVeiculo() {
        val form = _formState.value

        if (form.placa.isBlank() ||
            form.modelo.isBlank() ||
            form.ano.isBlank() ||
            form.ultimaRevisao == null ||
            form.proximaRevisao == null
        ) {
            _uiState.value = VeiculoUiState.Error("Preencha todos os campos obrigatórios (*)")
            return
        }

        val dataAtual = System.currentTimeMillis()

        if (form.proximaRevisao < dataAtual) {
            _uiState.value = VeiculoUiState.Error("A Próxima Revisão deve ser uma data futura")
            return
        }

        if (form.ultimaRevisao > form.proximaRevisao) {
            _uiState.value = VeiculoUiState.Error("A Próxima Revisão deve ser após a Última")
            return
        }

        viewModelScope.launch {
            _uiState.value = VeiculoUiState.Loading

            val veiculo = Veiculo(
                placa = form.placa.uppercase(),
                modelo = form.modelo,
                fabricante = if (form.fabricante.isBlank()) "Não informado" else form.fabricante,
                ano = form.ano.toIntOrNull() ?: 0,
                valorFipe = form.valorFipe.replace(",", ".").toDoubleOrNull() ?: 0.0,
                dataUltimaRevisao = form.ultimaRevisao ?: 0L,
                dataProximaRevisao = form.proximaRevisao ?: 0L
            )

            val resultado = repository.salvarVeiculo(veiculo)

            resultado.onSuccess {
                _uiState.value = VeiculoUiState.Success
            }.onFailure { erro ->
                _uiState.value = VeiculoUiState.Error(erro.message ?: "Erro ao salvar veículo")
            }
        }
    }

    fun resetarEstado() {
        _uiState.value = VeiculoUiState.Idle
        _formState.value = VeiculoFormState()
    }
}