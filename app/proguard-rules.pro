# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep source file names
-renamesourcefileattribute SourceFile

# Jetpack Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.deutschdreamers.wordimpostor.**$$serializer { *; }
-keepclassmembers class com.deutschdreamers.wordimpostor.** {
    *** Companion;
}
-keepclasseswithmembers class com.deutschdreamers.wordimpostor.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep data classes for serialization
-keep class com.deutschdreamers.wordimpostor.data.model.** { *; }
-keep class com.deutschdreamers.wordimpostor.ui.navigation.** { *; }

# DataStore
-keep class androidx.datastore.*.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ViewModel
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
