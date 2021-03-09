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
data class Entity internal constructor(
	val classes: List<String> = listOf(),
	val enhancedProperties: Map<String, PropertyValue> = mapOf(),
	val entities: List<Entity> = listOf(),
	val rel: List<String> = listOf(),
	val actions: Map<String, Action> = mapOf(),
	val links: List<Link> = listOf(),
	val href: String?,
	val title: String?) : JsonSerializable {

	// For on-demand backwards-compatibility
	val properties: Map<String, String> by lazy {
		enhancedProperties
			.mapNotNull { entry ->
				entry.value.let { propertyValue ->
					when (propertyValue) {
						is StringValue -> entry.key to propertyValue.stringValue
						is BooleanValue -> entry.key to propertyValue.booleanValue.toString()
						else -> null
					}
				}
			}
			.toMap()
	}

	companion object {

		fun fromJson(reader: KSirenJsonReader): Entity {
			val classes: MutableList<String> = mutableListOf()
			val enhancedProperties: MutableMap<String, PropertyValue> = mutableMapOf()
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
							conditionalRead(reader) { classes.add(it) }
						}
						reader.endArray()
					}
					"properties" -> {
						reader.beginObject()
						while (reader.hasNext()) {
							val propName = reader.nextName()
							parseEnhancedPropertyValue(reader)?.let { value ->
								enhancedProperties[propName] = value
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
							conditionalRead(reader) { rel.add(it) }
						}
						reader.endArray()
					}
					"href" -> {
						href = reader.nextString()
					}
					else -> {
						reader.skipValue()
					}
				}
			}
			reader.endObject()

			return Entity(classes, enhancedProperties, entities, rel, actions, links, href, title)
		}

		private fun parseEnhancedPropertyValue(reader: KSirenJsonReader): PropertyValue? {
			return tryParseWithLambdasAsPropertyValue(reader, ::parseString, ::parseBoolean, ::parseArray, ::parseObject)
		}

		private fun tryParseWithLambdasAsPropertyValue(reader: KSirenJsonReader, vararg parsingLambdas: (KSirenJsonReader) -> PropertyValue?): PropertyValue? {
			for (parser in parsingLambdas) {
				try {
					return parser.invoke(reader)
				} catch (e: Exception) {
					//this mapper failed
				}
			}
			throw KSirenException.ParseException("Could not parse PropertyValue")
		}

		private fun parseString(reader: KSirenJsonReader): StringValue? = reader.nextString()?.let { StringValue(it) }

		private fun parseBoolean(reader: KSirenJsonReader) = BooleanValue.from(reader.nextBoolean())

		private fun parseArray(reader: KSirenJsonReader): ArrayValue {
			val elements = mutableListOf<PropertyValue>()
			reader.beginArray()
			while (reader.hasNext()) {
				parseEnhancedPropertyValue(reader)?.let { value -> elements.add(value) }
			}
			reader.endArray()
			return ArrayValue(elements)
		}

		private fun parseObject(reader: KSirenJsonReader): ObjectValue {
			val values = mutableMapOf<String, PropertyValue>()
			reader.beginObject()
			while (reader.hasNext()) {
				val name = reader.nextName()
				parseEnhancedPropertyValue(reader)?.let { value -> values[name] = value }
			}
			reader.endObject()
			return ObjectValue(values)
		}
	}

	override fun toJson(writer: KSirenJsonWriter) = JsonUtils.toJson(this, writer)
}
