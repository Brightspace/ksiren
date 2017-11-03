package com.brightspace.ksiren

/**
 * Created by mpomeroy on 11/2/17.
 */
interface KSirenJsonReaderFactory {
	fun getKSirenJsonReader(string: String): KSirenJsonReader
}
