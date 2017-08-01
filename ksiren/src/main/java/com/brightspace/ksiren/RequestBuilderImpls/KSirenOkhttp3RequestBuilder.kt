package com.brightspace.ksiren.RequestBuilderImpls

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
class KSirenOkhttp3RequestBuilder(wrappedAction: Action): KSirenRequestBuilder<Request>(wrappedAction) {

	override fun build(): Request {
		val requestBuilder: Request.Builder = Request.Builder()
		val urlBuilder: HttpUrl.Builder? = HttpUrl.parse(action.href)?.newBuilder()

		if (action.fields.isNotEmpty()) {
			when (action.method) {
				"GET" -> {
					fieldValues.forEach {
						(name, value) ->

						urlBuilder?.addQueryParameter(name, value)
					}
					requestBuilder.get()
				}
				"POST" -> {
					requestBuilder.post(getBody())
				}
			}
		}

		requestBuilder.url(urlBuilder?.build())

		return requestBuilder.build()
	}

	private fun getBody(): RequestBody {
		when (action.type) {
			ContentType.JSON -> {
				val mediaType = MediaType.parse(action.type.value)

				val json = StringBuilder()
				json.append("{")

				var count = 0
				fieldValues.forEach { (name, value) ->
					if (count > 0) {
						json.append(", ")
					}

					json.append("\"")
					json.append(name)
					json.append("\": \"")
					json.append(value)
					json.append("\"")

					count += 1
				}

				json.append("}")
				return RequestBody.create(mediaType, json.toString())
			}

			ContentType.FORM -> {
				val requestBodyBuilder: MultipartBody.Builder = MultipartBody.Builder()
					.setType(MultipartBody.FORM)

				fieldValues.forEach {
					(name, value) ->
					requestBodyBuilder.addFormDataPart(name, value)
				}

				return requestBodyBuilder.build()
			}
		}
	}
}
