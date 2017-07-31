package com.brightspace.ksiren

import com.squareup.moshi.JsonReader
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.BufferedSource
import okio.Okio
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

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
fun String.toJsonReader(): JsonReader {
    return JsonReader.of(
            Okio.buffer(
                    Okio.source(
                            ByteArrayInputStream(
                                    this.toByteArray(StandardCharsets.UTF_8)))))
}

class EntityFromCallException(override val message: String, val errorCode: Int): IllegalArgumentException(message)

fun Entity.Companion.from(request: Request, client: OkHttpClient): Observable<Entity> {
    return Observable.create<Entity> {
        subscriber ->

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                subscriber.onNext(Entity.from(response))
            } else {
                subscriber.onError(EntityFromCallException("Response unsuccessful: ${response.body()?.string()}", response.code()))
            }

            subscriber.onComplete()
        } catch (e: Throwable) {
            subscriber.onError(e)
        }
    }
}

fun Entity.Companion.from(source: BufferedSource): Entity {
    return fromJson(JsonReader.of(source))
}

fun Entity.Companion.from(response: Response): Entity {
    val body: BufferedSource? = response.body()?.source()
    if (body != null) {
        return Entity.from(body)
    }
    throw IllegalStateException("Body of the response was null.")
}

fun Entity.Companion.from(source: String): Entity {
    return fromJson(source.toJsonReader())
}

