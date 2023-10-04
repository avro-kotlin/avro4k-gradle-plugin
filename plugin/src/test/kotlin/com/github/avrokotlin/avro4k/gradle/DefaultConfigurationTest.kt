package com.github.avrokotlin.avro4k.gradle

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class DefaultConfigurationTest {
    @Test
    fun `should generate avro schema`(){
        val projectDir = Paths.get("../examples/default-configuration-project")        
        val result = GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withArguments("generateAvroSchema")
            .withDebug(true)
            .withPluginClasspath()
            .build()
        assertThat(result.output).contains("BUILD SUCCESSFUL")
        assertThat(projectDir.resolve("build/generated/sources/avro/com.example.User.avsc")).exists()
    }
}