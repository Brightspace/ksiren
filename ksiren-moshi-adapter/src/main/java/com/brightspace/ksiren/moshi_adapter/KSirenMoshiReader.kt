package com.brightspace.ksiren.moshi_adapter

import com.brightspace.ksiren.KSirenException
import com.brightspace.ksiren.KSirenJsonReader
import com.squareup.moshi.JsonReader

/**
 * Copyright 2017 D2L Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class KSirenMoshiReader(val moshiReader: JsonReader) : KSirenJsonReader() {
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

	override fun nextStringImpl(): String {
		try {
			return moshiReader.nextString()
		} catch( e: Exception) {
			throw KSirenException.ParseException(e.message?:"Reading string failed")
		}
	}

	override fun nextBoolean(): Boolean {
		return moshiReader.nextBoolean()
	}

	override fun nextNull() {
		moshiReader.nextNull<String>()
	}
}
