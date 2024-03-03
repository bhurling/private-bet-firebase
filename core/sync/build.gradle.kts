plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    compileSdk = rootProject.ext["compileSdkVersion"] as Int

    defaultConfig {
        namespace = "io.hurling.privatebet.core.sync"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":core:auth"))
    implementation(project(":core:data"))

    api(libs.androidx.work)

    implementation(libs.hilt.android)
    api(libs.hilt.ext.work)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)
}
