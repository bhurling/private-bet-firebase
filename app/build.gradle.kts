plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services") apply false
    id("com.github.ben-manes.versions")
}

android {
    defaultConfig {
        applicationId = "io.bhurling.privatebet"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":lib-kotterknife"))
    implementation(project(":lib-arch"))
    implementation(project(":lib-di"))

    implementation(Config.Libs.AndroidX.CORE)
    implementation(Config.Libs.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Config.Libs.AndroidX.TRANSITION)

    implementation(Config.Libs.Firebase.CORE)
    implementation(Config.Libs.Firebase.AUTH)
    implementation(Config.Libs.Firebase.AUTH_UI)
    implementation(Config.Libs.Firebase.DATABASE)
    implementation(Config.Libs.Firebase.MESSAGING)

    implementation(Config.Libs.Rx.ANDROID)
    implementation(Config.Libs.Rx.JAVA)
    implementation(Config.Libs.Rx.KOTLIN)
    implementation(Config.Libs.Rx.BINDING_KOTLIN)

    implementation(Config.Libs.Ui.MATERIAL)
    implementation(Config.Libs.Ui.PICASSO)

    implementation(Config.Libs.Logging.TIMBER)

    testImplementation(Config.TestLibs.JUNIT)
    testImplementation(Config.TestLibs.MOCKITO)
    testImplementation(Config.TestLibs.MOCKITO_INLINE)
    testImplementation(Config.TestLibs.MOCKITO_KOTLIN)
}

// Docs say this plugin should be applied at the bottom of the file
apply(plugin = "com.google.gms.google-services")
