package com.luanferreira.automanager.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luanferreira.automanager.core.ui.componentes.AutoManagerTopBar
import com.luanferreira.automanager.data.model.Veiculo
import com.luanferreira.automanager.presentation.veiculo.VeiculoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCadastroVeiculo: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AutoManagerTopBar(
                titulo = "Meus Veiculos",
                podeVoltar = false,
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCadastroVeiculo,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Veículo")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HomeUiState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nenhum veículo cadastrado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                is HomeUiState.Error -> {
                    Text(
                        text = state.msg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HomeUiState.Success -> {
                    VeiculoList(veiculos = state.veiculos,
                        onDelete = { veiculo ->
                            viewModel.deletarVeiculo(veiculo)
                        })
                }
            }
        }
    }
}

@Composable
fun VeiculoList(
    veiculos: List<Veiculo>,
    onDelete: (Veiculo) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = veiculos,
            key = { it.id }
        ) { veiculo ->
            SwipeToDeleteContainer(
                item = veiculo,
                onDelete = { onDelete(veiculo) }
            ) {
                VeiculoCard(veiculo = veiculo)
            }
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun <T> SwipeToDeleteContainer(
        item: T,
        onDelete: (T) -> Unit,
        content: @Composable (T) -> Unit
    ) {
        val state = rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.StartToEnd) {
                    onDelete(item)
                    true
                } else {
                    false
                }
            }
        )

        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeDismissState = state)
            },
            content = { content(item) },
            enableDismissFromEndToStart = false
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeleteBackground(swipeDismissState: SwipeToDismissBoxState) {
        val color by animateColorAsState(
            if (swipeDismissState.targetValue == SwipeToDismissBoxValue.StartToEnd)
                Color.Red.copy(alpha = 0.8f) else Color.Transparent,
            label = "ColorAnimation"
        )

        val scale by animateFloatAsState(
            if (swipeDismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) 1.2f else 0.8f,
            label = "ScaleAnimation"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                modifier = Modifier.scale(scale),
                tint = Color.White
            )
        }
    }