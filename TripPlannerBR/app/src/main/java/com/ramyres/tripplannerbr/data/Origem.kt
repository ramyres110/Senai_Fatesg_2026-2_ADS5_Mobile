package com.ramyres.tripplannerbr.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tb_origens")
data class Origem(
    @PrimaryKey(autoGenerate = true)
    val id: UInt = 0u,
    val ibgeId: Int = 0,
    val uf: String = "",
    val cidade: String = "",
    val cep: ULong = 0u
)
