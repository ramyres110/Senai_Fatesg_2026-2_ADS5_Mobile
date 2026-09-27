package com.ramyres.tripplannerbr.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tb_passeios")
data class Passeio(
    @PrimaryKey(autoGenerate = true)
    val id: UInt = 0u,
    val destino: UInt = 0u,
    val descricao: String = "",
    val contato: UInt = 0u
)
