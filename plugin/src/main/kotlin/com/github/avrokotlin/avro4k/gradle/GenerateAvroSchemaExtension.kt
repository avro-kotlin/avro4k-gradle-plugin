package com.github.avrokotlin.avro4k.gradle

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory

interface GenerateAvroSchemaExtension {
    @get:OutputDirectory
    val outputDirectory: DirectoryProperty
}