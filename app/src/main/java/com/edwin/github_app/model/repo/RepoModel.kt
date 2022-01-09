package com.edwin.github_app.model.repo

import com.edwin.github_app.model.page.ListPage
import com.edwin.github_app.network.services.RepositoryService
import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.utils.format
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable
import java.util.*

class RepoListPage(val owner: User?) : ListPage<Repository>() {
    override fun getData(page: Int): Observable<GitHubPaging<Repository>> {
        return if (owner == null) {
            RepositoryService.allRepositories(page, "pushed:<" + Date().format("yyyy-MM-dd"))
                .map { it.paging }
        } else {
            RepositoryService.listRepositoriesOfUser(owner.login, page)
        }
    }

}