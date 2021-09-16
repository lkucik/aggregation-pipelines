package com.demo.aggregationpipelines.config

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.Instant

@Target(AnnotationTarget.FIELD)
annotation class KlaxonDate

val dateConverter = object : Converter {
    override fun canConvert(cls: Class<*>) = cls == Instant::class.java

    override fun fromJson(jv: JsonValue) =
        if (jv.string != null) {
            Instant.parse(jv.string)
        } else {
            throw KlaxonException("Couldn't parse date: ${jv.string}")
        }

    override fun toJson(value: Any) = """ { "date" : $value } """
}
