package com.ramyres.tripplannerbr.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM tb_usuarios LIMIT 1")
    suspend fun obterUsuario(): Usuario?

    @Query("SELECT * FROM tb_usuarios")
    suspend fun listar(): List<Usuario>

    @Update
    suspend fun atualizar(usuario: Usuario)
}
