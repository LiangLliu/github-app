package com.edwin.github_app.view.fragments

import com.edwin.github_app.view.common.CommonViewPagerFragment
import com.edwin.github_app.view.config.FragmentPage
import com.edwin.github_app.view.fragments.subfragments.MyIssueListFragment

class MyIssueFragment : CommonViewPagerFragment() {
    override fun getFragmentPagesNotLoggedIn()
            = listOf(
            FragmentPage(MyIssueListFragment(), "My")
    )

    override fun getFragmentPagesLoggedIn(): List<FragmentPage>
            = listOf(
            FragmentPage(MyIssueListFragment(), "My")
    )
}