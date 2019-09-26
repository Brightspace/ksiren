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
internal object JsonUtils {
	fun toJson(entity: Entity) = listOf(
			"class" to entity.classes.toJson(),
			"rel" to entity.rel.toJson(),
			"properties" to entity.enhancedProperties.toJson(),
			"entities" to entity.entities.toJson(),
			"actions" to entity.actions.values.toJson(),
			"links" to entity.links.toJson()
		)
		.toJsonObject()

	fun toJson(link: Link) = listOf(
			"rel" to link.rels.toJson(),
			"class" to link.classes.toJson(),
			"title" to link.title.toJson(),
			"type" to link.type.toJson(),
			"href" to link.href.toJson()
		).toJsonObject()

	fun toJson(action: Action) = listOf(
			"name" to action.name.toJson(),
			"title" to action.title.toJson(),
			"class" to action.classes.toJson(),
			"method" to action.method.toJson(),
			"href" to action.href.toJson(),
			"type" to action.type.value.toJson(),
			"fields" to action.fields.toJson()
		).toJsonObject()

	fun toJson(field: Field) = listOf(
			"name" to field.name.toJson(),
			"class" to field.classes.toJson(),
			"type" to field.type.toJson(),
			"value" to field.value.toJson()
		).toJsonObject()

	fun toJson(propertyValue: PropertyValue): String? = when(propertyValue) {
		is StringValue -> propertyValue.stringValue.toJson()
		is BooleanValue -> propertyValue.booleanValue.toString()
		is ArrayValue -> propertyValue.arrayElements.toJson()
		is ObjectValue -> propertyValue.objectProperties.toJson()
	}

	private fun String?.toJson() = if (this != null && isNotEmpty()) "\"$this\"" else null

	private fun List<String>.toJson() = toJsonArray { "\"$it\"" }

	private fun Collection<JsonSerializable>.toJson() = mapNotNull { it.toJson() }.toJsonArray()

	private fun List<String>.toJsonArray(transform: (String) -> String = { it })
		= if (isNotEmpty()) joinToString(prefix = "[", postfix = "]", transform = transform) else null

	private fun Map<String, PropertyValue>.toJson(): String? =
		mapValues { entry -> entry.value.toJson() }
			.entries
			.map { entry -> entry.toPair() }
			.toJsonObject()

	private fun List<Pair<String, String?>>.toJsonObject() =
		if (isNotEmpty())
			mapNotNull { pair -> pair.second?.let { json -> pair.first to json } }
				.joinToString(prefix = "{", postfix = "}") { "\"${it.first}\": ${it.second}" }
		else null
}

interface JsonSerializable {
	fun toJson(): String?
}
