package com.brightspace.ksiren

import org.junit.Test
import kotlin.test.assertEquals

class ToJsonTest {
	@Test
	fun testClassesToJson() {
		val classes = listOf("class a", "class b")
		val entity = Entity(classes = classes, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.classes,
			expected = classes)
	}

	@Test
	fun testRelToJson() {
		val rel = listOf("rel a", "rel b")
		val entity = Entity(rel = rel, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.rel,
			expected = rel)
	}

	@Test
	fun testPropertiesToJson() {
		fun basicProperties(prefix: String = ""): Array<Pair<String, PropertyValue>> {
			return arrayOf(
				"${prefix}StringProperty" to StringValue("stringValue"),
				"${prefix}TrueProperty" to BooleanValue.TRUE,
				"${prefix}FalseProperty" to BooleanValue.FALSE)
		}
		fun basicValues() = basicProperties().map { it.second }
		val properties = mapOf(
			*basicProperties(),
			"ObjectProperty" to ObjectValue(mapOf(
				*basicProperties("Nested"),
				"NestedObjectProperty" to ObjectValue(mapOf(*basicProperties("DoubleNested"))),
				"NestedArrayProperty" to ArrayValue(basicValues()))),
			"ArrayProperty" to ArrayValue(listOf(
				*basicValues().toTypedArray(),
				ObjectValue(mapOf(*basicProperties("Nested"))),
				ArrayValue(basicValues()))))
		val entity = Entity(enhancedProperties = properties, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.enhancedProperties,
			expected = properties)
	}

	@Test
	fun testEntitiesToJson() {
		val subEntities = listOf(
			Entity(classes = listOf("sub-entity"), href = null, title = null)
		)
		val entity = Entity(entities = subEntities, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.entities,
			expected = subEntities)
	}

	@Test
	fun testActionsToJson() {
		// TODO: Array
		val actions = (1..ContentType.values().size).map { a ->
			"action name $a" to Action(
				name = "action name $a",
				classes = listOf("action class $a a", "action class $a b"),
				method = "action method $a",
				href = "action href $a",
				title = "action title $a",
				type = ContentType.values()[a - 1],
				fields = (1..2).map { f ->
					Field(
						name = "field name $a-$f",
						classes = listOf("field class $a-$f a", "field class $a-$f b"),
						type = "field type $a-$f",
						value = "field value $a-$f")
				}
			)
		}.toMap()
		val entity = Entity(actions = actions, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.actions,
			expected = actions)
	}

	@Test
	fun testLinksToJson() {
		val links = (1..2).map { i ->
			Link(
				rels = listOf("rel $i a", "rel $i b"),
				classes = listOf("link class $i a", "link class $i b"),
				href = "link href $i",
				title = "link title $i",
				type = "link type $i")
		}
		val entity = Entity(links = links, href = null, title = null)
		val json = entity.toJson().toString()
		val entityFromJson = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entityFromJson.links,
			expected = links)
	}
}
