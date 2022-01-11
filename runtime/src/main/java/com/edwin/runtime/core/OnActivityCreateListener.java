package com.edwin.runtime.core;

import android.app.Activity;
import android.os.Bundle;

public interface OnActivityCreateListener {
    void onActivityCreated(Activity activity, Bundle savedInstanceState);
}
