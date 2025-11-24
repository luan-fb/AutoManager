package com.luanferreira.automanager.presentation.navigation

sealed class Rotas(val caminho: String) {
    object Login : Rotas("login_screen")
    object Cadastro : Rotas("cadastro_screen")

    object Home : Rotas("home_screen")
    object FormularioVeiculo : Rotas("veiculo_form_screen")
}