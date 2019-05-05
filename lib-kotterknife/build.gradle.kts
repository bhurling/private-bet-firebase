plugins {
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    api(Config.Libs.AndroidX.APPCOMPAT)
    api(Config.Libs.AndroidX.RECYCLERVIEW)

    api(Config.Libs.Kotlin.STDLIB)
}
