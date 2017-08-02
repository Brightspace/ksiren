package com.brightspace.ksiren

import com.brightspace.ksiren.okhttp3_request_builder.KSirenOkhttp3RequestBuilder
import okhttp3.Request
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
class KSirenRequestBuilderTest {

    fun returnAction(method: String): Action {
        return Action("test", listOf(), method, "http://www.example.com", "title", ContentType.FORM,
                listOf(Field("testParam", listOf(), "text", null)))
    }

    @Test
    fun testGetBuilder() {
        val action: Action = returnAction("GET")
        val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(action)
        requestBuilder.addFieldValue("testParam", "testValue")
        assertEquals("http://www.example.com/?testParam=testValue", requestBuilder.build().url().toString())
    }

    @Test
    fun testPostBuilder() {
        val action: Action = returnAction("POST")
        val requestBuilder: KSirenRequestBuilder<Request> = KSirenOkhttp3RequestBuilder(action)
        requestBuilder.addFieldValue("testParam", "testValue")
        requestBuilder.build()

        //As far as I know there isn't a good way to verify the requestbody
        //however, doing so would only be testing Okhttp3 anyway. Ensuring
        //this doesn't throw an exception should be good enough.
        assertTrue(true)
    }
}
