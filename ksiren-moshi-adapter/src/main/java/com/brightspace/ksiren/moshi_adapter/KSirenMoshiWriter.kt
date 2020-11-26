package com.brightspace.ksiren.moshi_adapter

import com.brightspace.ksiren.KSirenJsonWriter
import com.squareup.moshi.JsonWriter
import okio.Buffer
import java.io.Closeable

class KSirenMoshiWriter : KSirenJsonWriter, Closeable {
	private val buffer = Buffer()
	private val moshiWriter = JsonWriter.of(buffer).apply {
		serializeNulls = false
	}

	override fun getSerializedString(): String = buffer.readUtf8()

	override fun beginObject(): KSirenJsonWriter = apply { moshiWriter.beginObject() }
	override fun endObject(): KSirenJsonWriter = apply { moshiWriter.endObject() }

	override fun beginArray(): KSirenJsonWriter = apply { moshiWriter.beginArray() }
	override fun endArray(): KSirenJsonWriter = apply { moshiWriter.endArray() }

	override fun close() {
		moshiWriter.close()
	}

	override fun name(name: String): KSirenJsonWriter = apply { moshiWriter.name(name) }

	override fun value(value: String?): KSirenJsonWriter = apply { moshiWriter.value(value) }
	override fun value(value: Number?): KSirenJsonWriter = apply { moshiWriter.value(value) }
	override fun value(value: Double?): KSirenJsonWriter = apply { moshiWriter.value(value) }
	override fun value(value: Boolean?): KSirenJsonWriter = apply { moshiWriter.value(value) }
}
