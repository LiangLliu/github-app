package com.edwin.runtime.result;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.edwin.runtime.core.OnActivityResultListener;
import com.edwin.runtime.result.fields.ActivityField;
import com.edwin.runtime.result.fields.FragmentField;
import com.edwin.runtime.result.fields.ViewField;
import com.edwin.runtime.utils.FragmentUtils;
import com.edwin.runtime.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListenerEnvironment {
    public final OnActivityResultListener onActivityResultListener;
    private ActivityField activityField;
    private final ArrayList<FragmentField> supportFragmentFields = new ArrayList<>();
    private final ArrayList<ViewField> viewFields = new ArrayList<>();

    public ListenerEnvironment(OnActivityResultListener onActivityResultListener) {
        this.onActivityResultListener = onActivityResultListener;

        try {
            Object obj = onActivityResultListener.realListener;
            Class cls = obj.getClass();
            Field outerRefField;

            while (!cls.isAnonymousClass()) {
                // 注意这个其实是 $this 引用，指向外部类实例
                outerRefField = cls.getDeclaredFields()[0];
                outerRefField.setAccessible(true);
                Object outerObject = outerRefField.get(obj);
                // 内联的 lambda
                if (outerObject == obj) break;
                cls = obj.getClass();
            }

            while (cls.getEnclosingClass() != null) {
                Class enclosingClass = cls.getEnclosingClass();
                Object enclosingObj = null;
                boolean hasRefOfEnclosingClass = false;
                for (Field field : cls.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (View.class.isAssignableFrom(field.getType())) {
                        int id = ((View) field.get(obj)).getId();
                        viewFields.add(new ViewField(obj, field, id));
                    } else if (Fragment.class.isAssignableFrom(field.getType())) {
                        String who = FragmentUtils.getWhoFromFragment((Fragment) field.get(obj));
                        supportFragmentFields.add(new FragmentField(obj, field, who));
                    } else if (Activity.class.isAssignableFrom(field.getType())) {
                        activityField = new ActivityField(obj, field);
                    }

                    if (field.getType() == enclosingClass) {
                        hasRefOfEnclosingClass = true;
                        enclosingObj = field.get(obj);
                    }
                }
                if (hasRefOfEnclosingClass) {
                    cls = enclosingClass;
                    obj = enclosingObj;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            Logger.warn(e);
        }
    }

    public void update(ResultFragment resultFragment) {
        if (activityField != null) {
            activityField.update(resultFragment.getActivity());
        }
        for (ViewField viewField : viewFields) {
            viewField.update(resultFragment.getActivity().findViewById(viewField.id));
        }
        if (resultFragment.getActivity() instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) resultFragment.getActivity();
            for (FragmentField fragmentField : supportFragmentFields) {
                Fragment fragment = FragmentUtils.findFragmentByWho(fragmentActivity.getSupportFragmentManager(), fragmentField.who);
                Logger.debug("Update, Who: " + fragmentField.who + ", fragment=" + fragment);
                fragmentField.update(fragment);
            }
        }
    }
}
