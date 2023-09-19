enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":app")
include(":lib-arch")
include(":lib-navigation")
include(":lib-ui")
include(":lib-rx-firebase")
