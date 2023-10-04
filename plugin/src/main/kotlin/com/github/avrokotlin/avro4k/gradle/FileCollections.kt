package com.github.avrokotlin.avro4k.gradle

import org.gradle.api.file.FileCollection
import java.net.URL
import java.net.URLClassLoader

fun FileCollection.toClassloader(vararg additionalUrls: URL): ClassLoader {
    val urls = this.files.map { it.toURI().toURL() }.toTypedArray() + additionalUrls
    return URLClassLoader(urls, javaClass.classLoader)
}