package com.edwin.runtime.core;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface OnFragmentCreateListener {
    void onFragmentCreated(Fragment fragment, Bundle savedInstanceState);
}