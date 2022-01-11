package com.edwin.runtime;

import android.content.Context;

import com.edwin.runtime.core.ActivityBuilder;

public class Tieguanyin {
    public static void init(Context context) {
        ActivityBuilder.INSTANCE.init(context);
    }

    public static void init(Context context, ActivityBuilderCallback callback) {
        ActivityBuilder.INSTANCE.init(context);
        ActivityBuilder.INSTANCE.setBuilderCallback(callback);
    }
}
