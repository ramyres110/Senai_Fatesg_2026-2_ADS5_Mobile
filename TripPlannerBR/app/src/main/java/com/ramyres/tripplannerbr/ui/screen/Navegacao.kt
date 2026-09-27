package com.ramyres.tripplannerbr.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ramyres.tripplannerbr.data.BancoDeDados
import com.ramyres.tripplannerbr.model.repository.ViagemRepository

@Composable
fun Navegacao() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = remember { BancoDeDados.getDatabase(context) }
    val repository = remember { ViagemRepository(database) }
    val factory = remember { ViagemViewModelFactory(repository) }

    NavHost(
        navController = navController,
        startDestination = AreaDePousoEndpoint,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<AreaDePousoEndpoint> {
            val viewModel: AreaDePousoViewModel = viewModel(factory = factory)
            AreaDePousoScreen(
                viewModel = viewModel,
                onNavigateToNovoDestino = { navController.navigate(CadastroDestinoEndpoint) },
                onNavigateToDetalhes = { destinoId -> navController.navigate(DestinoDetalheEndpoint(destinoId)) },
                onNavigateToPerfil = { navController.navigate(PerfilUsuarioEndpoint) }
            )
        }

        composable<CadastroDestinoEndpoint> {
            val viewModel: CadastroDestinoViewModel = viewModel(factory = factory)
            CadastroDestinoScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<DestinoDetalheEndpoint> { backStackEntry ->
            val route: DestinoDetalheEndpoint = backStackEntry.toRoute()
            val viewModel: DestinoDetalheViewModel = viewModel(factory = factory)
            DestinoDetalheScreen(
                destinoId = route.destinoId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<PerfilUsuarioEndpoint> {
            val viewModel: PerfilUsuarioViewModel = viewModel(factory = factory)
            PerfilUsuarioScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
