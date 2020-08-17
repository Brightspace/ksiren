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

data class Action(
	val name: String,
	val classes: List<String> = listOf(),
	val method: String = "GET",
	val href: String,
	val title: String?,
	val type: ContentType = ContentType.FORM,
	val fields: List<Field> = listOf()) : JsonSerializable {

	companion object {

		fun fromJson(reader: KSirenJsonReader): Action {
			var name = ""
			val classes: MutableList<String> = mutableListOf()
			var method = "GET"
			var href = ""
			var title: String? = null
			var type = ContentType.FORM
			val fields: MutableList<Field> = mutableListOf()

			reader.beginObject()
			while (reader.hasNext()) {
				when (reader.nextName()) {
					"class" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							conditionalRead(reader, { classes.add(it) })
						}
						reader.endArray()
					}
					"name" -> {
						conditionalRead(reader, { name = it })
					}
					"method" -> {
						conditionalRead(reader, { method = it })
					}
					"href" -> {
						conditionalRead(reader, { href = it })
					}
					"title" -> {
						title = reader.nextString()
					}
					"type" -> {
						conditionalRead(reader, { type = ContentType.parse(it) })
					}
					"fields" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							fields.add(Field.fromJson(reader))
						}
						reader.endArray()
					}
				}
			}
			reader.endObject()
			val finishedAction = Action(name, classes, method, href, title, type, fields)
			return validate(finishedAction)
		}

		private fun validate(obj: Action): Action {
			if (obj.name == "" && obj.href == "") {
				throw KSirenException.ValidationException("Validation of action failed, href and name are empty.")
			} else if (obj.name == "") {
				throw KSirenException.ValidationException("Validation of action with href \"".plus(obj.href).plus("\" failed, action is missing name value."))
			} else if (obj.href == "") {
				throw KSirenException.ValidationException("Validation of action with name \"".plus(obj.name).plus("\" failed, action is missing an href."))
			}
			return obj
		}
	}

	fun deepCopy(): Action {
		return Action(
			name,
			classes,
			method,
			href,
			title,
			type,
			fields.map() { field -> field.copy() }
		)
	}

	fun hasField(name: String): Boolean = this.fields.any() { field -> field.name == name }

	override fun toJson(writer: KSirenJsonWriter): String = JsonUtils.toJson(this, writer)

	fun toJsonRequestBody(writer: KSirenJsonWriter): String {
		writer.beginObject()

		fields?.forEach() { field ->
			writer.name(field.name)
			writer.value(field.value)
		}

		writer.endObject()
		writer.close()

		return writer.getSerializedString()
	}
}
