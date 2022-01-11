package com.edwin.github_app.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
//import com.edwin.github.network.graphql.entities.RepositoryIssueCountQuery

import com.edwin.github_app.network.interceptor.AuthInterceptor
import com.edwin.retroapollo.RetroApollo
import com.edwin.retroapollo.annotations.GraphQLQuery
import com.edwin.retroapollo.coroutine.CoroutineCallAdapterFactory
import com.edwin.retroapollo.rxjava.RxJavaCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface GraphQLApi {
//    fun repositoryIssueCount(@GraphQLQuery("owner") owner: String, @GraphQLQuery("repo") repo: String)
//        :Observable<RepositoryIssueCountQuery.Data>
//
//    fun repositoryIssueCount2(@GraphQLQuery("owner") owner: String, @GraphQLQuery("repo") repo: String)
//            :ApolloCall<RepositoryIssueCountQuery.Data>
//
//    fun repositoryIssueCount3(@GraphQLQuery("owner") owner: String, @GraphQLQuery("repo") repo: String)
//            : Deferred<RepositoryIssueCountQuery.Data>
}

private const val BASE_URL = "https://api.github.com/graphql"

val apolloClient by lazy {
    ApolloClient.builder()
        .serverUrl(BASE_URL)
        .okHttpClient(
            OkHttpClient.Builder().addInterceptor(AuthInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
        )
        .build()
}

private val graphQLService by lazy {
    RetroApollo.Builder()
        .apolloClient(apolloClient)
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory()
                .observableScheduler(AndroidSchedulers.mainThread())
                .subscribeScheduler(Schedulers.io())
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .createGraphQLService(GraphQLApi::class)
}

object GraphQLService : GraphQLApi by graphQLService