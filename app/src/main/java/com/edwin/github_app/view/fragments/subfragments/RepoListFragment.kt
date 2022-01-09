package com.edwin.github_app.view.fragments.subfragments

import com.edwin.github_app.presenter.RepoListPresenter
import com.edwin.github_app.view.common.CommonListFragment

import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.network.entities.User

class RepoListFragment : CommonListFragment<Repository, RepoListPresenter>() {

    var user: User? = null

    override val adapter = RepoListAdapter()
}