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
class Entity(
	val classes: List<String> = listOf(),
	val properties: Map<String, String> = mapOf(),
	val entities: List<Entity> = listOf(),
	val rel: List<String> = listOf(),
	val actions: Map<String, Action> = mapOf(),
	val links: List<Link> = listOf(),
	val href: String?,
	val title: String?) : JsonSerializable {

	companion object {

		fun fromJson(reader: KSirenJsonReader): Entity {
			val classes: MutableList<String> = mutableListOf()
			val properties: MutableMap<String, String> = mutableMapOf()
			val entities: MutableList<Entity> = mutableListOf()
			val rel: MutableList<String> = mutableListOf()
			val actions: MutableMap<String, Action> = mutableMapOf()
			val links: MutableList<Link> = mutableListOf()
			var href: String? = null
			var title: String? = null

			reader.beginObject()
			while (reader.hasNext()) {
				when (reader.nextName()) {
					"class" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							conditionalRead(reader) {classes.add(it)}
						}
						reader.endArray()
					}
					"properties" -> {
						reader.beginObject()
						while (reader.hasNext()) {
							val propName = reader.nextName()
							tryParsePropertyValue(reader)?.let { value ->
								properties[propName] = value
							}
						}
						reader.endObject()
					}
					"entities" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							entities.add(fromJson(reader))
						}
						reader.endArray()
					}
					"actions" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							val action: Action = Action.fromJson(reader)
							if (actions.keys.contains(action.name)) {
								throw KSirenException.ValidationException("Entity action names must be unique.")
							}
							actions[action.name] = action
						}
						reader.endArray()
					}
					"links" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							links.add(Link.fromJson(reader))
						}
						reader.endArray()
					}
					"title" -> {
						title = reader.nextString()
					}
					"rel" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							conditionalRead(reader, {rel.add(it)})
						}
						reader.endArray()
					}
					"href" -> {
						href = reader.nextString()
					}
				}
			}
			reader.endObject()
			return Entity(classes, properties, entities, rel, actions, links, href, title)
		}

		private fun ignoreArray(reader: KSirenJsonReader): String? {
			reader.beginArray()
			while (reader.hasNext()) {
				tryParsePropertyValue(reader)
			}
			reader.endArray()
			return null
		}

		private fun ignoreObject(reader: KSirenJsonReader): String? {
			reader.beginObject()
			while (reader.hasNext()) {
				reader.nextName()
				tryParsePropertyValue(reader)
			}
			reader.endObject()
			return null
		}

		private fun tryParsePropertyValue(reader: KSirenJsonReader): String? {
			return tryParseWithLambdasAsString(reader, { it.nextString() }, { it.nextBoolean() }, ::ignoreArray, ::ignoreObject)
		}
	}

	override fun toJson(): CharSequence {
		return JsonUtils.toJson(this)
	}
}
