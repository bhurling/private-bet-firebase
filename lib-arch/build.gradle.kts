plugins {
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    api(Config.Libs.Rx.JAVA)
    api(Config.Libs.AndroidX.ANNOTATION)

    implementation(Config.Libs.Arch.MVRX)

    implementation(Config.Libs.Kotlin.STDLIB)
}
