plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.9.0"
}
group = "com.github.avro-kotlin.avro4k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    compileOnly("com.github.avro-kotlin.avro4k:avro4k-core:1.10.0")
    implementation("org.ow2.asm:asm:9.5")
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
gradlePlugin {
    plugins {
        create("avro4kPlugin") {
            id = "com.github.avro-kotlin.avro4k"
            implementationClass = "com.github.avrokotlin.avro4k.gradle.Avro4kPlugin"
        }
    }
}