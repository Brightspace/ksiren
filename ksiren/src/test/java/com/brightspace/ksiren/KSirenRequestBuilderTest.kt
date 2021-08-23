package com.brightspace.ksiren

import com.brightspace.ksiren.moshi_adapter.KSirenMoshiWriter
import com.brightspace.ksiren.okhttp3_request_builder.KSirenOkhttp3RequestBuilder
import okhttp3.MultipartBody
import okhttp3.Request
import okio.BufferedSink
import okio.Okio
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

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

	private fun createAction(method: String, contentType: ContentType): Action {
		return Action(
			name = "test",
			classes = listOf(),
			method = method,
			href = "http://www.example.com",
			title = "title",
			type = contentType,
			fields = listOf(
				Field("testParam", listOf(), "text", null),
				Field("unpopulatedParam", listOf(), "text", null)
			)
		)
	}

	@Test
	fun testGetBuilder() {
		val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(
			createAction("GET", ContentType.FORM),
			KSirenMoshiWriter()
		)
		requestBuilder.addFieldValue("testParam", "testValue")
		assertEquals("http://www.example.com/?testParam=testValue", requestBuilder.build().url().toString())
	}

	@TestFactory
	fun testPostBuilder() = listOf(
		ContentType.FORM,
		ContentType.JSON
	).map { contentType ->
		DynamicTest.dynamicTest("when Content-Type is $contentType") {
			val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(
				createAction("POST", contentType),
				KSirenMoshiWriter()
			)
			requestBuilder.addFieldValue("testParam", "testValue")

			val res = requestBuilder.build()

			when (contentType) {
				ContentType.JSON -> {
					assertEquals("http://www.example.com/?testParam=testValue", res.url().toString())
				}
				ContentType.FORM -> {
					assertEquals("http://www.example.com/", res.url().toString())
					assertTrue(res.body() is MultipartBody)

					if (res.body() is MultipartBody) {
						val body = res.body() as MultipartBody

						val outputStream = ByteArrayOutputStream()
						val outputSink = Okio.sink(outputStream)
						val contentSink = Okio.buffer(outputSink)

						assertEquals(1, body.parts().size)
						assertEquals("Content-Disposition: form-data; name=\"testParam\"\n", body.part(0).headers().toString())

						body.part(0).body().writeTo(contentSink)
						contentSink.flush()

						assertEquals("testValue", outputStream.toString())
					}
				}
				else -> {
					fail("Unknown Content-Type: ${contentType}")
				}
			}
		}
	}
}
