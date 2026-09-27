package com.ramyres.tripplannerbr.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramyres.tripplannerbr.api.CidadeDto
import com.ramyres.tripplannerbr.data.Destino
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import com.ramyres.tripplannerbr.model.repository.ViagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CadastroDestinoUiState(
    val uf: String = "",
    val cidade: String = "",
    val ibgeId: Int = 0,
    val cepText: String = "",
    val distanciaKmText: String = "",
    val orcamentoText: String = "",
    val dataPrevistaText: String = "",
    val status: StatusViagem = StatusViagem.PENDENTE,
    val cidadesDisponiveis: List<CidadeDto> = emptyList(),
    val isLoadingCidades: Boolean = false,
    val mensagemErro: String? = null,
    val isSalvoSucesso: Boolean = false
)

class CadastroDestinoViewModel(private val repository: ViagemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroDestinoUiState())
    val uiState: StateFlow<CadastroDestinoUiState> = _uiState.asStateFlow()

    fun onUfChanged(novaUf: String) {
        _uiState.update { it.copy(uf = novaUf, cidade = "", ibgeId = 0) }
        carregarCidadesPorUf(novaUf)
    }

    fun onCidadeSelecionada(cidadeDto: CidadeDto) {
        _uiState.update {
            it.copy(
                cidade = cidadeDto.nome,
                ibgeId = cidadeDto.id.toInt()
            )
        }
    }

    fun onCepChanged(novoCep: String) {
        val num = novoCep.filter { it.isDigit() }
        _uiState.update { it.copy(cepText = num) }
    }

    fun onDistanciaChanged(novaDistancia: String) {
        val num = novaDistancia.filter { it.isDigit() }
        _uiState.update { it.copy(distanciaKmText = num) }
    }

    fun onOrcamentoChanged(novoOrcamento: String) {
        _uiState.update { it.copy(orcamentoText = novoOrcamento) }
    }

    fun onDataPrevistaChanged(novaData: String) {
        _uiState.update { it.copy(dataPrevistaText = novaData) }
    }

    fun onStatusChanged(novoStatus: StatusViagem) {
        _uiState.update { it.copy(status = novoStatus) }
    }

    private fun carregarCidadesPorUf(uf: String) {
        if (uf.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCidades = true) }
            val cidades = repository.getCidadesPorUf(uf)
            _uiState.update { it.copy(cidadesDisponiveis = cidades, isLoadingCidades = false) }
        }
    }

    fun salvarDestino(onSucesso: () -> Unit) {
        val state = _uiState.value

        if (state.uf.isBlank() || state.cidade.isBlank()) {
            _uiState.update { it.copy(mensagemErro = "Selecione um Estado e uma Cidade.") }
            return
        }

        val dataFormatada = try {
            if (state.dataPrevistaText.isNotBlank()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.parse(state.dataPrevistaText) ?: Date()
            } else {
                Date()
            }
        } catch (e: Exception) {
            Date()
        }

        val cepValue = state.cepText.toULongOrNull() ?: 0u
        val distanciaValue = state.distanciaKmText.toULongOrNull() ?: 0u
        val orcamentoValue = state.orcamentoText.replace(",", ".").toDoubleOrNull() ?: 0.0

        val destino = Destino(
            ibgeId = state.ibgeId,
            uf = state.uf,
            cidade = state.cidade,
            cep = cepValue,
            distanciaKm = distanciaValue,
            orcamento = orcamentoValue,
            dataPrevista = dataFormatada,
            status = state.status
        )

        viewModelScope.launch {
            repository.salvarDestino(destino)
            _uiState.update { it.copy(isSalvoSucesso = true) }
            onSucesso()
        }
    }
}
