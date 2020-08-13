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

interface ActionBase : JsonSerializable {
	val name: String
	val classes: List<String>
	val method: String
	val href: String
	val title: String?
	val type: ContentType
	val fields: List<FieldBase>

	fun hasField(name: String): Boolean = this.fields.any() { it.name == name }

	fun toJsonRequestBody(): String? =
		if (fields.isNotEmpty())
			fields.mapNotNull { field -> field.value?.let { json -> field.name to json } }
				.joinToString(prefix = "{", postfix = "}") { "\"${it.first}\": \"${JsonUtils.escapeJson(it.second)}\"" }
		else null

	override fun toJson() = JsonUtils.toJson(this)
}

data class Action(
	override val name: String,
	override val classes: List<String> = listOf(),
	override val method: String = "GET",
	override val href: String,
	override val title: String?,
	override val type: ContentType = ContentType.FORM,
	override val fields: List<Field> = listOf()) : ActionBase {

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
}

internal data class MutableAction(
	override val name: String,
	override val classes: List<String> = listOf(),
	override val fields: List<MutableField> = listOf(),
	override val href: String,
	override val method: String = "GET",
	override val title: String?,
	override val type: ContentType = ContentType.FORM) : ActionBase {

	constructor(action: ActionBase) : this(
		action.name,
		action.classes,
		action.fields.map { MutableField(it) },
		action.href,
		action.method,
		action.title,
		action.type
	)
}
