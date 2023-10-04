pluginManagement {
    includeBuild("../plugin")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "avro4k-gradle-plugin-examples"

includeBuild("../plugin")
include(":default-configuration-project")

