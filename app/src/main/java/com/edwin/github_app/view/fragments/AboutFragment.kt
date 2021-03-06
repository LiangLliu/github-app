package com.edwin.github_app.view.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edwin.github_app.R
import com.edwin.github_app.utils.markdownText
import com.edwin.github_app.view.common.CommonSinglePageFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.AnkoContext.Companion
import org.jetbrains.anko.sdk15.listeners.onClick

class AboutFragment : CommonSinglePageFragment() {
    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return AboutFragmentUI()
            .createView(Companion.create(requireContext(), this))
    }
}

class AboutFragmentUI : AnkoComponent<AboutFragment> {
    override
    fun createView(ui: AnkoContext<AboutFragment>) = ui.apply {

        scrollView {

            verticalLayout {

                imageView {
                    imageResource = R.mipmap.ic_launcher
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                themedTextView(R.string.detail_title, R.style.detail_title) {

                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                themedTextView(R.string.detail_description, R.style.detail_description) {

                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                themedTextView(R.string.open_source_licenses, R.style.detail_description) {
                    onClick {
                        alert {
                            customView {
                                scrollView {
                                    textView {
                                        padding = dip(10)
                                        markdownText =
                                            context
                                                .assets
                                                .open("licenses.md")
                                                .bufferedReader()
                                                .readText()
                                    }
                                }
                            }
                        }.show()
                    }
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER
            }
        }
    }.view
}