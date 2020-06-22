plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api(Config.Libs.Kotlin.STDLIB)

    api(Config.Libs.AndroidX.CORE)
    api(Config.Libs.AndroidX.RECYCLERVIEW)
}
