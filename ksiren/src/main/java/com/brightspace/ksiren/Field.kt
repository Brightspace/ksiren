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
interface FieldBase : JsonSerializable {
	val name: String
	val classes: List<String>
	val type: String
	val value: String?

	override fun toJson() = JsonUtils.toJson(this)
}

data class Field(
	override val name: String,
	override val classes: List<String> = listOf(),
	override val type: String = "text",
	override val value: String?) : FieldBase {

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
						conditionalRead(reader) { name = it }
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
						value = tryParseWithLambdasAsString(reader,
							{ it.nextString() },
							{ it.nextBoolean().toString() },
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
}

internal class MutableField(private val field: FieldBase) : FieldBase by field {
	override var value: String? = field.value
}
