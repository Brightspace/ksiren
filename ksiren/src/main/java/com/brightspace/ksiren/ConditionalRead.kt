package com.brightspace.ksiren

/**
 * Created by mpomeroy on 11/8/17.
 */
fun conditionalRead(reader: KSirenJsonReader, setter:(String) -> Unit) {
	val string = reader.nextString()
	if (string != null) {
		setter.invoke(string)
	}
}
