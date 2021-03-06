package com.edwin.swipefinishable;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by benny on 9/19/16.
 */
public abstract class BaseSwipeFinishableActivity extends Activity implements SwipeFinishable.SwipeFinishableActivity {
    public static final String TAG = "BaseSwipeFinishableActivity";

    private SwipeFinishablePlugin swipeFinishablePlugin = new SwipeFinishablePlugin(this);

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        SwipeFinishable.INSTANCE.finishCurrentActivity();
    }

    @Override
    public void finishThisActivity() {
        super.finish();
    }
}
