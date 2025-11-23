package com.luanferreira.automanager.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luanferreira.automanager.presentation.auth.LoginScreen
import com.luanferreira.automanager.presentation.auth.CadastroScreen
import com.luanferreira.automanager.presentation.home.HomeScreen
import com.luanferreira.automanager.presentation.veiculo.VeiculoFormScreen

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
                    navController.navigate(Rota.Home.caminho) {
                        popUpTo(Rota.Login.caminho) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Rota.Home.caminho) {
            HomeScreen(
                onNavigateToCadastroVeiculo = {
                    navController.navigate(Rota.FormularioVeiculo.caminho)
                },
                onLogout = {
                    navController.navigate(Rota.Login.caminho) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Rota.FormularioVeiculo.caminho) {
            VeiculoFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
