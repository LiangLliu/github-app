package com.edwin.runtime.utils;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;
import java.util.Objects;


public class FragmentUtils {

    @SuppressLint("RestrictedApi")
    public static String getWhoFromFragment(Fragment fragment) {
        return fragment.mPreviousWho;
    }

    @SuppressLint("RestrictedApi")
    public static Fragment findFragmentByWho(FragmentManager fragmentManager, String who) {
        List<Fragment> fragments =
                fragmentManager.getFragments();

        for (Fragment fragment : fragments) {
            if (Objects.equals(fragment.mPreviousWho, who)) {
                return fragment;
            }
        }

        return null;
    }

}
