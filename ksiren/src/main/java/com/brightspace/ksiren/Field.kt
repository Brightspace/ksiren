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
			var name = ""
			val classes: MutableList<String> = mutableListOf()
			var type = "text"
			var value: String? = null

			reader.beginObject()
			while (reader.hasNext()) {
				when (reader.nextName()) {
					"name" -> {
						conditionalRead(reader) {name = it}
					}
					"class" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							conditionalRead(reader) { classes.add(it) }
						}
						reader.endArray()
					}
					"type" -> {
						conditionalRead(reader) { type = it }
					}
					"value" -> {
						value = tryExecuteParsingLambdas(reader,
							{ it.nextString() },
							{ readAndReserializeArray(it) })

					}
				}
			}
			reader.endObject()
			val finishedField = Field(name, classes, type, value)
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
