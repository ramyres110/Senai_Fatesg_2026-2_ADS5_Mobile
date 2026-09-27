package com.ramyres.tripplannerbr.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramyres.tripplannerbr.api.CidadeDto
import com.ramyres.tripplannerbr.data.Origem
import com.ramyres.tripplannerbr.data.Usuario
import com.ramyres.tripplannerbr.model.repository.ViagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val usuarioId: UInt = 0u,
    val nome: String = "",
    val email: String = "",
    val origemId: UInt = 0u,
    val uf: String = "",
    val cidade: String = "",
    val ibgeId: Int = 0,
    val cepText: String = "",
    val cidadesDisponiveis: List<CidadeDto> = emptyList(),
    val isLoadingCidades: Boolean = false,
    val isSalvoSucesso: Boolean = false,
    val mensagemErro: String? = null
)

class PerfilUsuarioViewModel(private val repository: ViagemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    fun carregarDados() {
        viewModelScope.launch {
            val usuario = repository.getUsuario()
            val origem = repository.getOrigem()

            _uiState.update { state ->
                state.copy(
                    usuarioId = usuario?.id ?: 0u,
                    nome = usuario?.nome ?: "",
                    email = usuario?.email ?: "",
                    origemId = origem?.id ?: 0u,
                    uf = origem?.uf ?: "",
                    cidade = origem?.cidade ?: "",
                    ibgeId = origem?.ibgeId ?: 0,
                    cepText = if ((origem?.cep ?: 0u) > 0u) origem?.cep.toString() else ""
                )
            }

            if (origem != null && origem.uf.isNotEmpty()) {
                carregarCidadesPorUf(origem.uf)
            }
        }
    }

    fun onNomeChanged(novoNome: String) {
        _uiState.update { it.copy(nome = novoNome, isSalvoSucesso = false) }
    }

    fun onEmailChanged(novoEmail: String) {
        _uiState.update { it.copy(email = novoEmail, isSalvoSucesso = false) }
    }

    fun onUfChanged(novaUf: String) {
        _uiState.update { it.copy(uf = novaUf, cidade = "", ibgeId = 0, isSalvoSucesso = false) }
        carregarCidadesPorUf(novaUf)
    }

    fun onCidadeSelecionada(cidadeDto: CidadeDto) {
        _uiState.update {
            it.copy(
                cidade = cidadeDto.nome,
                ibgeId = cidadeDto.id.toInt(),
                isSalvoSucesso = false
            )
        }
    }

    fun onCepChanged(novoCep: String) {
        val apenasNumeros = novoCep.filter { it.isDigit() }
        _uiState.update { it.copy(cepText = apenasNumeros, isSalvoSucesso = false) }
    }

    private fun carregarCidadesPorUf(uf: String) {
        if (uf.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCidades = true) }
            val cidades = repository.getCidadesPorUf(uf)
            _uiState.update { it.copy(cidadesDisponiveis = cidades, isLoadingCidades = false) }
        }
    }

    fun salvarPerfil() {
        viewModelScope.launch {
            val state = _uiState.value

            val usuario = Usuario(
                id = state.usuarioId,
                nome = state.nome.trim(),
                email = state.email.trim()
            )
            repository.salvarUsuario(usuario)

            val cepValue = state.cepText.toULongOrNull() ?: 0u
            val origem = Origem(
                id = state.origemId,
                ibgeId = state.ibgeId,
                uf = state.uf,
                cidade = state.cidade,
                cep = cepValue
            )
            repository.salvarOrigem(origem)

            _uiState.update { it.copy(isSalvoSucesso = true) }
        }
    }
}
