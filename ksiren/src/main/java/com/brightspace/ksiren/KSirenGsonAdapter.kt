package com.brightspace.ksiren

import com.google.gson.stream.JsonReader

/**
 * Created by mpomeroy on 7/31/17.
 */
class KSirenGsonAdapter(val gsonReader: JsonReader): KSirenJsonReader {
	override fun beginObject() {
		gsonReader.beginObject()
	}

	override fun endObject() {
		gsonReader.endObject()
	}

	override fun beginArray() {
		gsonReader.beginArray()
	}

	override fun endArray() {
		gsonReader.endArray()
	}

	override fun hasNext(): Boolean {
		return gsonReader.hasNext()
	}

	override fun nextName(): String {
		return gsonReader.nextName()
	}

	override fun nextString(): String {
		return gsonReader.nextString()
	}
}
