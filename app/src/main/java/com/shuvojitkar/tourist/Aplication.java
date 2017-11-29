package com.shuvojitkar.tourist;

import android.app.Application;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

/**
 * Created by SHOBOJIT on 11/29/2017.
 */

public class Aplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Instabug.Builder(this, "af7e04ae83abbc45ad11316f2f86dd6d")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();
    }
}
