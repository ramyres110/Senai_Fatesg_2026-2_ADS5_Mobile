package com.ramyres.tripplannerbr.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramyres.tripplannerbr.model.repository.ViagemRepository

class ViagemViewModelFactory(private val repository: ViagemRepository) : ViewModelProvider.Factory {
    // Criação de instâncias dos ViewModels com base no repositório
    // Unchecked cast para ignorar alertas
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AreaDePousoViewModel::class.java) -> AreaDePousoViewModel(repository) as T
            modelClass.isAssignableFrom(PerfilUsuarioViewModel::class.java) -> PerfilUsuarioViewModel(repository) as T
            modelClass.isAssignableFrom(CadastroDestinoViewModel::class.java) -> CadastroDestinoViewModel(repository) as T
            modelClass.isAssignableFrom(DestinoDetalheViewModel::class.java) -> DestinoDetalheViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
