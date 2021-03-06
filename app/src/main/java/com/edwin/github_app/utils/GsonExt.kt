package com.edwin.github_app.utils

import com.google.gson.Gson

/**
 * Created by benny on 1/20/18.
 */
inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)