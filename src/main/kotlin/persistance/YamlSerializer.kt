package persistance

import com.fasterxml.jackson.core.type.TypeReference
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import models.Note
import persistance.Serializer

class YamlSerializer(private val file: File) : Serializer {

    //this code was all optained from various online sources

    override fun read(): Any {
        val yamlMapper = ObjectMapper(YAMLFactory())
        yamlMapper.registerModule(KotlinModule())
        val reader = FileReader(file)
        val typeReference = object : TypeReference<List<Note>>() {}
        val obj = yamlMapper.readValue<List<Note>>(reader, typeReference)
        reader.close()
        return obj
    }

    override fun write(obj: Any?) {
        val yamlMapper = ObjectMapper(YAMLFactory())
        yamlMapper.registerModule(KotlinModule())
        val writer = FileWriter(file)
        yamlMapper.writeValue(writer, obj)
        writer.close()
    }
}
