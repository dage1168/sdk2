# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 指定外部模糊字典 proguard-chinese.txt 改为混淆文件名，下同
#-obfuscationdictionary proguard-chinese.txt
# 指定class模糊字典
#-classobfuscationdictionary proguard-chinese.txt
# 指定package模糊字典
#-packageobfuscationdictionary proguard-chinese.txt

-optimizationpasses 5  #指定代码的压缩级别
-dontpreverify  #预校验
-verbose  #指定日志级别
-dontskipnonpubliclibraryclassmembers
-dontusemixedcaseclassnames  #包名不混合大小写
-ignorewarnings  #忽略警告
-printmapping proguardMapping.txt  #生成原类名和混淆后的类名的映射文件
-optimizations !code/simplification/cast,!field/*,!class/merging/*  #混淆时所采用的算法
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

###############android工程必须添加的不混淆配置 start#############
#四大组件、View体系等
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.* {*;}
-keep public class * extends android.support.v4.*

-keep public class * extends android.view.View{
    ** get*();
    void set*(**);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers public class * extends android.view.View {
   void set*(**);
   ** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#viewbinding
-keep class android.view.LayoutInflater{*;}
-keep public class * extends androidx.viewbinding.*{*;}

#Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class *.R$* {
 *;
}

-keepclassmembers class * {
    void *(*Event);
}

#enum
-keepclassmembers enum * {
    public static *[] values();
    public static * valueOf(java.lang.String);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

#Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#webview
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}


#---------------------------------Support Vx ---------------------------------

# support-v4
-dontwarn android.support.v4.*
-keep class android.support.v4.app.* { *; }
-keep interface android.support.v4.app.* { *; }
-keep class android.support.v4.* { *; }
-keep class android.support.v4.* {
    <fields>;
    <methods>;
}
-keep interface  android.support.v4.app.* {
    <fields>;
    <methods>;
}

# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.* { *; }
-keep interface android.support.v7.internal.* { *; }
-keep class android.support.v7.* { *; }

# support design
-dontwarn android.support.design.**
-keep class android.support.design.* { *; }
-keep interface android.support.design.* { *; }
-keep public class android.support.design.R$* { *; }

###############android工程必须添加的不混淆配置 end#############
