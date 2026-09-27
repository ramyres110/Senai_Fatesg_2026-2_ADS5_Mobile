package com.ramyres.tripplannerbr.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface ContatoDao {
    @Insert
    suspend fun inserir(contato: Contato): Long

    @Query("SELECT * FROM tb_contatos")
    suspend fun listar(): List<Contato>

    @Query("SELECT * FROM tb_contatos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Contato?

    @Delete
    suspend fun excluir(contato: Contato)

    @Update
    suspend fun atualizar(contato: Contato)
}
