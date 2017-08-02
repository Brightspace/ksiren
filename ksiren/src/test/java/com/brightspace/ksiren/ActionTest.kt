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
class ActionTest {
    @Test
    fun expectAction() {
        val json: String = """{"name": "TestAction","method": "GET","href": "http://api.x.io/orders/42/items","title": "Test this thing","fields": [{"name": "test","type": "number","value": "1"}]}"""

        val action: Action = Action.fromJson(json.toKSirenJsonReader())
        assertEquals("TestAction", action.name)
        assertEquals("GET", action.method)
        assertEquals("http://api.x.io/orders/42/items", action.href)
        assertEquals("Test this thing", action.title)
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
    }

    @Test
    fun serializeToJson() {
        val json: String = """{ "name": "add-item", "title": "Add Item", "method": "POST", "href": "http://api.x.io/orders/42/items", "type": "application/x-www-form-urlencoded", "fields": [{ "name": "orderNumber", "type": "hidden", "value": "42" }, { "name": "productCode", "type": "text" }, { "name": "quantity", "type": "number" }] }"""
        val action = Action.fromJson(json.toKSirenJsonReader())

        // order of properties matters...
        assertEquals(json, action.toJson())
    }
}
