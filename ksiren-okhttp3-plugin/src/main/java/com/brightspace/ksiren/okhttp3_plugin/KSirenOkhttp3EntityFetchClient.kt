package com.brightspace.ksiren.okhttp3_plugin

import com.brightspace.ksiren.KSirenEntityFetchClient
import com.brightspace.ksiren.KSirenJsonReader
import com.brightspace.ksiren.KSirenJsonReaderFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Created by mpomeroy on 11/2/17.
 */
class KSirenOkhttp3EntityFetchClient(val okhttpClient: OkHttpClient, val ksirenJsonReaderFactory: KSirenJsonReaderFactory): KSirenEntityFetchClient() {
	override fun executeCall(href: String): KSirenJsonReader {
		return ksirenJsonReaderFactory
				.getKSirenJsonReader(okhttpClient.newCall(
					Request
						.Builder()
						.url(href)
						.build())
					.execute()
					.body()
					?.string()?: throw IOException("Request response body is empty."))
	}
}
