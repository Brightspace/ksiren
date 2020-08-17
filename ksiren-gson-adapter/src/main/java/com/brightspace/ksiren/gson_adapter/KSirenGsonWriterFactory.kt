package com.brightspace.ksiren.gson_adapter

import com.brightspace.ksiren.KSirenJsonWriter
import com.brightspace.ksiren.KSirenJsonWriterFactory
import com.google.gson.stream.JsonWriter
import java.io.StringWriter

class KSirenGsonWriterFactory : KSirenJsonWriterFactory {
	override fun create(): KSirenJsonWriter {
		val buffer = StringWriter()
		return KSirenGsonWriter(JsonWriter(buffer)) {
			buffer.toString()
		}
	}
}
