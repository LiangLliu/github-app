package com.edwin.github_app.network.services


import com.edwin.github_app.network.entities.Issue
import com.edwin.github_app.network.retrofit
import retrofit2.adapter.rxjava.GitHubPaging
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by benny on 2/4/18.
 */
interface IssueApi {
    @GET("/issues?filter=all&state=all")
    fun listIssuesOfAuthenticatedUser(
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 20
    ): Observable<GitHubPaging<Issue>>

}

object IssueService : IssueApi by retrofit.create(IssueApi::class.java)