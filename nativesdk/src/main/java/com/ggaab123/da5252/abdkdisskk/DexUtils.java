package com.ggaab123.da5252.abdkdisskk;

import android.content.Context;
import android.util.ArrayMap;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class DexUtils {
    public static void loadDexClass(Context context) {
        // getDir("dex1", 0)会在/data/data/**package/下创建一个名叫”app_dex1“的文件夹，其内存放的文件是自动生成output.dex
        File OutputDir = FileUtils.getCacheDir(context.getApplicationContext());
        String dexPath = OutputDir.getAbsolutePath() + File.separator + ".<out_dex_file_name>";

        File desFile = new File(dexPath);
        if (!desFile.exists()) {
            try {
                InputStream inputStream = context.getAssets().open("<dex_file_name>");
                if (inputStream != null) {
                    FileUtils.decodeAES("<aes_key>", "AES/ECB/PKCS5Padding", inputStream, desFile);
                    inputStream.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 参数1 dexPath：待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限
         * 参数2 optimizedDirectory：解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写（安全性考虑），所以只能放在data/data下。
         * 参数3 libraryPath：指向包含本地库(so)的文件夹路径，可以设为null
         * 参数4 parent：父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
         */
        DexClassLoader classLoader = new DexClassLoader(dexPath, OutputDir.getAbsolutePath(), null, context.getClassLoader());
        replaceLoadedApkClassLoader(context, classLoader);
        try {
            // 该方法将Class文件加载到内存时,并不会执行类的初始化,直到这个类第一次使用时才进行初始化.该方法因为需要得到一个ClassLoader对象
//            Class clz = classLoader.loadClass("com.xy.dex.plugin.impl.DexImpl");
//            IDex dex = (IDex) clz.newInstance();
//            Toast.makeText(this, dex.getMessage(), Toast.LENGTH_LONG).show();
//            try {
//                Class clz = classLoader.loadClass("com.ggaab123.da5252.abdkdisskk.StartActivity");
//                startActivity(new Intent(this, clz));
//                finish();
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 替换 LoadedApk 中的 类加载器 ClassLoader
     *
     * @param context
     * @param loader  动态加载dex的ClassLoader
     */
    public static void replaceLoadedApkClassLoader(Context context, DexClassLoader loader) {
        // I. 获取 ActivityThread 实例对象
        // 获取 ActivityThread 字节码类 , 这里可以使用自定义的类加载器加载
        // 原因是 基于 双亲委派机制 , 自定义的 DexClassLoader 无法加载 , 但是其父类可以加载
        // 即使父类不可加载 , 父类的父类也可以加载
        Class<?> activityThreadClass = null;
        try {
            activityThreadClass = loader.loadClass("android.app.ActivityThread");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 获取 ActivityThread 中的 sCurrentActivityThread 成员
        // 获取的字段如下 :
        // private static volatile ActivityThread sCurrentActivityThread;
        // 获取字段的方法如下 :
        // public static ActivityThread currentActivityThread() {return sCurrentActivityThread;}
        Method currentActivityThreadMethod = null;
        try {
            currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            // 设置可访问性 , 所有的 方法 , 字段 反射 , 都要设置可访问性
            currentActivityThreadMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 执行 ActivityThread 的 currentActivityThread() 方法 , 传入参数 null
        Object activityThreadObject = null;
        try {
            activityThreadObject = currentActivityThreadMethod.invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // II. 获取 LoadedApk 实例对象
        // 获取 ActivityThread 实例对象的 mPackages 成员
        // final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<>();
        Field mPackagesField = null;
        try {
            mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            // 设置可访问性 , 所有的 方法 , 字段 反射 , 都要设置可访问性
            mPackagesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        // 从 ActivityThread 实例对象 activityThreadObject 中
        // 获取 mPackages 成员
        ArrayMap<String, WeakReference<Object>> mPackagesObject = null;
        try {
            mPackagesObject = (ArrayMap<String, WeakReference<Object>>) mPackagesField.get(activityThreadObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 获取 WeakReference<LoadedApk> 弱引用对象
        WeakReference<Object> weakReference = mPackagesObject.get(context.getPackageName());
        // 获取 LoadedApk 实例对象
        Object loadedApkObject = weakReference.get();


        // III. 替换 LoadedApk 实例对象中的 mClassLoader 类加载器
        // 加载 android.app.LoadedApk 类
        Class<?> loadedApkClass = null;
        try {
            loadedApkClass = loader.loadClass("android.app.LoadedApk");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 通过反射获取 private ClassLoader mClassLoader; 类加载器对象
        Field mClassLoaderField = null;
        try {
            mClassLoaderField = loadedApkClass.getDeclaredField("mClassLoader");
            // 设置可访问性
            mClassLoaderField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        // 替换 mClassLoader 成员
        try {
            mClassLoaderField.set(loadedApkObject, loader);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
