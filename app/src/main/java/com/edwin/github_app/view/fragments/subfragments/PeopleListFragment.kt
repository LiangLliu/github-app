package com.edwin.github_app.view.fragments.subfragments

import com.edwin.annotations.FragmentBuilder
import com.edwin.annotations.Optional
import com.edwin.annotations.Required
import com.edwin.github_app.presenter.PeopleListPresenter
import com.edwin.github_app.view.common.CommonListFragment
import com.edwin.github_app.network.entities.User


@FragmentBuilder
class PeopleListFragment : CommonListFragment<User, PeopleListPresenter>() {
    @Optional
    lateinit var login: String

    @Required
    lateinit var type: String

    override val adapter = PeopleListAdapter()
}