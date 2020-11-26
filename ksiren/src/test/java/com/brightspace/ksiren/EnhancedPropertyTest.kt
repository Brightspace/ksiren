package com.brightspace.ksiren

import org.junit.Test
import kotlin.test.assertEquals

class EnhancedPropertyTest {
	@Test
	fun testStringProperty() {
		val stringName = "stringProperty"
		val stringValue = "the value of a string property"
		val json = """{"properties": { "$stringName": "$stringValue" } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(stringName to StringValue(stringValue)))
	}

	@Test
	fun testBooleanProperty() {
		val trueName = "trueProperty"
		val falseName = "falseProperty"
		val json = """{"properties": { "$trueName": true, "$falseName": false } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(trueName to BooleanValue.TRUE, falseName to BooleanValue.FALSE))
	}

	@Test
	fun testArrayProperty() {
		val arrayName = "arrayProperty"
		val arrayElements = listOf("a", "b", "c").map(::StringValue)
		val arrayElementsJson = arrayElements.joinToString { "\"${it.stringValue}\"" }
		val json = """{"properties": { "$arrayName": [$arrayElementsJson] } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(arrayName to ArrayValue(arrayElements)))
	}

	@Test
	fun testObjectProperty() {
		val objectName = "objectProperty"
		val objectProperties = mapOf("a" to "foo", "b" to "bar", "c" to "baz").mapValues { StringValue(it.value) }
		val objectJson = objectProperties.entries.joinToString { "\"${it.key}\": \"${it.value.stringValue}\"" }
		val json = """{"properties": { "$objectName": {$objectJson} } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(objectName to ObjectValue(objectProperties)))
	}

	@Test
	fun testBooleanInArrayProperty() {
		val arrayName = "arrayProperty"
		val nestedArrayElements = listOf(true, false).map { BooleanValue.from(it) }
		val nestedArrayElementsJson = nestedArrayElements.joinToString { it.booleanValue.toString() }
		val json = """{"properties": { "$arrayName": [$nestedArrayElementsJson] } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(arrayName to ArrayValue(nestedArrayElements)))
	}

	@Test
	fun testArrayInArrayProperty() {
		val arrayName = "arrayProperty"
		val nestedArrayElements = listOf("a", "b", "c").map(::StringValue)
		val nestedArrayElementsJson = nestedArrayElements.joinToString { "\"${it.stringValue}\"" }
		val json = """{"properties": { "$arrayName": [[$nestedArrayElementsJson]] } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(arrayName to ArrayValue(listOf(ArrayValue(nestedArrayElements)))))
	}

	@Test
	fun testObjectInArrayProperty() {
		val arrayName = "arrayProperty"
		val nestedObjectProperties = mapOf("a" to "foo", "b" to "bar", "c" to "baz").mapValues { StringValue(it.value) }
		val nestedObjectJson = nestedObjectProperties.entries.joinToString { "\"${it.key}\": \"${it.value.stringValue}\"" }
		val json = """{"properties": { "$arrayName": [{$nestedObjectJson}] } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(arrayName to ArrayValue(listOf(ObjectValue(nestedObjectProperties)))))
	}

	@Test
	fun testBooleanInObjectProperty() {
		val objectName = "objectProperty"
		val nestedObjectProperties = mapOf("trueProperty" to BooleanValue.TRUE, "falseProperty" to BooleanValue.FALSE)
		val nestedObjectJson = nestedObjectProperties.entries.joinToString { "\"${it.key}\": ${it.value.booleanValue}" }
		val json = """{"properties": { "$objectName": {$nestedObjectJson} } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(objectName to ObjectValue(nestedObjectProperties)))
	}

	@Test
	fun testArrayInObjectProperty() {
		val objectName = "objectProperty"
		val nestedArrayName = "nestedArrayProperty"
		val nestedArrayElements = listOf("a", "b", "c").map(::StringValue)
		val nestedArrayElementsJson = nestedArrayElements.joinToString { "\"${it.stringValue}\"" }
		val json = """{"properties": { "$objectName": {"$nestedArrayName": [$nestedArrayElementsJson] } } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(objectName to ObjectValue(mapOf(nestedArrayName to ArrayValue(nestedArrayElements)))))
	}

	@Test
	fun testObjectInObjectProperty() {
		val objectName = "objectProperty"
		val nestedObjectName = "nestedObjectProperty"
		val nestedObjectProperties = mapOf("a" to "foo", "b" to "bar", "c" to "baz").mapValues { StringValue(it.value) }
		val nestedObjectJson = nestedObjectProperties.entries.joinToString { "\"${it.key}\": \"${it.value.stringValue}\"" }
		val json = """{"properties": { "$objectName": {"$nestedObjectName": {$nestedObjectJson} } } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.enhancedProperties,
			expected = mapOf(objectName to ObjectValue(mapOf(nestedObjectName to ObjectValue(nestedObjectProperties)))))
	}
}
