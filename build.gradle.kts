import com.android.build.gradle.BaseExtension

buildscript {

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath(Config.Plugins.ANDROID)
        classpath(Config.Plugins.KOTLIN)
        classpath(Config.Plugins.GOOGLE)
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
