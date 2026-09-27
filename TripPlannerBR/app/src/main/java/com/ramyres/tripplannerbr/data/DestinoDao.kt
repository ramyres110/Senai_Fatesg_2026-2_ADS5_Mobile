package com.ramyres.tripplannerbr.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface DestinoDao {
    @Insert
    suspend fun inserir(destino: Destino): Long

    @Query("SELECT * FROM tb_destinos")
    suspend fun listar(): List<Destino>

    @Query("SELECT * FROM tb_destinos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Destino?

    @Delete
    suspend fun excluir(destino: Destino)

    @Update
    suspend fun atualizar(destino: Destino)
}
