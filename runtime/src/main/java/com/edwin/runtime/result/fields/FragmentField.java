package com.edwin.runtime.result.fields;

import com.edwin.runtime.utils.Logger;

import java.lang.reflect.Field;

/**
 * Created by benny on 2/6/18.
 */

public class FragmentField extends ListenerField {
    public final String who;

    public FragmentField(Object object, Field field, String who) {
        super(object, field);
        try {
            Logger.debug("Setup, Who: " + who + ", fragment=" + field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.who = who;
    }
}
