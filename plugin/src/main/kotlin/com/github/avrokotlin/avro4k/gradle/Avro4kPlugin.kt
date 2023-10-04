package com.github.avrokotlin.avro4k.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

class Avro4kPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        //Do nothing
        val extension = target.extensions.create("generateAvroSchema", GenerateAvroSchemaExtension::class.java)
        target.tasks.register("generateAvroSchemas", GenerateSchemaTask::class.java) {
            val sourceSetsContainer= target.extensions.getByType(SourceSetContainer::class.java)
            val sourceSet = sourceSetsContainer.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            it.dependsOn(sourceSet.getCompileTaskName("kotlin"))
        }
    }
}