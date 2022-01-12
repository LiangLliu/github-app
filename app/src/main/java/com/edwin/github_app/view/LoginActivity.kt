package com.edwin.github_app.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.edwin.common.ext.otherwise
import com.edwin.common.ext.yes
import com.edwin.github_app.BaseApp
import com.edwin.github_app.R
import com.edwin.github_app.presenter.LoginPresenter
import com.edwin.github_app.utils.hideSoftInput
import com.edwin.github_app.view.config.Themer
import com.edwin.mvp.impl.BaseActivity
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity<LoginPresenter>() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
        setContentView(R.layout.activity_login)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        findViewById<Button>(R.id.signInButton).onClick {
            presenter.checkUserName(username.text.toString())
                .yes {
                    presenter.checkPasswd(password.text.toString())
                        .yes {
                            hideSoftInput()
                            presenter.doLogin(username.text.toString(), password.text.toString())
                        }
                        .otherwise {
                            showTips(password, "密码不合法")
                        }
                }
                .otherwise {
                    showTips(username, "用户名不合法")
                }
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        val loginForm = findViewById<LinearLayout>(R.id.loginForm)
        loginForm.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        val loginProgress = findViewById<ProgressBar>(R.id.loginProgress)

        loginProgress.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun showTips(view: EditText, tips: String) {
        view.requestFocus()
        view.error = tips
    }

    fun onLoginStart() {
        showProgress(true)
    }

    fun onLoginError(e: Throwable) {
        e.printStackTrace()
        toast("登录失败")
        showProgress(false)
    }

    fun onLoginSuccess() {
        toast("登录成功")
        showProgress(false)
        val intent = Intent(BaseApp.context, MainActivity::class.java)
        startActivity(intent)
    }

    fun onDataInit(name: String, passwd: String) {
        username.setText(name)
        password.setText(passwd)
    }
}