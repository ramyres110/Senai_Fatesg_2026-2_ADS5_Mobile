package com.ramyres.tripplannerbr.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface OrigemDao {
    @Insert
    suspend fun inserir(origem: Origem)

    @Query("SELECT * FROM tb_origens LIMIT 1")
    suspend fun obterOrigem(): Origem?

    @Query("SELECT * FROM tb_origens")
    suspend fun listar(): List<Origem>

    @Update
    suspend fun atualizar(origem: Origem)
}
