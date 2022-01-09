package com.edwin.github_app.view.fragments

import android.os.Bundle
import com.edwin.github_app.model.account.AccountManager

import com.edwin.github_app.view.common.CommonViewPagerFragment
import com.edwin.github_app.view.fragments.subfragments.RepoListFragment
import com.edwin.github_app.view.config.FragmentPage

class RepoFragment : CommonViewPagerFragment() {
    override fun getFragmentPagesNotLoggedIn(): List<FragmentPage> {
        return listOf(FragmentPage(RepoListFragment(), "All"))
    }

    override fun getFragmentPagesLoggedIn(): List<FragmentPage> {
        return listOf(
            FragmentPage(RepoListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        RepoListFragmentBuilder.OPTIONAL_USER,
                        AccountManager.currentUser
                    )
                }
            }, "My"),
            FragmentPage(RepoListFragment(), "All")
        )
    }

}