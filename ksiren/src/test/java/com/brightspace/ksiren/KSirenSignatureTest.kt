package com.brightspace.ksiren

import org.junit.Test
import kotlin.test.assertFails
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
class KSirenSignatureTest {
	@Test
	fun testValidate() {
		val signature: KSirenEntitySignature =
			KSirenEntitySignature(
				listOf("order"),
				listOf(),
				listOf("self"),
				listOf("orderNumber", "itemCount", "status"))

		val json: String = """{ "class": [ "order" ], "properties": { "orderNumber": 42, "itemCount": 3, "status": "pending" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/orders/42" }] }"""
		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		signature.validate(entity)

		//assuring the above doesn't throw an exception is good enough
		assertTrue(true)

	}

	@Test
	fun testFailedValidate() {
		val signature: KSirenEntitySignature =
			KSirenEntitySignature(
				listOf("order"),
				listOf(),
				listOf("self"),
				listOf("orderNumber", "itemCount", "status", "anotherProperty"))

		val json: String = """{ "class": [ "order" ], "properties": { "orderNumber": 42, "itemCount": 3, "status": "pending" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/orders/42" }] }"""

		val entity: Entity = Entity.fromJson(json.toKSirenJsonReader())
		assertFails { signature.validate(entity) }
	}
}
