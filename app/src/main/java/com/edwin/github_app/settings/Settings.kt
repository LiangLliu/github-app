package com.edwin.github_app.settings

import com.edwin.github_app.AppContext
import com.edwin.github_app.R
import com.edwin.github_app.model.account.AccountManager.isLoggedIn
import com.edwin.github_app.utils.pref


object Settings {
    var lastPage: Int
        get() = if (lastPageIdString.isEmpty()) 0 else AppContext.resources.getIdentifier(
            lastPageIdString, "id", AppContext.packageName
        )
        set(value) {
            lastPageIdString = AppContext.resources.getResourceEntryName(value)
        }

    val defaultPage
        get() = if (isLoggedIn()) defaultPageForUser else defaultPageForVisitor

    private var defaultPageForUser by pref(R.id.navRepos)

    private var defaultPageForVisitor by pref(R.id.navRepos)

    private var lastPageIdString by pref("")

    var themeMode by pref("DAY")
}