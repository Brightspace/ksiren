package com.brightspace.ksiren.gson_adapter

import com.brightspace.ksiren.KSirenJsonReader
import com.brightspace.ksiren.KSirenJsonReaderFactory
import com.google.gson.stream.JsonReader
import java.io.StringReader

/**
 * Created by mpomeroy on 11/2/17.
 */
class KSirenGsonReaderFactory: KSirenJsonReaderFactory {
	override fun getKSirenJsonReader(string: String): KSirenJsonReader {
		return KSirenGsonAdapter(JsonReader(StringReader(string)))
	}
}
