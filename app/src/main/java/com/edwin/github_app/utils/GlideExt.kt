package com.edwin.github_app.utils

import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

fun AppCompatAvatarImageView.loadWithGlide(
    url: String,
    textPlaceHolder: Char,
    requestOptions: RequestOptions = RequestOptions()
) {
    textPlaceHolder.toString().let {
        setTextAndColorSeed(it.uppercase(Locale.getDefault()), it.hashCode().toString())
    }

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}