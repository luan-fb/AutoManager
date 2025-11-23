package com.luanferreira.automanager

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import com.luanferreira.automanager.presentation.navigation.NavGraph
import com.luanferreira.automanager.presentation.navigation.Rota
import com.luanferreira.automanager.core.ui.theme.AutoManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AutenticacaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AutoManagerTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentUser by authRepository.usuarioAtual.collectAsState(initial = null)
                    val destinoInicial =
                        if (currentUser != null) Rota.Home.caminho else Rota.Login.caminho
                    NavGraph(
                        navController = navController,
                        destinoInicial = destinoInicial,
                    )
                   }
                }
            }
        }
    }