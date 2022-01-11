package com.edwin.github_app.view.widget

import android.view.MenuItem
import android.view.View
import android.widget.TextView
import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView
import com.edwin.common.log.logger
import com.edwin.github_app.R
import com.edwin.github_app.model.account.AccountManager
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.settings.Settings
import com.edwin.github_app.utils.doOnLayoutAvailable
import com.edwin.github_app.utils.loadWithGlide
import com.edwin.github_app.utils.selectItem
import com.edwin.github_app.view.config.NavViewItem
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk15.coroutines.onClick

class NavigationController(
    private val navigationView: NavigationView,
    private val onNavItemChanged: (NavViewItem) -> Unit,
    private val onHeaderClick: () -> Unit
) : NavigationView.OnNavigationItemSelectedListener {

    init {
        navigationView.setNavigationItemSelectedListener(this)
    }

    private var currentItem: NavViewItem? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.apply {
            Settings.lastPage = item.itemId
            val navItem = NavViewItem[item.itemId]
            onNavItemChanged(navItem)
        }
        return true
    }

    fun useLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    fun useNoLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer_no_logged_in) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    private fun onUpdate(user: User?) {
        navigationView.doOnLayoutAvailable {
            navigationView.apply {
                findViewById<TextView>(R.id.usernameView).text = user?.login ?: "请登录"
                findViewById<TextView>(R.id.emailView).text = user?.email ?: "bennyhuo@kotliner.cn"
                if (user == null) {
                    findViewById<AppCompatAvatarImageView>(R.id.avatarView).imageResource =
                        R.drawable.ic_github
                } else {
                    findViewById<AppCompatAvatarImageView>(R.id.avatarView).loadWithGlide(
                        user.avatar_url,
                        user.login.first()
                    )
                }

                findViewById<View>(R.id.navigationHeader).onClick { onHeaderClick() }
            }
        }
    }

    fun selectProperItem() {
        logger.debug("selectProperItem")
        navigationView.doOnLayoutAvailable {
            logger.debug("selectProperItem onLayout: $currentItem")
            ((currentItem?.let { NavViewItem[it] } ?: Settings.lastPage)
                .takeIf { navigationView.menu.findItem(it) != null }
                ?: run { Settings.defaultPage })
                .let(navigationView::selectItem)
        }
    }
}