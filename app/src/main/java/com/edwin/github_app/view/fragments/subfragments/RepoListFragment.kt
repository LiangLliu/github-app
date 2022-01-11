package com.edwin.github_app.view.fragments.subfragments

import com.edwin.annotations.FragmentBuilder
import com.edwin.annotations.Optional
import com.edwin.github_app.presenter.RepoListPresenter
import com.edwin.github_app.view.common.CommonListFragment

import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.network.entities.User

@FragmentBuilder
class RepoListFragment : CommonListFragment<Repository, RepoListPresenter>() {

    @Optional
    var user: User? = null

    override val adapter = RepoListAdapter()
}