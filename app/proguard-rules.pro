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


-keepparameternames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,JsonProperty,JsonDeserialize
-keepattributes SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# The official support library.
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class android.support.v13.** { *; }
-keep interface android.support.v13.** { *; }

# v7 specifics
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep interface android.support.v7.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-dontwarn java.lang.invoke.*

# Java specific
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class javax.** { *; }
-keep class org.spongycastle.** { *; }
-keep class java8.** { *; }
-dontwarn java8.**

# Application
-keep,includedescriptorclasses class com.nordicit.fairymine.common.storage.model.** { *; }
-keep,includedescriptorclasses class com.nordicit.fairymine.common.form.** { *; }
-keep,includedescriptorclasses class com.nordicit.fairymine.api.** { *; }
-keep,includedescriptorclasses class com.nordicit.fairymine.api.model.** { *; }
-keep,includedescriptorclasses class com.nordicit.fairymine.api.interceptor.** { *; }

# WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ----------- Third party libraries and frameworks ----------------

# Parcel library
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Crashlytics
-keep class com.crashlytics.android.** { *; }
-keep class io.fabric.sdk.android.** { *; }
-dontwarn com.crashlytics.**

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Jodatime
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }


#RetroFit / okHttp
-dontwarn okio.**

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }

-dontwarn retrofit2.**
-dontwarn retrofit.appengine.UrlFetchClient
-keepattributes Signature
-keepattributes Exceptions
-keep,includedescriptorclasses class retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontnote org.apache.http.**
-dontnote android.net.http.**

# Dagger
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }

-dontwarn com.google.errorprone.annotations.*

#Sl4f
-dontwarn org.slf4j.**

#ViewPagerIndicator
-dontwarn com.viewpagerindicator.**

-verbose