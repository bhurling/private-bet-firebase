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
