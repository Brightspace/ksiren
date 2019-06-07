package com.brightspace.ksiren

fun readAndReserializeArray(reader: KSirenJsonReader): String {
	val arrayList: MutableList<String> = mutableListOf()

	reader.beginArray()
	while (reader.hasNext()) {
		conditionalRead(reader) { arrayList.add(it) }
	}
	reader.endArray()

	return arrayList.joinToString(prefix = "[", postfix = "]", separator = ", ")
}
