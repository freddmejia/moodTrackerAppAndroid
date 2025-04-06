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

##############################################
# ✨ General
##############################################
# Conservar todos los enums para evitar fallos en valueOf()
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Mantener clases con constructores vacíos usados con reflexión
-keepclassmembers class * {
    public <init> ();
}

##############################################
# 💉 Hilt + Dagger
##############################################
# Mantener clases anotadas con @Inject, @HiltViewModel, @Module, etc.
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }
-keep class androidx.hilt.** { *; }

# Mantener clases con anotaciones de Hilt para evitar errores con KAPT
-keep class * {
    @dagger.hilt.android.lifecycle.HiltViewModel *;
}
-keep class * {
    @dagger.Module *;
}
-keep class * {
    @javax.inject.Inject *;
}

##############################################
# 🧩 Jetpack Compose
##############################################
# No optimizar o eliminar clases internas necesarias para Compose
-keep class androidx.compose.** { *; }
-keep class androidx.activity.ComponentActivity { *; }
-keep class androidx.lifecycle.viewmodel.compose.** { *; }

##############################################
# 🗃️ Room Database
##############################################
# Mantener entidades y DAOs
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * {
    @androidx.room.* <methods>;
}

# Mantener tus modelos locales si se accede por reflexión
-keep class gamer.botixone.moodtracker.data.local_datasource.model.** { *; }

##############################################
# 📦 Gson / Moshi / Serialization (opcional)
##############################################
# (Descomenta si usas Gson, Moshi u otro JSON parser con reflexión)

# Gson
#-keep class com.google.gson.** { *; }
#-keep class * {
#    @com.google.gson.annotations.SerializedName *;
#}

# Moshi
#-keep class com.squareup.moshi.** { *; }
#-keep @com.squareup.moshi.JsonClass class * {
#    @com.squareup.moshi.* *;
#}

##############################################
# 🔄 Clases personalizadas con reflexión
##############################################
-keep class gamer.botixone.moodtracker.data.model.** { *; }

##############################################
# 🧪 Extras opcionales para seguridad y análisis
##############################################
# Mantener logs si los usas para debug o crash
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** i(...);
#}

# Evitar que R8 elimine métodos usados en XML
-keepclassmembers class * {
    public void *(android.view.View);
}
