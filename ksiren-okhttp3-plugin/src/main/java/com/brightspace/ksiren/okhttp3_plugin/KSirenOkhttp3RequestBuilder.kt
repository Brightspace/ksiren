package com.brightspace.ksiren.okhttp3_request_builder

import com.brightspace.ksiren.Action
import com.brightspace.ksiren.ContentType
import com.brightspace.ksiren.KSirenRequestBuilder
import okhttp3.*

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
class KSirenOkhttp3RequestBuilder(wrappedAction: Action) : KSirenRequestBuilder<Request>(wrappedAction) {

	override fun build(): Request {
		val requestBuilder: Request.Builder = Request.Builder()
		val urlBuilder: HttpUrl.Builder = HttpUrl.parse(action.href)?.newBuilder()
			?: throw Exception("Action href could not be parsed")

		if (action.fields.isNotEmpty()) {
			when (action.method) {
				"GET" -> {
					action.fields.forEach {
						urlBuilder.addQueryParameter(it.name, it.value)
					}
					requestBuilder.get()
				}
				"POST" -> {
					requestBuilder.post(getBody())
				}
			}
		}

		requestBuilder.url(urlBuilder.build())

		return requestBuilder.build()
	}

	private fun getBody(): RequestBody {
		when (action.type) {
			ContentType.JSON -> {
				return RequestBody.create(MediaType.parse(action.type.value), action.toJsonRequestBody())
			}

			ContentType.FORM -> {
				val requestBodyBuilder: MultipartBody.Builder = MultipartBody.Builder()
					.setType(MultipartBody.FORM)

				action.fields
					.mapNotNull { field -> field.value?.let { value -> field.name to value } }
					.forEach() { pair -> requestBodyBuilder.addFormDataPart(pair.first, pair.second) }

				return requestBodyBuilder.build()
			}
		}
	}
}
