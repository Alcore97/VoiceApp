package com.alcore.voiceapp.Database;

import com.orm.SugarApp;
import com.orm.SugarContext;

public class Custom extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
