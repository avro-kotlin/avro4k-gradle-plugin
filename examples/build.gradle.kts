plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}
buildscript {
    dependencies {
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    group = "com.github.avro-kotlin.avro4k.example"
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation("com.github.avro-kotlin.avro4k:avro4k-core:1.10.0")
    }
    kotlin {
        jvmToolchain(8)
    }
}