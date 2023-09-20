allprojects {
    repositories {
        google()
        mavenCentral()

        maven(url = "https://maven.fabric.io/public")
    }
}

ext {
    set("compileSdk", 33)
    set("targetSdk", 33)
    set("minSdk", 21)
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
