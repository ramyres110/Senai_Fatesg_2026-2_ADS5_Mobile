package com.ramyres.tripplannerbr.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiMunicipiosService {
    @GET("estados/{uf}/municipios")
    suspend fun getMunicipios(@Path("uf") uf: String): List<CidadeDto>
}