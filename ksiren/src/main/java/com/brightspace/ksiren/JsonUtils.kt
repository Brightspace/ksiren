package com.brightspace.ksiren

import java.lang.Exception

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
internal object JsonUtils {
	fun toJson(entity: Entity, writer: KSirenJsonWriter): String {
		writeEntity(entity, writer)
		writer.close()

		return writer.getSerializedString()
	}

	fun toJson(action: Action, writer: KSirenJsonWriter): String {
		writeAction(action, writer)
		writer.close()

		return writer.getSerializedString()
	}

	private fun writeProperties(properties: Map<String, PropertyValue>, writer: KSirenJsonWriter) {
		writer.beginObject()
		properties.forEach() { (name, value) ->
			writer.name(name)
			writePropertyValue(value, writer)
		}
		writer.endObject()
	}

	private fun writePropertyValue(value: PropertyValue, writer: KSirenJsonWriter) {
		when (value) {
			is StringValue -> writer.value(value.stringValue)
			is BooleanValue -> writer.value(value.booleanValue)
			is ArrayValue -> {
				writer.beginArray()
				value.arrayElements.forEach() { el -> writePropertyValue(el, writer) }
				writer.endArray()
			}
			is ObjectValue -> writeProperties(value.objectProperties, writer)
		}
	}

	private fun writeEntity(entity: Entity, writer: KSirenJsonWriter) {
		writer.beginObject()

		entity.classes?.let { classes ->
			if (classes.isNotEmpty()) {
				writer.name("class")
				writeStringArray(classes, writer)
			}
		}

		entity.rel?.let { rels ->
			if (rels.isNotEmpty()) {
				writer.name("rel")
				writeStringArray(rels, writer)
			}
		}

		entity.href?.let {
			writer.name("href")
				.value(entity.href)
		}

		entity.enhancedProperties?.let {
			writer.name("properties")
			writeProperties(entity.enhancedProperties, writer)
		}

		entity.entities?.let { subEntities ->
			if (subEntities.isNotEmpty()) {
				writer.name("entities")
				writer.beginArray()
				subEntities.forEach() { entity -> writeEntity(entity, writer) }
				writer.endArray()
			}
		}

		entity.actions?.let { actions ->
			if (actions.isNotEmpty()) {
				writer.name("actions")
				writer.beginArray()
				actions.forEach() { (_, action) ->	writeAction(action, writer)	}
				writer.endArray()
			}
		}

		entity.links?.let { links ->
			if (links.isNotEmpty()) {
				writer.name("links")
				writeLinks(entity.links, writer)
			}
		}

		writer.endObject()
	}

	private fun writeLinks(links: Collection<Link>, writer: KSirenJsonWriter) {
		writer.beginArray()

		links.forEach() { link ->
			if (link.href == null) throw Exception("link must have href")
			if (link.rels.isEmpty()) throw Exception("link must have at least one rel")

			writer.beginObject()

			link.title?.let { title ->
				writer.name("title")
					.value(title)
			}

			link.classes?.let { classes ->
				writer.name("class")
				writeStringArray(classes, writer)
			}

			writer.name("rel")
			writeStringArray(link.rels, writer)

			writer.name("href")
				.value(link.href)

			writer.name("type")
				.value(link.type)

			writer.endObject()
		}

		writer.endArray()
	}

	private fun writeAction(action: Action, writer: KSirenJsonWriter) {
		writer.beginObject()

		writer.name("name")
			.value(action.name)

		action.classes?.let { classes ->
			writer.name("class")
			writeStringArray(classes, writer)
		}

		action.title?.let { title ->
			writer.name("title")
				.value(title)
		}

		writer.name("type")
			.value(action.type.value)

		writer.name("href")
			.value(action.href)

		writer.name("method")
			.value(action.method)

		action.fields?.let { fields ->
			if (fields.isEmpty())
				return

			writer.name("fields")

			writer.beginArray()
			fields.mapNotNull { field ->
				writer.beginObject()

				writer.name("name")
				writer.value(field.name)

				field.classes?.let { classes ->
					writer.name("class")
					writeStringArray(classes, writer)
				}

				writer.name("type")
				writer.value(field.type)

				field.value?.let {
					writer.name("value")
					writer.value(field.value)
				}

				writer.endObject()
			}
			writer.endArray()
		}

		writer.endObject()
	}

	private fun writeStringArray(values: List<String>, writer: KSirenJsonWriter) {
		writer.beginArray()
		values.forEach() { value -> writer.value(value) }
		writer.endArray()
	}
}

interface JsonSerializable {
	fun toJson(writer: KSirenJsonWriter): String
}
