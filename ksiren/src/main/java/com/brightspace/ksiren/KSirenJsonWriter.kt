package com.brightspace.ksiren

import java.io.Closeable

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
interface KSirenJsonWriter: Closeable {
	fun getSerializedString(): String

	fun beginObject(): KSirenJsonWriter
	fun endObject(): KSirenJsonWriter

	fun beginArray(): KSirenJsonWriter
	fun endArray(): KSirenJsonWriter

	override fun close()

	fun name(name: String): KSirenJsonWriter

	fun value(value: String?): KSirenJsonWriter
	fun value(value: Number?): KSirenJsonWriter
	fun value(value: Double?): KSirenJsonWriter
	fun value(value: Boolean?): KSirenJsonWriter
}
