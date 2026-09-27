package com.ramyres.githubmobile.setup

import com.ramyres.githubmobile.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 1. Defina a URL base da sua API (sem o /endpoint)
    private const val BASE_URL = " https://api.github.com/"

    // 2. Cria e retorna a instância do Retrofit
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Diz que usaremos Gson para conversão
            .build()
            .create(ApiService::class.java) // Cria a implementação da nossa interface
    }
}