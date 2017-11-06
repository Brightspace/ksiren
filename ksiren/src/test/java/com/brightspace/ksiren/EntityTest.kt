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
		assertTrue(entity.properties.size == 3)
		assertEquals(listOf("order"), entity.classes)
	}

    @Test
    fun serializeToJson() {
        // the parser cannot handle linked entities
        // { "class": [ "items", "collection" ], "rel": [ "http://x.io/rels/order-items" ], "href": "http://api.x.io/orders/42/items"},
        val json = """{ "class": [ "order" ], "properties": { "orderNumber": "42", "itemCount": "3", "status": "pending" }, "entities": [{ "class": [ "info", "customer" ], "rel": [ "http://x.io/rels/customer" ], "properties": { "customerId": "pj123", "name": "Peter Joseph" }, "links": [{ "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }] }], "actions": [{ "name": "add-item", "title": "Add Item", "method": "POST", "href": "http://api.x.io/orders/42/items", "type": "application/x-www-form-urlencoded", "fields": [{ "name": "orderNumber", "type": "hidden", "value": "42" }, { "name": "productCode", "type": "text" }, { "name": "quantity", "type": "number" }] }], "links": [{ "rel": [ "self" ], "href": "http://api.x.io/orders/42" }, { "rel": [ "previous" ], "href": "http://api.x.io/orders/41" }, { "rel": [ "next" ], "href": "http://api.x.io/orders/43" }] }"""
        val entity = Entity.fromJson(json.toKSirenJsonReader())

        // order of properties matters...
        assertEquals(json, entity.toJson())
    }
}
