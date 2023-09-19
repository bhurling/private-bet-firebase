import com.android.build.gradle.BaseExtension

buildscript {

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    dependencies {
        classpath(Config.Plugins.ANDROID)
        classpath(Config.Plugins.KOTLIN)
        classpath(Config.Plugins.GOOGLE)
        classpath(Config.Plugins.VERSIONS)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()

        maven(url = "https://maven.fabric.io/public")

        jcenter()
    }
}

subprojects {
    afterEvaluate {
        configure<BaseExtension> {
            compileSdkVersion(33)

            defaultConfig {
                minSdk = 21
                targetSdk = 33

                vectorDrawables.useSupportLibrary = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            lintOptions {
                // We don't care about accessibility features
                disable("ContentDescription")
            }
        }
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
