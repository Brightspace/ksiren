package com.brightspace.ksiren.moshi_adapter

import com.brightspace.ksiren.KSirenJsonReaderFactory
import com.squareup.moshi.JsonReader
import okio.Okio
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

/**
 * Created by mpomeroy on 11/2/17.
 */
class KSirenMoshiReaderFactory: KSirenJsonReaderFactory {
	override fun getKSirenJsonReader(string: String): KSirenMoshiAdapter {
		return KSirenMoshiAdapter(JsonReader.of(
			Okio.buffer(
				Okio.source(
					ByteArrayInputStream(
						string.toByteArray(
							StandardCharsets.UTF_8))))))
	}
}
