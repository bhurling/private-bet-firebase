plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    compileSdk = rootProject.ext["compileSdkVersion"] as Int

    defaultConfig {
        namespace = "io.hurling.privatebet.core.domain"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":core:auth"))
    api(project(":core:data"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    api(libs.kotlin.coroutines)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotest.assertions)
}
