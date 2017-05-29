package com.giddy.image.giddysticker;

import android.app.Application;

import com.mz.ZAndroidSystemDK;

/**
 * Created by binhnk on 5/22/2017.
 */

public class GiddyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ZAndroidSystemDK.initApplication(this, getPackageName());
    }
}
