package com.edwin.github_app.presenter

import com.edwin.github_app.model.page.ListPage
import com.edwin.github_app.model.people.PeoplePage
import com.edwin.github_app.model.people.PeoplePageParams
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.view.common.CommonListPresenter
import com.edwin.github_app.view.fragments.subfragments.PeopleListFragment

/**
 * Created by benny on 7/9/17.
 */
class PeopleListPresenter : CommonListPresenter<User, PeopleListFragment>(){

    override val listPage: ListPage<User> by lazy {
        PeoplePage(PeoplePageParams(view.type, view.login))
    }

}