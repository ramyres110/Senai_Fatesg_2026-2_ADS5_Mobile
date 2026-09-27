package com.ramyres.tripplannerbr.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiIBGE {
    private const val BASE_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/"

    val service: ApiMunicipiosService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiMunicipiosService::class.java)
    }
}