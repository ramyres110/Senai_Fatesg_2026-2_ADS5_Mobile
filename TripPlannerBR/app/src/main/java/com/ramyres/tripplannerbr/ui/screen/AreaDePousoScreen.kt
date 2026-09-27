package com.ramyres.tripplannerbr.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramyres.tripplannerbr.R
import com.ramyres.tripplannerbr.data.Destino
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
object AreaDePousoEndpoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaDePousoScreen(
    viewModel: AreaDePousoViewModel,
    onNavigateToNovoDestino: () -> Unit,
    onNavigateToDetalhes: (Long) -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.carregarDados()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToPerfil) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.user_profile)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNovoDestino) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_destination)
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Header com informação da Origem
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onNavigateToPerfil() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            val defaultTraveler = stringResource(R.string.traveler)
                            val nomeUsuario = state.usuario?.nome?.ifBlank { defaultTraveler } ?: defaultTraveler
                            Text(
                                text = stringResource(R.string.hello_user, nomeUsuario),
                                style = MaterialTheme.typography.titleMedium
                            )
                            val origemText = if (state.origem?.cidade?.isNotBlank() == true) {
                                stringResource(R.string.origin_format, state.origem!!.cidade, state.origem!!.uf)
                            } else {
                                stringResource(R.string.tap_to_configure_origin)
                            }
                            Text(
                                text = origemText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Filtros de Status
                Text(
                    text = stringResource(R.string.filter_destinations_by_status),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = state.filtroStatus == null,
                        onClick = { viewModel.filtrarPorStatus(null) },
                        label = { Text(stringResource(R.string.all)) }
                    )
                    StatusViagem.entries.forEach { statusItem ->
                        FilterChip(
                            selected = state.filtroStatus == statusItem,
                            onClick = { viewModel.filtrarPorStatus(statusItem) },
                            label = {
                                Text(
                                    when (statusItem) {
                                        StatusViagem.PENDENTE -> stringResource(R.string.pending_plural)
                                        StatusViagem.EXECUTANDO -> stringResource(R.string.status_in_progress)
                                        StatusViagem.REALIZADA -> stringResource(R.string.completed_plural)
                                    }
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lista de Destinos
                if (state.destinosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.todosDestinos.isEmpty()) {
                                stringResource(R.string.no_destinations_registered)
                            } else {
                                stringResource(R.string.no_destinations_filtered)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.destinosFiltrados) { destino ->
                            CardDestinoItem(
                                destino = destino,
                                onClick = { onNavigateToDetalhes(destino.id.toLong()) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardDestinoItem(
    destino: Destino,
    onClick: () -> Unit
) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${destino.cidade} - ${destino.uf}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Badge de Status
                val (statusNome, containerColor, contentColor) = when (destino.status) {
                    StatusViagem.PENDENTE -> Triple(stringResource(R.string.pending), MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                    StatusViagem.EXECUTANDO -> Triple(stringResource(R.string.status_in_progress), MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                    StatusViagem.REALIZADA -> Triple(stringResource(R.string.completed), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
                }

                SuggestionChip(
                    onClick = onClick,
                    label = { Text(statusNome) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.date_format, sdf.format(destino.dataPrevista)),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.distance_format, destino.distanciaKm.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (destino.orcamento > 0.0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.budget_format, destino.orcamento),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
