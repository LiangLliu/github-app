package com.edwin.github_app.view.config

import android.os.Bundle

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.edwin.github_app.R

import com.edwin.github_app.view.fragments.AboutFragment
import com.edwin.github_app.view.fragments.MyIssueFragment
import com.edwin.github_app.view.fragments.PeopleFragment
import com.edwin.github_app.view.fragments.RepoFragment
//import com.edwin.github_app.view.fragments.subfragments.RepoListFragmentBuilder

class NavViewItem private constructor(
    val groupId: Int = 0,
    val title: String,
    @DrawableRes val icon: Int,
    val fragmentClass: Class<out Fragment>,
    val arguements: Bundle = Bundle()
) {

    companion object {
        private val items = mapOf(
            R.id.navRepos to NavViewItem(
                0,
                "Repository",
                R.drawable.ic_repository,
                RepoFragment::class.java,
                Bundle().apply { 
//                    putParcelable(RepoListFragmentBuilder.OPTIONAL_USER, null)
                }),
            R.id.navPeople to NavViewItem(
                0,
                "People",
                R.drawable.ic_people,
                PeopleFragment::class.java
            ),
            R.id.navIssue to NavViewItem(
                0,
                "Issue",
                R.drawable.ic_issue,
                MyIssueFragment::class.java
            ),
            R.id.navAbout to NavViewItem(
                0,
                "About",
                R.drawable.ic_about_us,
                AboutFragment::class.java
            )
        )

        operator fun get(@IdRes navId: Int): NavViewItem {
            return items[navId] ?: items[R.id.navRepos]!!
        }

        operator fun get(item: NavViewItem): Int {
            return items.filter { it.value == item }.keys.first()
        }
    }

    override fun toString(): String {
        return "NavViewItem(groupId=$groupId, title='$title', icon=$icon, fragmentClass=$fragmentClass, arguements=$arguements)"
    }
}
