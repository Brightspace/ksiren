package com.brightspace.ksiren

import com.brightspace.ksiren.okhttp3_request_builder.KSirenOkhttp3RequestBuilder
import okhttp3.Request
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

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
class KSirenRequestBuilderTest {

	private fun returnAction(method: String, contentType: ContentType): Action {
		return Action(
			"test",
			listOf(),
			method,
			"http://www.example.com",
			"title",
			contentType,
			listOf(Field("testParam", listOf(), "text", null)))
	}

	@Test
	fun testGetBuilder() {
		val action: Action = returnAction("GET", ContentType.FORM)
		val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(action)
		requestBuilder.addFieldValue("testParam", "testValue")
		assertEquals("http://www.example.com/?testParam=testValue", requestBuilder.build().url().toString())
	}

	@TestFactory
	fun testPostBuilder() = listOf(
		ContentType.FORM,
		ContentType.JSON
	).map { contentType ->
			DynamicTest.dynamicTest("when Content-Type is $contentType") {
				val action = returnAction("POST", contentType)
				val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(action)
				requestBuilder.addFieldValue("testParam", "testValue")
				requestBuilder.build()

				//As far as I know there isn't a good way to verify the requestbody
				//however, doing so would only be testing Okhttp3 anyway. Ensuring
				//this doesn't throw an exception should be good enough.
				Assertions.assertTrue(true)
			}
		}
}
