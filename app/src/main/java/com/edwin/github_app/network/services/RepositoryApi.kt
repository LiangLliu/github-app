package com.edwin.github_app.network.services

import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.network.entities.SearchRepositories
import com.edwin.github_app.network.retrofit
import okhttp3.CacheControl.Companion.FORCE_NETWORK
import retrofit2.adapter.rxjava.GitHubPaging
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface RepositoryApi {

    @GET("/users/{owner}/repos?type=all")
    fun listRepositoriesOfUser(@Path("owner") owner: String, @Query("page") page: Int = 1, @Query("per_page") per_page: Int = 20): Observable<GitHubPaging<Repository>>

    @GET("/search/repositories?order=desc&sort=updated")
    fun allRepositories(@Query("page") page: Int = 1, @Query("q") q: String, @Query("per_page") per_page: Int = 20): Observable<SearchRepositories>

    @GET("/repos/{owner}/{repo}")
    fun getRepository(@Path("owner") owner: String, @Path("repo") repo: String, @Query("FORCE_NETWORK") forceNetwork: Boolean = false): Observable<Repository>
}

object RepositoryService : RepositoryApi by retrofit.create(RepositoryApi::class.java)