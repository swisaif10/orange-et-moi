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


-keepattributes InnerClasses
-keepattributes EnclosingMethod
# Okio
-dontwarn okio.**
# Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn org.conscrypt.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.android.gms.measurement.** { *; }
-dontwarn com.google.android.gms.measurement.**
# Androidx
-keep class androidx.core.app.CoreComponentFactory { *; }
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.orange.ma.entreprise.fcm.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.cgu.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.commons.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.controlversion.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.dashboard.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.guest.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.login.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.settings.** {*;}
-keepclassmembers class com.orange.ma.entreprise.models.tabmenu.** {*;}
-keepclassmembers class com.orange.ma.entreprise.viewmodels.** {*;}
