package com.brightspace.ksiren

fun tryParseWithLambdasAsString(reader: KSirenJsonReader, vararg parsingLambdas: (KSirenJsonReader) -> String?): String? {
	for (parser in parsingLambdas) {
		try {
			return parser.invoke(reader)
		} catch (e: Exception) {
			//this mapper failed
		}
	}

	throw KSirenException.ParseException("Could not parse value as String or list. KSiren does not support object types")
}
