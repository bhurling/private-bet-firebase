object Config {

    object Versions {
        const val KOTLIN = "1.3.20"
    }

    object Plugins {
        const val ANDROID = "com.android.tools.build:gradle:3.3.2"
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val GOOGLE = "com.google.gms:google-services:4.2.0"
    }

    object Libs {

        object Kotlin {
            const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        }

        object AndroidX {
            const val ANNOTATION = "androidx.annotation:annotation:1.0.2"
            const val APPCOMPAT = "androidx.appcompat:appcompat:1.0.2"
            const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:1.1.3"
            const val CORE = "androidx.core:core-ktx:1.0.1"
            const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.0.0"
            const val TRANSITION = "androidx.transition:transition:1.0.1"
        }

        object Firebase {
            const val CORE = "com.google.firebase:firebase-core:17.0.1"
            const val AUTH = "com.google.firebase:firebase-auth:18.1.0"
            const val AUTH_UI = "com.firebaseui:firebase-ui-auth:5.0.0"
            const val DATABASE = "com.google.firebase:firebase-database:18.0.1"
            const val MESSAGING = "com.google.firebase:firebase-messaging:19.0.1"
        }

        object Rx {
            const val JAVA = "io.reactivex.rxjava2:rxjava:2.2.8"
            const val ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.1"
            const val KOTLIN = "io.reactivex.rxjava2:rxkotlin:2.3.0"
            const val BINDING_KOTLIN = "com.jakewharton.rxbinding2:rxbinding-kotlin:2.1.1"
        }

        object Arch {
            const val MVRX = "com.airbnb.android:mvrx:1.0.1"
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
        const val MOCKITO = "org.mockito:mockito-core:2.27.0"
        const val MOCKITO_INLINE = "org.mockito:mockito-inline:2.27.0"
        const val MOCKITO_KOTLIN = "com.nhaarman:mockito-kotlin:1.6.0"
    }
}