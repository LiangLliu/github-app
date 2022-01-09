package com.edwin.github_app.view.fragments.subfragments

import com.edwin.github_app.network.entities.Issue
import com.edwin.github_app.presenter.MyIssuePresenter
import com.edwin.github_app.view.common.CommonListFragment

/**
 * Created by benny on 7/9/17.
 */
class MyIssueListFragment : CommonListFragment<Issue, MyIssuePresenter>() {
    companion object{
        const val REPOSITORY_NAME = "repository_name"
        const val OWNER_LOGIN = "owner_login"
    }

    override val adapter = IssueListAdapter()
}