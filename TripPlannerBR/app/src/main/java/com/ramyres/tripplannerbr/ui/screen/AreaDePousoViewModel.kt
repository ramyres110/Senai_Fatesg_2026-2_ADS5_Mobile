package com.ramyres.tripplannerbr.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramyres.tripplannerbr.data.Destino
import com.ramyres.tripplannerbr.data.Origem
import com.ramyres.tripplannerbr.data.Usuario
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import com.ramyres.tripplannerbr.model.repository.ViagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AreaDePousoUiState(
    val usuario: Usuario? = null,
    val origem: Origem? = null,
    val todosDestinos: List<Destino> = emptyList(),
    val destinosFiltrados: List<Destino> = emptyList(),
    val filtroStatus: StatusViagem? = null,
    val isLoading: Boolean = true
)

class AreaDePousoViewModel(private val repository: ViagemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AreaDePousoUiState())
    val uiState: StateFlow<AreaDePousoUiState> = _uiState.asStateFlow()

    fun carregarDados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val usuario = repository.getUsuario()
            val origem = repository.getOrigem()
            val destinos = repository.listarDestinos()

            _uiState.update { state ->
                val filtrados = aplicarFiltro(destinos, state.filtroStatus)
                state.copy(
                    usuario = usuario,
                    origem = origem,
                    todosDestinos = destinos,
                    destinosFiltrados = filtrados,
                    isLoading = false
                )
            }
        }
    }

    fun filtrarPorStatus(status: StatusViagem?) {
        _uiState.update { state ->
            val novoFiltro = if (state.filtroStatus == status) null else status
            state.copy(
                filtroStatus = novoFiltro,
                destinosFiltrados = aplicarFiltro(state.todosDestinos, novoFiltro)
            )
        }
    }

    private fun aplicarFiltro(lista: List<Destino>, status: StatusViagem?): List<Destino> {
        return if (status == null) {
            lista
        } else {
            lista.filter { it.status == status }
        }
    }
}
