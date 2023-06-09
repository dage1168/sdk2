package com.ggaab123.da5252.abdkdisskk;

import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DexUtils.loadDexClass(this);
    }


}
