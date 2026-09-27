package com.ramyres.tripplannerbr.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface PasseioDao {
    @Insert
    suspend fun inserir(passeio: Passeio)

    @Query("SELECT * FROM tb_passeios WHERE destino = :destinoId")
    suspend fun listarPorDestino(destinoId: Long): List<Passeio>

    @Query("SELECT * FROM tb_passeios")
    suspend fun listar(): List<Passeio>

    @Delete
    suspend fun excluir(passeio: Passeio)

    @Update
    suspend fun atualizar(passeio: Passeio)
}
