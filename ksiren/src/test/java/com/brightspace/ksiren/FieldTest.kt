package com.brightspace.ksiren

import com.squareup.moshi.JsonEncodingException
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
class FieldTest {
	@Test
	fun expectField() {
		val field: Field = Field.fromJson("""{ "class":["class1", "class2"], "name": "orderNumber", "type": "hidden", "value": "42" }""".toKSirenJsonReader())
		assertEquals("orderNumber", field.name)
		assertEquals("hidden", field.type)
		assertEquals("42", field.value)
		assertTrue(field.classes.contains("class1"))
		assertTrue(field.classes.contains("class2"))
	}

	@Test
	fun expectFieldValueArray() {
		val field: Field = Field.fromJson("""{ "class":["class1", "class2"], "name": "orderNumber", "type": "hidden", "value": "[42]" }""".toKSirenJsonReader())
		assertEquals("orderNumber", field.name)
		assertEquals("hidden", field.type)
		assertEquals("[42]", field.value)
		assertTrue(field.classes.contains("class1"))
		assertTrue(field.classes.contains("class2"))
	}

	@Test
	fun booleanParseTest() {
		val field: Field = Field.fromJson("""{ "name": "expressShipping", "type": "checkbox", "value": false }""".toKSirenJsonReader())
		assertEquals("expressShipping", field.name)
		assertEquals("checkbox", field.type)
		assertEquals("false", field.value)
	}

	@Test
	fun nullParseTest() {
		val field: Field = Field.fromJson("""{ "name": "expressShipping", "type": "checkbox", "value": null }""".toKSirenJsonReader())
		assertEquals("expressShipping", field.name)
		assertEquals("checkbox", field.type)
		assertEquals(null, field.value)
	}

	@Test
	fun expectJsonException() {
		try {
			Field.fromJson("""{ "name": "orderNumber" "type": "hidden", "value": "42" }""".toKSirenJsonReader())
		} catch (ex: JsonEncodingException) {
			assert(true)
		}
	}
}
