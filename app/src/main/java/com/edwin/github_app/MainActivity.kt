package com.edwin.github_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.edwin.mvp.impl.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment =  MainFragment()

        Log.d("mvp", mainFragment.toString())
        Log.d("mvp", mainFragment.presenter.toString())
        Log.d("mvp", mainFragment.presenter.view.toString())
    }
}