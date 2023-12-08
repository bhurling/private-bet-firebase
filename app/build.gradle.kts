@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    hilt {
        enableAggregatingTask = false
    }
}

dependencies {
    implementation(project(":lib-arch"))
    implementation(project(":lib-navigation"))
    implementation(project(":lib-ui"))
    implementation(project(":lib-rx-firebase"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
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

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
}
