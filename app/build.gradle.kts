plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services") apply false
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

    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.transition:transition:1.0.1")
    implementation("com.google.android.material:material:1.0.0")

    implementation("com.google.firebase:firebase-core:16.0.8")
    implementation("com.google.firebase:firebase-auth:16.2.1")
    implementation("com.google.firebase:firebase-database:16.1.0")
    implementation("com.google.firebase:firebase-messaging:17.6.0")
    implementation("com.firebaseui:firebase-ui-auth:4.3.2")

    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")

    implementation("com.jakewharton.rxbinding2:rxbinding-kotlin:2.1.1")

    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.squareup.picasso:picasso:2.71828")

    compileOnly("javax.annotation:jsr250-api:1.0")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-inline:2.27.0")
    testImplementation("org.mockito:mockito-core:2.27.0")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
}

// Docs say this plugin should be applied at the bottom of the file
apply(mapOf("plugin" to "com.google.gms.google-services"))
