package com.edwin.github_app.presenter

import com.edwin.github_app.view.fragments.subfragments.MyIssueListFragment
import com.edwin.github_app.model.issue.MyIssuePage
import com.edwin.github_app.network.entities.Issue
import com.edwin.github_app.view.common.CommonListPresenter

/**
 * Created by benny on 7/9/17.
 */
class MyIssuePresenter : CommonListPresenter<Issue, MyIssueListFragment>() {
    override val listPage = MyIssuePage()
}