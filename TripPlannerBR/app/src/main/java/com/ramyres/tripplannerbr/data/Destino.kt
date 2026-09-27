package com.ramyres.tripplannerbr.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.ramyres.tripplannerbr.model.enumeradores.StatusViagem
import java.util.Date

@Entity(tableName = "tb_destinos")
data class Destino(
    @PrimaryKey(autoGenerate = true)
    val id: UInt = 0u,
    val ibgeId: Int = 0,
    val uf: String = "",
    val cidade: String = "",
    val cep: ULong = 0u,
    val distanciaKm: ULong = 0u,
    val orcamento: Double = 0.0,
    val dataPrevista: Date = Date(),
    val dataRealizada: Date? = null,
    val status: StatusViagem = StatusViagem.PENDENTE
)
