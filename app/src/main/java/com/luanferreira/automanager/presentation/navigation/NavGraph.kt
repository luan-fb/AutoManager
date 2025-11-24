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
        composable(route = Rotas.Login.caminho) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Rotas.Home.caminho) {
                        popUpTo(Rotas.Login.caminho) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Rotas.Cadastro.caminho)
                }
            )
        }

        composable(route = Rotas.Cadastro.caminho) {
            CadastroScreen(
                onCadastroSuccess = {
                    navController.navigate(Rotas.Home.caminho) {
                        popUpTo(Rotas.Login.caminho) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Rotas.Home.caminho) {
            HomeScreen(
                onNavigateToCadastroVeiculo = {
                    navController.navigate(Rotas.FormularioVeiculo.caminho)
                },
                onLogout = {
                    navController.navigate(Rotas.Login.caminho) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Rotas.FormularioVeiculo.caminho) {
            VeiculoFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
