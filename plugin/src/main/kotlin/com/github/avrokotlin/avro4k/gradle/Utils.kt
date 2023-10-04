package com.github.avrokotlin.avro4k.gradle

import com.github.avrokotlin.avro4k.Avro
import kotlinx.serialization.modules.SerializersModule

fun getAvroConfig(str: String?): Avro {
    return str?.let {
        val clazz = Class.forName(it)

        val instance = try {
            clazz.kotlin.objectInstance
        } catch (e: Throwable) {
            null
        }
        clazz.methods.first {
            it.name == "getAvro" && it.parameterCount == 0 && it.returnType == Avro::class.java
        }.invoke(instance) as? Avro
    } ?: Avro.default

}

val avro = Avro(SerializersModule {})

object AvroProvider {
    val avro = Avro(SerializersModule {})
}
class AvroCompanion {
    companion object {
        val avro = Avro(SerializersModule {})
    }
}

fun main() {
    println(getAvroConfig("com.github.avrokotlin.gradle.UtilsKt"))
    println(getAvroConfig("com.github.avrokotlin.gradle.AvroProvider"))
    println(getAvroConfig("com.github.avrokotlin.gradle.AvroCompanion"))
}