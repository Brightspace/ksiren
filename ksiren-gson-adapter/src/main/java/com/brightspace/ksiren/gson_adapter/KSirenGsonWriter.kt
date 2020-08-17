package com.brightspace.ksiren.gson_adapter

import com.brightspace.ksiren.KSirenJsonWriter
import com.google.gson.stream.JsonWriter

class KSirenGsonWriter(
	private val writer: JsonWriter,
	override val getSerializedString: () -> String) : KSirenJsonWriter {

	init {
		writer.serializeNulls = false
	}

	override fun beginObject(): KSirenJsonWriter {
		writer.beginObject()
		return this
	}

	override fun endObject(): KSirenJsonWriter {
		writer.endObject()
		return this
	}

	override fun beginArray(): KSirenJsonWriter {
		writer.beginArray()
		return this
	}

	override fun endArray(): KSirenJsonWriter {
		writer.endArray()
		return this
	}

	override fun close() {
		writer.close()
	}

	override fun name(name: String): KSirenJsonWriter {
		writer.name(name)
		return this
	}

	override fun value(value: String?): KSirenJsonWriter {
		writer.value(value)
		return this
	}

	override fun value(value: Number?): KSirenJsonWriter {
		writer.value(value)
		return this
	}

	override fun value(value: Double?): KSirenJsonWriter {
		writer.value(value)
		return this
	}

	override fun value(value: Boolean?): KSirenJsonWriter {
		writer.value(value)
		return this
	}
}
