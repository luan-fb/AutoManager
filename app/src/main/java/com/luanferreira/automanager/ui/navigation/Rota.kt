package com.luanferreira.automanager.ui.navigation

sealed class Rota(val caminho: String) {
    object Login : Rota("login_screen")
    object Cadastro : Rota("cadastro_screen")

    object Home : Rota("home_screen")
    object FormularioVeiculo : Rota("veiculo_form_screen")
}