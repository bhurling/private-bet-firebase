allprojects {
    repositories {
        google()
        mavenCentral()

        maven(url = "https://maven.fabric.io/public")
    }
}

ext {
    set("compileSdk", 34)
    set("targetSdk", 34)
    set("minSdk", 21)
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
