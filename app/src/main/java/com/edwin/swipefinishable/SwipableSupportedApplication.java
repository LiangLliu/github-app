package com.edwin.swipefinishable;

import android.app.Application;

/**
 * Created by benny on 9/24/16.
 */
public class SwipableSupportedApplication extends Application{
    public static final String TAG = "SwipableSupportedApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        SwipeFinishable.INSTANCE.init(this);
    }
}
