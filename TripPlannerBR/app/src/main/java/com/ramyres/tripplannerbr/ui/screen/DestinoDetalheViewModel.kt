package com.ramyres.tripplannerbr.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramyres.tripplannerbr.data.Contato
import com.ramyres.tripplannerbr.data.Destino
import com.ramyres.tripplannerbr.data.Passeio
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import com.ramyres.tripplannerbr.model.repository.ViagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DestinoDetalheUiState(
    val destino: Destino? = null,
    val passeios: List<Passeio> = emptyList(),
    val contatos: List<Contato> = emptyList(),
    val isLoading: Boolean = true,
    val isExcluido: Boolean = false
)

class DestinoDetalheViewModel(private val repository: ViagemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DestinoDetalheUiState())
    val uiState: StateFlow<DestinoDetalheUiState> = _uiState.asStateFlow()

    fun carregarDestino(destinoId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val destino = repository.buscarDestinoPorId(destinoId)
            val passeios = repository.listarPasseios(destinoId)
            val contatos = repository.listarContatos()

            _uiState.update {
                it.copy(
                    destino = destino,
                    passeios = passeios,
                    contatos = contatos,
                    isLoading = false
                )
            }
        }
    }

    fun atualizarStatus(novoStatus: StatusViagem) {
        val destinoAtual = _uiState.value.destino ?: return
        viewModelScope.launch {
            val atualizado = destinoAtual.copy(status = novoStatus)
            repository.salvarDestino(atualizado)
            _uiState.update { it.copy(destino = atualizado) }
        }
    }

    fun adicionarPasseio(descricao: String, contatoId: UInt = 0u) {
        val destinoAtual = _uiState.value.destino ?: return
        if (descricao.isBlank()) return

        viewModelScope.launch {
            val passeio = Passeio(
                destino = destinoAtual.id,
                descricao = descricao.trim(),
                contato = contatoId
            )
            repository.salvarPasseio(passeio)
            val passeiosAtualizados = repository.listarPasseios(destinoAtual.id.toLong())
            _uiState.update { it.copy(passeios = passeiosAtualizados) }
        }
    }

    fun excluirPasseio(passeio: Passeio) {
        val destinoAtual = _uiState.value.destino ?: return
        viewModelScope.launch {
            repository.excluirPasseio(passeio)
            val passeiosAtualizados = repository.listarPasseios(destinoAtual.id.toLong())
            _uiState.update { it.copy(passeios = passeiosAtualizados) }
        }
    }

    fun adicionarContato(nome: String, telefone: String, email: String) {
        if (nome.isBlank()) return
        viewModelScope.launch {
            val contato = Contato(
                nome = nome.trim(),
                telefone = telefone.trim(),
                email = email.trim()
            )
            repository.salvarContato(contato)
            val contatosAtualizados = repository.listarContatos()
            _uiState.update { it.copy(contatos = contatosAtualizados) }
        }
    }

    fun excluirContato(contato: Contato) {
        viewModelScope.launch {
            repository.excluirContato(contato)
            val contatosAtualizados = repository.listarContatos()
            _uiState.update { it.copy(contatos = contatosAtualizados) }
        }
    }

    fun excluirDestino(onSucesso: () -> Unit) {
        val destinoAtual = _uiState.value.destino ?: return
        viewModelScope.launch {
            repository.excluirDestino(destinoAtual)
            _uiState.update { it.copy(isExcluido = true) }
            onSucesso()
        }
    }
}
