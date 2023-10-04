package com.github.avrokotlin.avro4k.gradle

import com.github.avrokotlin.avro4k.Avro
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import org.gradle.api.tasks.SourceSet
import java.io.File
import java.lang.reflect.Method
import java.net.URL

private const val avroSchemaGeneratorClassName = "com.github.avrokotlin.avro4k.gradle.AvroSchemaGenerator"

interface IAvroSchemaGenerator {
    fun writeSchema(outputDir: File, className: String)
}

/**
 * Only this facade should be used. Never use AvroSchemaGenerator directly.
 * This facade guarantees that the correct classpath of the provided sourceSet is used to generate
 * the avro schemas.
 */
class AvroSchemaGeneratorFacade(sourceSet: SourceSet) : IAvroSchemaGenerator {
    private val clazz: Class<*>
    private val writeSchemaMethod: Method
    private val avroSchemaGenerator: Any

    init {
        //Find out the jar file in which the AvroSchemaGenerator lies.        
        val classFileName = avroSchemaGeneratorClassName.replace('.', '/') + ".class"
        val classFile = this.javaClass.classLoader.getResource(classFileName)
            ?: throw IllegalStateException("Could not find $classFileName in the current classloader. Cannot generate avro schemas.")
        val urlToJarForThisPlugin = URL(classFile.toString().removePrefix("jar:").removeSuffix("!/$classFileName"))
        val customCodeClassLoader = sourceSet.runtimeClasspath.toClassloader(urlToJarForThisPlugin)
        clazz = customCodeClassLoader.loadClass("com.github.avrokotlin.avro4k.gradle.AvroSchemaGenerator")
        writeSchemaMethod = clazz.methods.first { it.name == "writeSchema" }
        avroSchemaGenerator = clazz.constructors.first().newInstance()
    }

    override fun writeSchema(outputDir: File, className: String) {
        writeSchemaMethod.invoke(avroSchemaGenerator, outputDir, className)
    }
}

@OptIn(InternalSerializationApi::class)
class AvroSchemaGenerator : IAvroSchemaGenerator {

    private val avro: Avro by lazy {
        Avro.default
    }

    private fun schema(className: String): Schema {
        val clazz = Class.forName(className)
        return avro.schema(clazz.kotlin.serializer())
    }

    override fun writeSchema(outputDir: File, className: String) {
        val schema = schema(className)
        val fullName = schema.fullName
        val avroSchemaFile = outputDir.resolve("${fullName}.avsc")

        avroSchemaFile.writeText(schema.toString(true))
        println("Schema '$fullName' for class '$className' written to '$avroSchemaFile'.")
    }

}