@file:OptIn(InternalSerializationApi::class)

package com.github.avrokotlin.avro4k.gradle

import kotlinx.serialization.InternalSerializationApi
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction


abstract class GenerateSchemaTask : DefaultTask() {
   

    @TaskAction
    fun generateSchema() {
        val sourceSetsContainer = project.extensions.getByType(SourceSetContainer::class.java)
        val sourceSet = sourceSetsContainer.getByName(SourceSet.MAIN_SOURCE_SET_NAME)

        val scanner = SerializableClassScanner(sourceSet)

        val serializedClasses = scanner.scanForSerializedClasses()
        // Generate avro schemas
        val outputDir = project.file("${project.buildDir}/generated/sources/avro/")
        outputDir.mkdirs()
        val avroSchemaGenerator = AvroSchemaGeneratorFacade(sourceSet)

        serializedClasses.forEach {
            avroSchemaGenerator.writeSchema(outputDir, it)
        }
    }
}



