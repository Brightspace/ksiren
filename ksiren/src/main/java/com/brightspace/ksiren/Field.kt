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
class Field(
	val name: String,
	val classes: List<String> = listOf(),
	val type: String = "text",
	val value: String?) {

	companion object {

		fun fromJson(reader: KSirenJsonReader): Field {
			var name: String = ""
			val classes: MutableList<String> = mutableListOf()
			var type: String = "text"
			var value: String? = null

			reader.beginObject()
			while (reader.hasNext()) {
				when (reader.nextName()) {
					"name" -> {
						name = reader.nextString()
					}
					"class" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							classes.add(reader.nextString())
						}
						reader.endArray()
					}
					"type" -> {
						type = reader.nextString()
					}
					"value" -> {
						try {
							value = reader.nextString()
						} catch(e:KSirenException.ParseException) {
							value = reader.nextBoolean()
						}
					}
				}
			}
			reader.endObject()
			val finishedField: Field = Field(name, classes, type, value)
			return validate(finishedField)
		}

		fun validate(obj: Field): Field {
			if (obj.name == "") {
				throw KSirenException.ValidationException("Field parsing failed, missing name value.")
			}
			return obj
		}
	}

	fun toJson(): CharSequence {
		return JsonUtils.toJson(this)
	}
}
