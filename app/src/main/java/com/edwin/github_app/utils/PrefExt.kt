package com.edwin.github_app.utils

import com.edwin.common.sharedpreferences.Preference
import com.edwin.github_app.AppContext
import kotlin.reflect.jvm.jvmName

inline fun <reified R, T> R.pref(default: T) = Preference(AppContext, "", default, R::class.jvmName)