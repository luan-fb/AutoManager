package com.luanferreira.automanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import com.luanferreira.automanager.ui.navigation.NavGraph
import com.luanferreira.automanager.ui.navigation.Rota
import com.luanferreira.automanager.ui.theme.AutoManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AutenticacaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutoManagerTheme {
                val navController = rememberNavController()
                val currentUser by authRepository.usuarioAtual.collectAsState(initial = null)
                val destinoInicial = if (currentUser != null) Rota.Home.caminho else Rota.Login.caminho

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        destinoInicial = destinoInicial,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}