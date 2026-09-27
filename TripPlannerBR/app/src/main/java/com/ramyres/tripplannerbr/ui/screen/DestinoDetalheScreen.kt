package com.ramyres.tripplannerbr.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ramyres.tripplannerbr.data.Contato
import com.ramyres.tripplannerbr.data.Passeio
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class DestinoDetalheEndpoint(val destinoId: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinoDetalheScreen(
    destinoId: Long,
    viewModel: DestinoDetalheViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var showAddPasseioDialog by remember { mutableStateOf(false) }
    var showAddContatoDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(destinoId) {
        viewModel.carregarDestino(destinoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.destination_details)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (state.destino != null) {
                        IconButton(onClick = { showConfirmDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_destination),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
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
        } else if (state.destino == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.destination_not_found))
            }
        } else {
            val destino = state.destino!!
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card Principal
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${destino.cidade} - ${destino.uf}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(stringResource(R.string.planned_date_format, sdf.format(destino.dataPrevista)))
                        Text(stringResource(R.string.budget_format, destino.orcamento))
                        Text(stringResource(R.string.distance_format, destino.distanciaKm.toString()))
                        if (destino.cep > 0u) {
                            Text(stringResource(R.string.cep_format, destino.cep.toString()))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = stringResource(R.string.trip_status_label),
                            style = MaterialTheme.typography.labelLarge
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatusViagem.entries.forEach { statusItem ->
                                FilterChip(
                                    selected = destino.status == statusItem,
                                    onClick = { viewModel.atualizarStatus(statusItem) },
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
                    }
                }

                // Seção Passeios
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.planned_tours_count, state.passeios.size),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    IconButton(onClick = { showAddPasseioDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_tour)
                        )
                    }
                }

                if (state.passeios.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_tours_registered),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    state.passeios.forEach { passeio ->
                        CardPasseioItem(
                            passeio = passeio,
                            onExcluir = { viewModel.excluirPasseio(passeio) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Seção Contatos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.guides_and_hotels_count, state.contatos.size),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    IconButton(onClick = { showAddContatoDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_contact)
                        )
                    }
                }

                if (state.contatos.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_contacts_registered),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    state.contatos.forEach { contato ->
                        CardContatoItem(
                            contato = contato,
                            onExcluir = { viewModel.excluirContato(contato) }
                        )
                    }
                }
            }
        }
    }

    // Dialog Adicionar Passeio
    if (showAddPasseioDialog) {
        DialogNovoPasseio(
            onDismiss = { showAddPasseioDialog = false },
            onConfirm = { desc ->
                viewModel.adicionarPasseio(desc)
                showAddPasseioDialog = false
            }
        )
    }

    // Dialog Adicionar Contato
    if (showAddContatoDialog) {
        DialogNovoContato(
            onDismiss = { showAddContatoDialog = false },
            onConfirm = { nome, tel, email ->
                viewModel.adicionarContato(nome, tel, email)
                showAddContatoDialog = false
            }
        )
    }

    // Dialog Confirmar Exclusão
    if (showConfirmDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_destination)) },
            text = { Text(stringResource(R.string.delete_destination_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDeleteDialog = false
                        viewModel.excluirDestino {
                            onNavigateBack()
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun CardPasseioItem(
    passeio: Passeio,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = passeio.descricao,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onExcluir) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_tour),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CardContatoItem(
    contato: Contato,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = contato.nome, style = MaterialTheme.typography.titleSmall)
                if (contato.telefone.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.phone_format, contato.telefone),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (contato.email.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.email_format, contato.email),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = onExcluir) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_contact),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun DialogNovoPasseio(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var descricao by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_tour)) },
        text = {
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text(stringResource(R.string.tour_description)) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (descricao.isNotBlank()) onConfirm(descricao)
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun DialogNovoContato(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_contact_guide_hotel)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text(stringResource(R.string.phone)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nome.isNotBlank()) onConfirm(nome, telefone, email)
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
