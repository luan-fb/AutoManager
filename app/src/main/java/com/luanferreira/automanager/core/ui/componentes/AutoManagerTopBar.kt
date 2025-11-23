package com.luanferreira.automanager.core.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoManagerTopBar(
    titulo: String,
    podeVoltar: Boolean = false, // Controla se mostra a seta
    onVoltar: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    // Cores do seu tema (Roxo -> Rosa fica muito bonito)
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Texto Branco para contraste
                )
            )
        },
        navigationIcon = {
            if (podeVoltar && onVoltar != null) {
                IconButton(onClick = onVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White // Ícone Branco
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(primary, tertiary)
            )
        )
    )
}