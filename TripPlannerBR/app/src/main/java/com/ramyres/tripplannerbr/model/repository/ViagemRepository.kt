package com.ramyres.tripplannerbr.model.repository

import com.ramyres.tripplannerbr.api.ApiIBGE
import com.ramyres.tripplannerbr.api.CidadeDto
import com.ramyres.tripplannerbr.data.BancoDeDados
import com.ramyres.tripplannerbr.data.Contato
import com.ramyres.tripplannerbr.data.Destino
import com.ramyres.tripplannerbr.data.Origem
import com.ramyres.tripplannerbr.data.Passeio
import com.ramyres.tripplannerbr.data.Usuario

class ViagemRepository(private val db: BancoDeDados) {

    // Usuario
    suspend fun getUsuario(): Usuario? = db.usuarioDao().obterUsuario()
    
    suspend fun salvarUsuario(usuario: Usuario) {
        if (usuario.id == 0u) {
            db.usuarioDao().inserir(usuario)
        } else {
            db.usuarioDao().atualizar(usuario)
        }
    }

    // Origem
    suspend fun getOrigem(): Origem? = db.origemDao().obterOrigem()
    
    suspend fun salvarOrigem(origem: Origem) {
        val existente = db.origemDao().obterOrigem()
        if (existente == null) {
            db.origemDao().inserir(origem)
        } else {
            db.origemDao().atualizar(origem.copy(id = existente.id))
        }
    }

    // Destinos
    suspend fun listarDestinos(): List<Destino> = db.destinoDao().listar()
    
    suspend fun buscarDestinoPorId(id: Long): Destino? = db.destinoDao().buscarPorId(id)
    
    suspend fun salvarDestino(destino: Destino) {
        if (destino.id == 0u) {
            db.destinoDao().inserir(destino)
        } else {
            db.destinoDao().atualizar(destino)
        }
    }
    
    suspend fun excluirDestino(destino: Destino) = db.destinoDao().excluir(destino)

    // Passeios
    suspend fun listarPasseios(destinoId: Long): List<Passeio> = db.passeioDao().listarPorDestino(destinoId)
    
    suspend fun salvarPasseio(passeio: Passeio) {
        if (passeio.id == 0u) {
            db.passeioDao().inserir(passeio)
        } else {
            db.passeioDao().atualizar(passeio)
        }
    }
    
    suspend fun excluirPasseio(passeio: Passeio) = db.passeioDao().excluir(passeio)

    // Contatos
    suspend fun listarContatos(): List<Contato> = db.contatoDao().listar()
    
    suspend fun buscarContatoPorId(id: Long): Contato? = db.contatoDao().buscarPorId(id)
    
    suspend fun salvarContato(contato: Contato): Long {
        return if (contato.id == 0u) {
            db.contatoDao().inserir(contato)
        } else {
            db.contatoDao().atualizar(contato)
            contato.id.toLong()
        }
    }
    
    suspend fun excluirContato(contato: Contato) = db.contatoDao().excluir(contato)

    // API IBGE
    suspend fun getCidadesPorUf(uf: String): List<CidadeDto> {
        return try {
            ApiIBGE.service.getMunicipios(uf)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
