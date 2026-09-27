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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import kotlinx.serialization.Serializable

@Serializable
object PerfilUsuarioEndpoint

val LISTA_UFS = listOf(
    "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
    "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
    "RS", "RO", "RR", "SC", "SP", "SE", "TO"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    viewModel: PerfilUsuarioViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var ufExpanded by remember { mutableStateOf(false) }
    var cidadeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_profile)) },
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
            // Card de Identificação
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
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.user),
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (state.nome.isNotBlank()) state.nome else stringResource(R.string.your_profile),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Dados Pessoais
            Text(
                text = stringResource(R.string.personal_info),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = state.nome,
                onValueChange = { viewModel.onNomeChanged(it) },
                label = { Text(stringResource(R.string.full_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Origem (Residência)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.origin_location_header),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Dropdown de Estado (UF)
            ExposedDropdownMenuBox(
                expanded = ufExpanded,
                onExpandedChange = { ufExpanded = !ufExpanded }
            ) {
                OutlinedTextField(
                    value = state.uf,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.state_uf)) },
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

            // Dropdown de Cidade (API IBGE)
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
                    label = { Text(stringResource(R.string.origin_city)) },
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
                value = state.cepText,
                onValueChange = { viewModel.onCepChanged(it) },
                label = { Text(stringResource(R.string.cep_digits_only)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (state.isSalvoSucesso) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.success),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.profile_saved_success),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.salvarPerfil() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.save_profile))
            }
        }
    }
}
