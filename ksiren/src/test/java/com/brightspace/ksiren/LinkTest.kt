package com.brightspace.ksiren

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
class LinkTest {
    @Test
    fun expectLink() {
        val json: String = """{ "rel": [ "self" ], "class": [ "link" ], "title": "my link", "type": "text/plain", "href": "http://api.x.io/orders/42" }"""
        val link: Link = Link.fromJson(json.toKSirenJsonReader())

        assertEquals(listOf("self"), link.rels)
        assertEquals("http://api.x.io/orders/42", link.href)
		assertTrue(link.hasClass("link"))
		assertFalse(link.hasClass("notLink"))
    }

    @Test
    fun serializeToJson() {
        val json = """{ "rel": [ "self" ], "class": [ "link" ], "title": "my link", "type": "text/plain", "href": "http://api.x.io/orders/42" }"""
        val link = Link.fromJson(json.toKSirenJsonReader())

        // order of properties matters...
        assertEquals(json, link.toJson())
    }
}
