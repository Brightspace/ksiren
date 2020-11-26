package com.brightspace.ksiren.gson_adapter

import com.brightspace.ksiren.KSirenJsonWriter
import com.google.gson.stream.JsonWriter
import java.io.Closeable
import java.io.StringWriter

class KSirenGsonWriter : KSirenJsonWriter, Closeable {
	private val buffer = StringWriter()
	private val gsonWriter = JsonWriter(buffer).apply {
		serializeNulls = false
	}

	override fun getSerializedString(): String = buffer.toString()

	override fun beginObject(): KSirenJsonWriter = apply { gsonWriter.beginObject() }
	override fun endObject(): KSirenJsonWriter = apply { gsonWriter.endObject() }

	override fun beginArray(): KSirenJsonWriter = apply { gsonWriter.beginArray() }
	override fun endArray(): KSirenJsonWriter = apply { gsonWriter.endArray() }

	override fun close() {
		gsonWriter.close()
	}

	override fun name(name: String): KSirenJsonWriter = apply { gsonWriter.name(name) }

	override fun value(value: String?): KSirenJsonWriter = apply { gsonWriter.value(value) }
	override fun value(value: Number?): KSirenJsonWriter = apply { gsonWriter.value(value) }
	override fun value(value: Double?): KSirenJsonWriter = apply { gsonWriter.value(value) }
	override fun value(value: Boolean?): KSirenJsonWriter = apply { gsonWriter.value(value) }
}
