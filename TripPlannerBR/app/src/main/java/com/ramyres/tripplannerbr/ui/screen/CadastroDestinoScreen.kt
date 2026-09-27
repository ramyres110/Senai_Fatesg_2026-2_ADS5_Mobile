package com.ramyres.tripplannerbr.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramyres.tripplannerbr.R
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import kotlinx.serialization.Serializable

@Serializable
object CadastroDestinoEndpoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroDestinoScreen(
    viewModel: CadastroDestinoViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var ufExpanded by remember { mutableStateOf(false) }
    var cidadeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_destination)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = stringResource(R.string.destination),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.add_new_trip_destination),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (state.mensagemErro != null) {
                Text(
                    text = stringResource(R.string.error_select_state_city),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // UF
            ExposedDropdownMenuBox(
                expanded = ufExpanded,
                onExpandedChange = { ufExpanded = !ufExpanded }
            ) {
                OutlinedTextField(
                    value = state.uf,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.destination_state_uf)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ufExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = ufExpanded,
                    onDismissRequest = { ufExpanded = false }
                ) {
                    LISTA_UFS.forEach { ufItem ->
                        DropdownMenuItem(
                            text = { Text(ufItem) },
                            onClick = {
                                viewModel.onUfChanged(ufItem)
                                ufExpanded = false
                            }
                        )
                    }
                }
            }

            // Cidade (IBGE)
            ExposedDropdownMenuBox(
                expanded = cidadeExpanded,
                onExpandedChange = {
                    if (state.uf.isNotEmpty() && !state.isLoadingCidades) {
                        cidadeExpanded = !cidadeExpanded
                    }
                }
            ) {
                OutlinedTextField(
                    value = state.cidade,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.destination_city_ibge)) },
                    trailingIcon = {
                        if (state.isLoadingCidades) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = cidadeExpanded)
                        }
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = state.uf.isNotEmpty()
                )

                if (state.cidadesDisponiveis.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = cidadeExpanded,
                        onDismissRequest = { cidadeExpanded = false }
                    ) {
                        state.cidadesDisponiveis.forEach { cidadeDto ->
                            DropdownMenuItem(
                                text = { Text(cidadeDto.nome) },
                                onClick = {
                                    viewModel.onCidadeSelecionada(cidadeDto)
                                    cidadeExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.distanciaKmText,
                onValueChange = { viewModel.onDistanciaChanged(it) },
                label = { Text(stringResource(R.string.estimated_distance_km)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.orcamentoText,
                onValueChange = { viewModel.onOrcamentoChanged(it) },
                label = { Text(stringResource(R.string.planned_budget_brl)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.dataPrevistaText,
                onValueChange = { viewModel.onDataPrevistaChanged(it) },
                label = { Text(stringResource(R.string.planned_date_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.cepText,
                onValueChange = { viewModel.onCepChanged(it) },
                label = { Text(stringResource(R.string.cep_digits_only)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = stringResource(R.string.trip_status),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusViagem.entries.forEach { statusItem ->
                    FilterChip(
                        selected = state.status == statusItem,
                        onClick = { viewModel.onStatusChanged(statusItem) },
                        label = {
                            Text(
                                when (statusItem) {
                                    StatusViagem.PENDENTE -> stringResource(R.string.pending)
                                    StatusViagem.EXECUTANDO -> stringResource(R.string.status_in_progress)
                                    StatusViagem.REALIZADA -> stringResource(R.string.completed)
                                }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.salvarDestino {
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.register_destination))
            }
        }
    }
}
