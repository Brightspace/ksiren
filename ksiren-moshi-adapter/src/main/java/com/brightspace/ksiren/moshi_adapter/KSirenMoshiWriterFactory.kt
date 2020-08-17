package com.brightspace.ksiren.moshi_adapter

import com.brightspace.ksiren.KSirenJsonWriter
import com.brightspace.ksiren.KSirenJsonWriterFactory
import com.squareup.moshi.JsonWriter
import okio.Buffer

class KSirenMoshiWriterFactory : KSirenJsonWriterFactory {
	override fun create(): KSirenJsonWriter {
		val buffer = Buffer()
		return KSirenMoshiWriter(JsonWriter.of(buffer)) {
			buffer.readUtf8()
		}
	}
}
