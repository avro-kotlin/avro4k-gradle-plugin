package com.github.avrokotlin.avro4k.gradle

import org.gradle.api.tasks.SourceSet
import org.objectweb.asm.ClassReader

class SerializableClassScanner(sourceSet: SourceSet) {
    private val outputDir = sourceSet.output.classesDirs
    private val classLoader = sourceSet.runtimeClasspath.toClassloader()

    @Suppress("UNCHECKED_CAST")
    private val serializableClass = classLoader.loadClass("kotlinx.serialization.Serializable") as Class<Annotation>
    fun scanForSerializedClasses(): List<String> {
        val serializableClasses = mutableListOf<String>()

        val classFiles = outputDir.asFileTree.matching { it.include("**/*.class") }.files.toList()
        for (url in classFiles) {
            val path = url.path
            val strippedPath = removeOutputDirPrefix(path)
            val className = getClassNameFromPath(strippedPath)
            if (isSerializableClass(className)) {
                serializableClasses.add(className)
            }
        }

        return serializableClasses
    }

    private fun removeOutputDirPrefix(path: String): String {
        return outputDir.files.map { it.path + "/" }.filter { path.startsWith(it) }.map { path.removePrefix(it) }
            .first()
    }

    private fun getClassNameFromPath(classFilePath: String): String {
        val classReader = ClassReader(classLoader.getResourceAsStream(classFilePath))
        return classReader.className.replace("/", ".")
    }

    private fun isSerializableClass(className: String): Boolean {
        return try {
            val clazz = classLoader.loadClass(className)
            clazz.getAnnotation(serializableClass) != null
        } catch (e: Throwable) {
            // Class not found
            false
        }
    }
}