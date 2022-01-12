package com.edwin.github_app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.edwin.runtime.Tieguanyin
import com.edwin.runtime.core.ActivityBuilder
import com.edwin.swipefinishable.SwipeFinishable

private lateinit var INSTANCE: Application

class BaseApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        context = baseContext

        Tieguanyin.init(this);
        ActivityBuilder.INSTANCE.init(this)
        SwipeFinishable.INSTANCE.init(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }
}

object AppContext : ContextWrapper(INSTANCE)