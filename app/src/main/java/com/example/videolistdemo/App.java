package com.example.videolistdemo;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class App extends Application {

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
