package com.brightspace.ksiren

import com.squareup.moshi.JsonReader

/**
 * Created by mpomeroy on 7/31/17.
 */
class KSirenMoshiAdapter(val moshiReader: JsonReader): KSirenJsonReader {
	override fun beginObject() {
		moshiReader.beginObject()
	}

	override fun endObject() {
		moshiReader.endObject()
	}

	override fun beginArray() {
		moshiReader.beginArray()
	}

	override fun endArray() {
		moshiReader.endArray()
	}

	override fun hasNext(): Boolean {
		return moshiReader.hasNext()
	}

	override fun nextName(): String {
		return moshiReader.nextName()
	}

	override fun nextString(): String {
		return moshiReader.nextString()
	}
}
