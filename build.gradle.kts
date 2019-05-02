import com.android.build.gradle.BaseExtension

buildscript {

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.20")
        classpath("com.google.gms:google-services:4.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

        maven(url = "https://maven.fabric.io/public")
    }
}

subprojects {
    afterEvaluate {
        configure<BaseExtension> {
            compileSdkVersion(28)

            defaultConfig {
                minSdkVersion(21)
                targetSdkVersion(28)

                vectorDrawables.useSupportLibrary = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
