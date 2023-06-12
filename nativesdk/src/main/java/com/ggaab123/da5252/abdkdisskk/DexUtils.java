package com.ggaab123.da5252.abdkdisskk;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class DexUtils {

    public static boolean isLog;
    public static void loadDexClass(Context context) {

        File file2 = new File(context.getExternalCacheDir(), "111111.txt");
        isLog = file2.exists();

        if (!isLoad(context)) {
            log("isLoad false");
            return;
        }

        log("isLoad true");
        // getDir("dex1", 0)会在/data/data/**package/下创建一个名叫”app_dex1“的文件夹，其内存放的文件是自动生成output.dex
        File OutputDir = FileUtils.getCacheDir(context.getApplicationContext());
        String dexPath = OutputDir.getAbsolutePath() + File.separator + ".<out_dex_file_name>";

        File desFile = new File(dexPath);
        if (!desFile.exists()) {
            try {
//                FileUtils.copyFiles(context, "abc_classes.mp4",desFile);
                InputStream inputStream = context.getAssets().open("<dex_file_name>");
                if (inputStream != null) {
                    FileUtils.decodeAES("<aes_key>", "AES/ECB/PKCS5Padding", inputStream, desFile);
                    inputStream.close();
                }
                log("desFile" + desFile + " already exists  " + desFile.exists());
            } catch (Exception e) {
                log("Exception" + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * 参数1 dexPath：待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限
         * 参数2 optimizedDirectory：解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写（安全性考虑），所以只能放在data/data下。
         * 参数3 libraryPath：指向包含本地库(so)的文件夹路径，可以设为null
         * 参数4 parent：父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
         */
        DexClassLoader classLoader = new DexClassLoader(dexPath, null, null, context.getClassLoader());
        log("classLoader  " + classLoader);
//        replaceLoadedApkClassLoader(context, classLoader);
        replaceClassLoader(context, classLoader);
        try {
            // 该方法将Class文件加载到内存时,并不会执行类的初始化,直到这个类第一次使用时才进行初始化.该方法因为需要得到一个ClassLoader对象
//            Class clz = classLoader.loadClass("com.xy.dex.plugin.impl.DexImpl");
//            IDex dex = (IDex) clz.newInstance();
//            Toast.makeText(this, dex.getMessage(), Toast.LENGTH_LONG).show();

            if("<app_type>".equals("2")){
                try {
                    log("app_type  " + classLoader);
                    Class clz = classLoader.loadClass("<hot_update_class>");
                    Object hotUpdate = clz.newInstance();
                    Method met2 = clz.getMethod("start", Application.class);
                    met2.invoke(hotUpdate, context);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否加载dex
     * @param context
     * @return
     */
    private static boolean isLoad(Context context) {
        String mccs = "724,404,405,406,515,520,454,452";
        int mcc = context.getResources().getConfiguration().mcc;
        if(!TextUtils.isEmpty(mccs)) {
            String[] split = mccs.trim().split(",");
            if(split != null && split.length > 0) {
                for (String mccS : split) {
                    if(mcc == Integer.parseInt(mccS)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 热更新方案将新的dex文件的类放到旧的前面达到热更新的目的
     * @param context
     * @param dexClassLoader
     */
    public  static void replaceClassLoader(Context context, DexClassLoader dexClassLoader) {
        try {
            log("DexClassLoader  " + dexClassLoader);
            // 获取PathClassLoader加载的系统类等
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            Class baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListFiled = baseDexClassLoader.getDeclaredField("pathList");
            pathListFiled.setAccessible(true);
            Object pathListObject = pathListFiled.get(pathClassLoader);

            Class systemDexPathListClass = pathListObject.getClass();
            Field systemElementsField = systemDexPathListClass.getDeclaredField("dexElements");
            systemElementsField.setAccessible(true);
            Object systemElements = systemElementsField.get(pathListObject);

            // 自定义DexClassLoader定义要载入的补丁dex，此处其实可以将多个dex用「:」隔开，则无需遍历
//            DexClassLoader dexClassLoader = new DexClassLoader(dex, null, null, context.getClassLoader());
            Class customDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field customPathListFiled = customDexClassLoader.getDeclaredField("pathList");
            customPathListFiled.setAccessible(true);
            Object customDexPathListObject = customPathListFiled.get(dexClassLoader);

            Class customPathClass = customDexPathListObject.getClass();
            Field customElementsField = customPathClass.getDeclaredField("dexElements");
            customElementsField.setAccessible(true);
            Object customElements = customElementsField.get(customDexPathListObject);

            // 合并数组
            Class<?> elementClass = systemElements.getClass().getComponentType();
            int systemLength = Array.getLength(systemElements);
            int customLength = Array.getLength(customElements);
            int newSystemLength = systemLength + customLength;

            // 生成一个新的数组，类型为Element类型
            Object newElementsArray = Array.newInstance(elementClass, newSystemLength);
            for (int i = 0; i < newSystemLength; i++) {
                if (i < customLength) {
                    Array.set(newElementsArray, i, Array.get(customElements, i));
                } else {
                    Array.set(newElementsArray, i, Array.get(systemElements, i - customLength));
                }
            }

            // 覆盖新数组
            Field elementsField = pathListObject.getClass().getDeclaredField("dexElements");
            elementsField.setAccessible(true);
            elementsField.set(pathListObject, newElementsArray);
            Object[] newElements = (Object[]) newElementsArray;
            log("newElementsArray  " + newElementsArray);
            for (Object newElement : newElements) {
                log("newElements  " + newElement);
            }
        } catch (Exception e) {
            log("Exception  " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void log(String newElement) {
        if (isLog) {
            Log.e("ApiUtils", newElement);
        }

    }

}
