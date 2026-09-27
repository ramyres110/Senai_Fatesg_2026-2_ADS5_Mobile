package com.ramyres.githubmobile.api

import com.ramyres.githubmobile.model.GithubRepo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    // GET /users/{username}
    @GET("/users/{username}")
    fun getUser(
        @Path("username") user: String // @Path substitui o {id} na URL
    ): retrofit2.Call<GithubRepo> // Retorna uma Call contendo o objeto User

    // Rota que retorna todos os repos publicos
    @GET("/users/{id}/repos")
    fun getUserRepos(
        @Path("id") user: String // @Path substitui o {id} na URL
    ): retrofit2.Call<List<GithubRepo>> // Retorna uma Call contendo o objeto User

    @DELETE
    fun delete(@Path("id") id: Int)
    : retrofit2.Call<Boolean>

    @POST
    fun save(@Body user: GithubRepo): retrofit2.Call<GithubRepo>
}