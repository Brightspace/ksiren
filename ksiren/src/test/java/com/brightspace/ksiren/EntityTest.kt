package com.brightspace.ksiren

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
class EntityTest {
	@Test
	fun expectEntity() {
		val json = """{ "class": [ "order" ], "properties": { "orderNumber": "42", "itemCount": "3", "status": "pending" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }] }"""

		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		assertTrue(entity.actions.isEmpty())
		assertTrue(entity.links.isNotEmpty())
		assertTrue(entity.properties.size == 3)
		assertEquals(listOf("order"), entity.classes)
	}

	@Test
	fun expectHandledNulls() {
		val json = """{ "class": [ "order" ], "properties": { "orderNumber": null, "itemCount": "3", "status": null }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }] }"""

		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		assertTrue(entity.actions.isEmpty())
		assertTrue(entity.links.isNotEmpty())
		assertTrue(entity.properties.size == 1)
		assertEquals(listOf("order"), entity.classes)
	}

	@Test
	fun expectIgnoreArrayProperties() = expectIgnoreProperty("""["a", "b", "c"]""")

	@Test
	fun expectIgnoreObjectProperties() = expectIgnoreProperty("""{"foo": "bar", "fizz": "buzz"}""")

	@Test
	fun expectIgnoreArrayInArrayProperty() = expectIgnoreProperty("""["a", ["foo", "bar"], "c"]""")

	@Test
	fun expectIgnoreObjectInArrayProperty() = expectIgnoreProperty("""["a", {"foo": "bar"}, "c"]""")

	@Test
	fun expectIgnoreArrayInObjectProperty() = expectIgnoreProperty("""{"foo": "bar", "baz": ["bing", "fling"], "fizz": "buzz"}""")

	@Test
	fun expectIgnoreObjectInObjectProperty() = expectIgnoreProperty("""{"foo": "bar", "baz": {"bing": "fling"}, "fizz": "buzz"}""")

	fun expectIgnoreProperty(ignoredPropertyJson: String) {
		val json = """{ "class": [ "order" ], "properties": { "orderNumber": "42", "ignoredProperty": """ + ignoredPropertyJson + """, "status": "pending" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }] }"""
		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(mapOf("orderNumber" to "42", "status" to "pending"), entity.properties)
		assertEquals(listOf("order"), entity.classes)
		assertTrue(entity.actions.isEmpty())
		assertTrue(entity.links.isNotEmpty())
	}

	@Test
	fun expectHandleBooleanAsAsString() {
		val truePropertyName = "trueProperty"
		val falsePropertyName = "falseProperty"
		val json = """{ "properties": { "$truePropertyName": true, "$falsePropertyName": false } }"""
		val entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(
			actual = entity.properties,
			expected = mapOf(truePropertyName to "true", falsePropertyName to "false"))
	}

	@Test
	fun expectHandleNumber() {
		val json = """{ "class": [ "order" ], "properties": { "orderNumber": 42, "status": "pending" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }] }"""
		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		assertEquals(mapOf("orderNumber" to "42", "status" to "pending"), entity.properties)
		assertEquals(listOf("order"), entity.classes)
		assertTrue(entity.actions.isEmpty())
		assertTrue(entity.links.isNotEmpty())
	}
}
