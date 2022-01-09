package com.edwin.github_app.network.entities


import com.edwin.common.anno.PoKo
import retrofit2.adapter.rxjava.PagingWrapper

@PoKo
data class SearchRepositories(var total_count: Int,
                              var incomplete_results: Boolean,
                              var items: List<Repository>) : PagingWrapper<Repository>() {

    override fun getElements() = items

}