package com.edwin.github_app.view.fragments

import android.os.Bundle
import com.edwin.github_app.model.account.AccountManager
import com.edwin.github_app.view.common.CommonViewPagerFragment
import com.edwin.github_app.view.config.FragmentPage
import com.edwin.github_app.view.fragments.subfragments.PeopleListFragment

class PeopleFragment : CommonViewPagerFragment() {
    override fun getFragmentPagesNotLoggedIn(): List<FragmentPage> {
        return listOf(FragmentPage(PeopleListFragment().also {
            it.arguments = Bundle().apply {
//                putString(PeopleListFragmentBuilder.REQUIRED_TYPE, ALL.name)
            }
        }, "All"))
    }

    override fun getFragmentPagesLoggedIn(): List<FragmentPage> =
        listOf(
            FragmentPage(PeopleListFragment().also {
                it.arguments = Bundle().apply {
                    putString(
                        null,
//                        PeopleListFragmentBuilder.OPTIONAL_LOGIN,
                        AccountManager.currentUser?.login
                    )
//                    putString(PeopleListFragmentBuilder.REQUIRED_TYPE, FOLLOWER.name)
                }
            }, "Followers"),
            FragmentPage(PeopleListFragment().also {
                it.arguments = Bundle().apply {
                    putString(
                        null,
//                        PeopleListFragmentBuilder.OPTIONAL_LOGIN,
                        AccountManager.currentUser!!.login
                    )
//                    putString(PeopleListFragmentBuilder.REQUIRED_TYPE, FOLLOWING.name)
                }
            }, "Following"),
            FragmentPage(PeopleListFragment().also {
                it.arguments = Bundle().apply {
//                    putString(PeopleListFragmentBuilder.REQUIRED_TYPE, ALL.name)
                }
            }, "All")
        )

}