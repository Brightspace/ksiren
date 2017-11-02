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
	val entities: MutableList<Entity> = mutableListOf(),
	val rel: List<String> = listOf(),
	val actions: Map<String, Action> = mapOf(),
	val links: List<Link> = listOf(),
	val href: String?,
	val title: String?) : JsonSerializable {

	var isEmbedded: Boolean = false

	inline fun <T:DefinedEntity>getSubEntitiesOfType(client: KSirenEntityFetchClient, factory: (Entity) -> T): List<T> {
		val resultArr: MutableList<T> = mutableListOf()

		for (i in entities.indices) {
			val href: String? = entities[i].href

			if (entities[i].isEmbedded && href != null) {
				entities[i] = Entity.fromJson(client.executeCall(href))
			}

			try {
				resultArr.add(factory(entities[i]))
			} catch (e: KSirenException.ValidationException) {
				//Do, nothing, this sub entity just doesn't match the requested type
			}
		}
		return resultArr
	}

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
				val name = reader.nextName()
				when (name) {
					"class" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							classes.add(reader.nextString())
						}
						reader.endArray()
					}
					"properties" -> {
						reader.beginObject()
						while (reader.hasNext()) {
							properties.put(reader.nextName(), reader.nextString())
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
							val action: Action = Action.Companion.fromJson(reader)
							if (actions.keys.contains(action.name)) {
								throw KSirenException.ValidationException("Entity action names must be unique.")
							}
							actions.put(action.name, action)
						}
						reader.endArray()
					}
					"links" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							links.add(Link.Companion.fromJson(reader))
						}
						reader.endArray()
					}
					"title" -> {
						title = reader.nextString()
					}
					"rel" -> {
						reader.beginArray()
						while (reader.hasNext()) {
							rel.add(reader.nextString())
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
	}

	override fun toJson(): CharSequence {
		return JsonUtils.toJson(this)
	}
}
