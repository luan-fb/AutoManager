package com.luanferreira.automanager.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luanferreira.automanager.ui.auth.LoginScreen
import com.luanferreira.automanager.ui.auth.CadastroScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    destinoInicial: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = destinoInicial,
        modifier = modifier
    ) {
        // 1. Tela de Login
        composable(route = Rota.Login.caminho) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Rota.Home.caminho) {
                        popUpTo(Rota.Login.caminho) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Rota.Cadastro.caminho)
                }
            )
        }

        composable(route = Rota.Cadastro.caminho) {
            CadastroScreen(
                onCadastroSuccess = {
                    // Ao cadastrar com sucesso, vai direto para Home
                    navController.navigate(Rota.Home.caminho) {
                        popUpTo(Rota.Login.caminho) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ... (Rotas Home e FormularioVeiculo continuam iguais)

    }
}