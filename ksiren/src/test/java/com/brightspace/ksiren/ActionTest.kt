package com.brightspace.ksiren

import com.brightspace.ksiren.moshi_adapter.KSirenMoshiWriterFactory
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
class ActionTest {
	/**
	 * A string which has various characters requiring escaping as per https://tools.ietf.org/html/rfc7159#section-7
	 * including: double-quotes, newline, tab, solidus, and several other control characters
	 */
	private val stringRequiringJsonEscape = """"quoted text"
	█ \ / \/ \\ """
	private fun createJsonAction(fields: List<Field> = listOf()) =
		Action(
			"test-action",
			listOf(),
			"POST",
			"https://foo.bar",
			"action-siren-title",
			ContentType.JSON,
			fields)

	@Test
	fun expectAction() {
		val json: String = """{"name": "TestAction","method": "GET","href": "http://api.x.io/orders/42/items","title": "Test this thing","fields": [{"name": "test","type": "number","value": "1"}]}"""

		val action: Action = Action.fromJson(json.toKSirenJsonReader())
		assertEquals("TestAction", action.name)
		assertEquals("GET", action.method)
		assertEquals("http://api.x.io/orders/42/items", action.href)
		assertEquals("Test this thing", action.title)
		assertTrue(action.fields.isNotEmpty())
	}

	@Test
	fun expectActionWithNoFields() {
		val json: String = """{"name": "TestAction","method": "GET","href": "http://api.x.io/orders/42/items","title": "Test this thing"}"""

		val action: Action = Action.fromJson(json.toKSirenJsonReader())
		assertEquals("TestAction", action.name)
		assertEquals("GET", action.method)
		assertEquals("http://api.x.io/orders/42/items", action.href)
		assertEquals("Test this thing", action.title)
		assertEquals("Test this thing", action.title)
	}

	@Test
	fun expectActionWithField() {
		val json: String = """{ "name": "add-item", "title": "Add Item", "method": "GET", "href": "http://api.x.io/orders/42/items", "fields": [{ "name": "orderNumber", "type": "hidden", "value": "42" }, { "name": "productCode", "type": "text" }, { "name": "quantity", "type": "number" }] }"""
		val action: Action = Action.fromJson(json.toKSirenJsonReader())
		assertTrue(action.hasField("orderNumber"))
		assertTrue(action.hasField("productCode"))
		assertTrue(action.hasField("quantity"))
		assertFalse(action.hasField("doesNotExist"))
	}

	@Test
	fun expectEscapedFieldString() {
		val action = createJsonAction(listOf(Field("escapedString", listOf(), "text", stringRequiringJsonEscape)))
		val actionJson = action.toJson(KSirenMoshiWriterFactory().create())
		val parsedAction = Action.fromJson(actionJson.toKSirenJsonReader())

		assertEquals(stringRequiringJsonEscape, parsedAction.fields.find { it.name == "escapedString" }?.value)
	}

	@Test
	fun expectSerializedRequestBody() {
		val action = createJsonAction(listOf(Field("escapedString", listOf(), "text", stringRequiringJsonEscape)))
		val jsonRequestBody = action.toJsonRequestBody(KSirenMoshiWriterFactory().create())
		val expectedBody = """{"escapedString":"\"quoted text\"\n\t█\b\f \\ / \\/ \\\\ "}"""

		assertEquals(expectedBody, jsonRequestBody)
	}
}
