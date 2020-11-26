package com.brightspace.ksiren

/**
 * Created by mpomeroy on 11/2/17.
 */
abstract class KSirenEntityFetchClient {
	abstract fun executeCall(href: String): KSirenJsonReader
}
