package com.edwin.github_app.model.issue

import com.edwin.github_app.model.page.ListPage
import com.edwin.github_app.network.services.IssueService
import com.edwin.github_app.network.entities.Issue
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable

/**
 * Created by benny on 9/24/17.
 */
class MyIssuePage : ListPage<Issue>() {
    override fun getData(page: Int): Observable<GitHubPaging<Issue>> {
        return IssueService.listIssuesOfAuthenticatedUser(page = page)
    }
}