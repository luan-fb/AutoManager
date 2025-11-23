package com.luanferreira.automanager.presentation.veiculo

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luanferreira.automanager.core.ui.componentes.AutoManagerTopBar
import com.luanferreira.automanager.utils.toDataLegivel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeiculoFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: VeiculoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Controles dos DatePickers
    var showDatePickerUltima by remember { mutableStateOf(false) }
    var showDatePickerProxima by remember { mutableStateOf(false) }

    // Side Effects
    LaunchedEffect(uiState) {
        when (uiState) {
            is VeiculoUiState.Success -> {
                Toast.makeText(context, "Veículo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.resetarEstado()
                onNavigateBack()
            }
            is VeiculoUiState.Error -> {
                Toast.makeText(context, (uiState as VeiculoUiState.Error).msg, Toast.LENGTH_LONG).show()
                viewModel.consumirEvento()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AutoManagerTopBar(
                titulo = "Novo Veículo",
                podeVoltar = true,
                onVoltar = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.salvarVeiculo()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                if (uiState is VeiculoUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Salvar")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Preencha os dados do veículo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 1. PLACA (Obrigatório)
            OutlinedTextField(
                value = formState.placa,
                onValueChange = {
                    if (it.length <= 7) {
                        viewModel.atualizarFormulario(formState.copy(placa = it))
                    }
                },
                label = { LabelObrigatorio("Placa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                )
            )

            // 2. ANO e VALOR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = formState.ano,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 4) {
                            viewModel.atualizarFormulario(formState.copy(ano = it))
                        }
                    },
                    label = { LabelObrigatorio("Ano") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = formState.valorFipe,
                    onValueChange = { viewModel.atualizarFormulario(formState.copy(valorFipe = it)) },
                    label = { Text("Valor Fipe") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )
            }

            // 3. MODELO (Obrigatório)
            OutlinedTextField(
                value = formState.modelo,
                onValueChange = { viewModel.atualizarFormulario(formState.copy(modelo = it)) },
                label = { LabelObrigatorio("Modelo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            // 4. FABRICANTE
            OutlinedTextField(
                value = formState.fabricante,
                onValueChange = { viewModel.atualizarFormulario(formState.copy(fabricante = it)) },
                label = { Text("Fabricante") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            // 5. DATAS DE MANUTENÇÃO
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Manutenção",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CampoData(
                    label = "Última Revisão *",
                    valor = formState.ultimaRevisao,
                    onClick = { showDatePickerUltima = true },
                    modifier = Modifier.weight(1f)
                )

                CampoData(
                    label = "Próxima Revisão *",
                    valor = formState.proximaRevisao,
                    onClick = { showDatePickerProxima = true },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // --- DIALOGS ---
    if (showDatePickerUltima) {
        VeiculoDatePickerDialog(
            onDateSelected = { viewModel.atualizarFormulario(formState.copy(ultimaRevisao = it)) },
            onDismiss = { showDatePickerUltima = false }
        )
    }

    if (showDatePickerProxima) {
        VeiculoDatePickerDialog(
            onDateSelected = { viewModel.atualizarFormulario(formState.copy(proximaRevisao = it)) },
            onDismiss = { showDatePickerProxima = false }
        )
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun LabelObrigatorio(texto: String) {
    Row {
        Text(texto)
        Text(" *", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun CampoData(
    label: String,
    valor: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor?.toDataLegivel() ?: "",
        onValueChange = {},
        label = {
            // Verifica se tem * para pintar de vermelho
            if (label.contains("*")) {
                LabelObrigatorio(label.replace(" *", ""))
            } else {
                Text(label)
            }
        },
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.DateRange, contentDescription = "Selecionar Data")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeiculoDatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}