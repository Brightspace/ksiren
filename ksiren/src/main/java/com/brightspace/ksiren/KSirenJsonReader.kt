package com.brightspace.ksiren

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
abstract class KSirenJsonReader {

	abstract fun beginObject()
	abstract fun endObject()

	abstract fun beginArray()
	abstract fun endArray()

	abstract fun hasNext(): Boolean
	abstract fun nextName(): String

	fun nextString(): String {
		try {
			return nextStringImpl()
		} catch (e: Exception) {
			//Do nothing, try reading booleans and nulls first
		}

		try {
			return nextBoolean()
		} catch (e: Exception) {
			//Do nothing, try reading nulls first
		}

		try {
			return nextNull()
		} catch(e: Exception) {
			throw KSirenException.ParseException(e.message?:"Could not parse value as String, Boolean or null value.")
		}
	}

	abstract fun nextStringImpl(): String
	abstract protected fun nextBoolean(): String
	abstract protected fun nextNull(): String

}
