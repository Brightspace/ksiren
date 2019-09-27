package com.brightspace.ksiren

/**
 * Created by mpomeroy on 11/8/17.
 */
fun conditionalRead(reader: KSirenJsonReader, setter:(String) -> Unit) {
	val string = tryParseWithLambdasAsString(reader, { it.nextString() }, { it.nextBoolean().toString() })
	if (string != null) {
		setter.invoke(string)
	}
}
