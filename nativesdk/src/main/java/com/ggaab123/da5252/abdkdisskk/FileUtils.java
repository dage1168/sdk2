package com.ggaab123.da5252.abdkdisskk;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileUtils {
    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int i;
            while ((i = in.read(bytes)) != -1)
                out.write(bytes, 0, i);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return 返回缓存文件路径
     */
    public static File getCacheDir(Context context) {
        File cache;
        if (hasExternalStorage()) {
            cache = context.getExternalCacheDir();
        } else {
            cache = context.getCacheDir();
        }
        if (!cache.exists())
            cache.mkdirs();
        return cache;
    }

    public static void decodeAES(String str, String str3, InputStream fis, File file) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
        Cipher instance = Cipher.getInstance(str3);
        instance.init(2, secretKeySpec);
        //        byte[] doFinal = instance.doFinal(bArr);

        //        InputStream fis = new ByteArrayInputStream(bArr);
        FileOutputStream fos = new FileOutputStream(file);
        //        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, instance);
        int b;
        byte[] d = new byte[2048];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }
}
