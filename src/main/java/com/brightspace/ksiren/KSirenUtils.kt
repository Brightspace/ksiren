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
 * Created by mpomeroy on 5/3/17.
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

