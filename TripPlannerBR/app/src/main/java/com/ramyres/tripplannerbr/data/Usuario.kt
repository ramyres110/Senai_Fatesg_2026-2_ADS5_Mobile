package com.ramyres.tripplannerbr.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tb_usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: UInt = 0u,
    val nome: String = "",
    val email: String = ""
)
