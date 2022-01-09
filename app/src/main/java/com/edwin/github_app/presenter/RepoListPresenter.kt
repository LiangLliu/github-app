package com.edwin.github_app.presenter

import com.edwin.github_app.model.repo.RepoListPage

import com.edwin.github_app.network.entities.Repository
import com.edwin.github_app.view.common.CommonListPresenter
import com.edwin.github_app.view.fragments.subfragments.RepoListFragment

class RepoListPresenter : CommonListPresenter<Repository, RepoListFragment>(){
    override val listPage by lazy {
        RepoListPage(view.user)
    }

}