package com.brightspace.ksiren

fun tryExecuteParsingLambdas(reader: KSirenJsonReader, vararg mappers: (KSirenJsonReader) -> String?): String? {
	for (mapper in mappers) {
		try {
			return mapper.invoke(reader)
		} catch (e: Exception) {
			//this mapper failed
		}
	}

	throw KSirenException.ParseException("Could not parse value as String or list. KSiren does not support object types")
}
