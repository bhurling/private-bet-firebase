@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = rootProject.ext["compileSdk"] as Int
}

dependencies {
    api(Config.Libs.Rx.JAVA)
    api(Config.Libs.AndroidX.ANNOTATION)

    implementation(Config.Libs.Arch.MVRX)
    implementation(Config.Libs.Rx.KOTLIN)

    implementation(Config.Libs.Kotlin.STDLIB)
}
