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
object JsonUtils {
	fun toJson(entity: Entity): CharSequence {
		val builder = StringBuilder()
		builder.append("{ ")

		addArray("class", entity.classes, false, builder)

		addArray("rel", entity.rel, true, builder)

		if (!entity.properties.isEmpty()) {
			builder.append(", \"properties\": { ")

			var count = 0
			entity.properties.forEach { (name, value) ->
				addProperty(name, value, count > 0, builder)
				count += 1
			}

			builder.append(" }")
		}

		addJsonSerializable("entities", entity.entities, builder)
		addJsonSerializable("actions", entity.actions.values, builder)
		addJsonSerializable("links", entity.links, builder)

		builder.append(" }")

		return builder.toString()
	}

	fun toJson(link: Link): CharSequence {
		val builder = StringBuilder()
		builder.append("{ ")

		addArray("rel", link.rels, false, builder)
		addArray("class", link.classes, true, builder)
		addProperty("title", link.title, true, builder)
		addProperty("type", link.type, true, builder)
		addProperty("href", link.href, true, builder)
		builder.append(" }")

		return builder.toString()
	}

	fun toJson(action: Action): CharSequence {
		val builder = StringBuilder()
		builder.append("{ ")

		addProperty("name", action.name, false, builder)
		addProperty("title", action.title, true, builder)
		addArray("classes", action.classes, true, builder)
		addProperty("method", action.method, true, builder)
		addProperty("href", action.href, true, builder)
		addProperty("type", action.type.value, true, builder)

		if (!action.fields.isEmpty()) {
			builder.append(", \"fields\": [")
			action.fields.forEachIndexed { index, field ->
				if (index > 0) {
					builder.append(", ")
				}

				builder.append(toJson(field))
			}
			builder.append("]")
		}

		builder.append(" }")

		return builder.toString()
	}

	fun toJson(field: Field): CharSequence {
		val builder = StringBuilder()
		builder.append("{ ")

		addProperty("name", field.name, false, builder)
		addArray("class", field.classes, true, builder)
		addProperty("type", field.type, true, builder)
		addProperty("value", field.value, true, builder)

		builder.append(" }")
		return builder.toString()
	}

	private fun addProperty(name: String, value: String?, prependComma: Boolean, builder: StringBuilder) {
		value?.let {
			if (prependComma) {
				builder.append(", \"")
			} else {
				builder.append("\"")
			}

			builder.append(name)
			builder.append("\": \"")
			builder.append(it)
			builder.append("\"")
		}
	}

	fun addArray(name: String, array: List<String>, prependComma: Boolean, builder: StringBuilder) {
		if (array.isEmpty()) {
			return
		}

		if (prependComma) {
			builder.append(", \"")
		} else {
			builder.append("\"")
		}

		builder.append(name)
		builder.append("\": [ ")
		array.forEachIndexed { index, value ->
			if (index > 0) {
				builder.append(", ")
			}

			builder.append("\"")
			builder.append(value)
			builder.append("\"")
		}
		builder.append(" ]")
	}

	private fun addJsonSerializable(name: String, items: Collection<JsonSerializable>, builder: StringBuilder) {
		if (!items.isEmpty()) {
			builder.append(", \"").append(name).append("\": [")

			items.forEachIndexed { index, item ->
				if (index > 0) {
					builder.append(", ")
				}

				builder.append(item.toJson())
			}

			builder.append("]")
		}
	}
}

interface JsonSerializable {
	fun toJson(): CharSequence
}
