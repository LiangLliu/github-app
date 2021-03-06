package com.edwin.github_app.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.edwin.common.ext.no
import com.edwin.common.ext.otherwise
import com.edwin.common.ext.yes
import com.edwin.experimental.coroutines.launchUI
import com.edwin.github_app.BaseApp
import com.edwin.github_app.R
import com.edwin.github_app.model.account.AccountManager
import com.edwin.github_app.model.account.OnAccountStateChangeListener
import com.edwin.github_app.network.entities.User
import com.edwin.github_app.utils.afterClosed
import com.edwin.github_app.utils.showFragment
import com.edwin.github_app.view.config.NavViewItem
import com.edwin.github_app.view.config.Themer
import com.edwin.github_app.view.widget.ActionBarController
import com.edwin.github_app.view.widget.NavigationController
import com.edwin.github_app.view.widget.confirm
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.sdk15.listeners.onCheckedChange
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), OnAccountStateChangeListener {

    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    val actionBarController by lazy {
        ActionBarController(this)
    }

    private val navigationController by lazy {
        NavigationController(navigationView, ::onNavItemChanged, ::handleNavigationHeaderClickEvent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        initNavigationView()

        AccountManager.onAccountStateChangeListeners.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AccountManager.onAccountStateChangeListeners.remove(this)
    }

    private fun initNavigationView() {
        AccountManager.isLoggedIn()
            .yes {
                navigationController.useLoginLayout()
            }
            .otherwise {
                navigationController.useNoLoginLayout()
            }
        navigationController.selectProperItem()
    }

    override fun onLogin(user: User) {
        navigationController.useLoginLayout()
    }

    override fun onLogout() {
        navigationController.useNoLoginLayout()
    }

    private fun onNavItemChanged(navViewItem: NavViewItem) {
        drawerLayout.afterClosed {
            showFragment(R.id.fragmentContainer, navViewItem.fragmentClass, navViewItem.arguements)
            title = navViewItem.title
        }
    }

    private fun handleNavigationHeaderClickEvent() {
        AccountManager.isLoggedIn()
            .no {
                val intent = Intent(BaseApp.context, LoginActivity::class.java)
                startActivity(intent)
            }.otherwise {
                launchUI {
                    if (confirm("??????", "????????????????")) {
                        AccountManager
                            .logout()
                            .subscribe({
                                toast("????????????")
                            }, {
                                it.printStackTrace()
                            })
                    } else {
                        toast("?????????")
                    }
                }
            }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_actionbar, menu)
        menu.findItem(R.id.dayNight)
            .actionView
            .findViewById<AppCompatToggleButton>(R.id.dayNightSwitch)
            .apply {
                isChecked = Themer.currentTheme() == Themer.ThemeMode.DAY
                onCheckedChange { _, _ ->
                    Themer.toggle(this@MainActivity)
                }
            }
        return super.onCreateOptionsMenu(menu)
    }

    //??????????????????home??????(?????????)???????????????????????????????????????????????????
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}