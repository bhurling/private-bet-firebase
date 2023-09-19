@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.extensions)
}

android {
    compileSdk = rootProject.ext["compileSdk"] as Int

    defaultConfig {
        applicationId = "io.bhurling.privatebet"
        namespace = "io.bhurling.privatebet"
        versionCode = 1
        versionName = "1.0"

        minSdk = rootProject.ext["minSdk"] as Int
        targetSdk = rootProject.ext["targetSdk"] as Int
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":lib-arch"))
    implementation(project(":lib-navigation"))
    implementation(project(":lib-ui"))
    implementation(project(":lib-rx-firebase"))

    implementation(libs.koin)
    implementation(libs.koin.viewmodel)

    implementation(libs.androidx.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.transition)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.authui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)

    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.rx.kotlin)
    implementation(libs.rx.binding)

    implementation(libs.material)
    implementation(libs.picasso)

    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
}

// Docs say this plugin should be applied at the bottom of the file
apply(plugin = "com.google.gms.google-services")
