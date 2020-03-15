object Config {

    object Versions {
        const val KOTLIN = "1.3.70"
    }

    object Plugins {
        const val ANDROID = "com.android.tools.build:gradle:3.6.0"
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val GOOGLE = "com.google.gms:google-services:4.3.2"
        const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
    }

    object Libs {

        object Kotlin {
            const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        }

        object AndroidX {
            const val ANNOTATION = "androidx.annotation:annotation:1.0.2"
            const val APPCOMPAT = "androidx.appcompat:appcompat:1.1.0"
            const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:1.1.3"
            const val CORE = "androidx.core:core-ktx:1.1.0"
            const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.0.0"
            const val TRANSITION = "androidx.transition:transition:1.2.0"
        }

        object Firebase {
            const val AUTH = "com.google.firebase:firebase-auth:19.3.0"
            const val AUTH_UI = "com.firebaseui:firebase-ui-auth:6.2.0"
            const val DATABASE = "com.google.firebase:firebase-firestore-ktx:21.4.1"
            const val MESSAGING = "com.google.firebase:firebase-messaging:20.1.2"
        }

        object Rx {
            const val JAVA = "io.reactivex.rxjava2:rxjava:2.2.19"
            const val ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.1"
            const val KOTLIN = "io.reactivex.rxjava2:rxkotlin:2.4.0"
            const val BINDING_KOTLIN = "com.jakewharton.rxbinding2:rxbinding-kotlin:2.2.0"
        }

        object Arch {
            const val MVRX = "com.airbnb.android:mvrx:1.2.1"
        }

        object Ui {
            const val MATERIAL = "com.google.android.material:material:1.0.0"
            const val PICASSO = "com.squareup.picasso:picasso:2.71828"
        }

        object Logging {
            const val TIMBER = "com.jakewharton.timber:timber:4.7.1"
        }
    }

    object TestLibs {
        const val JUNIT = "junit:junit:4.12"
        const val MOCKITO = "org.mockito:mockito-core:3.1.0"
        const val MOCKITO_INLINE = "org.mockito:mockito-inline:3.1.0"
        const val MOCKITO_KOTLIN = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    }
}